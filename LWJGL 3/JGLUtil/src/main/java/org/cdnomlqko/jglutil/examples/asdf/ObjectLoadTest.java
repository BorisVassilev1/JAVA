package org.cdnomlqko.jglutil.examples.asdf;

import org.cdnomlqko.jglutil.Renderer;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.data.Camera;
import org.cdnomlqko.jglutil.data.textures.Texture2D;
import org.cdnomlqko.jglutil.examples.ApplicationBase;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.shader.VFShader;
import org.cdnomlqko.jglutil.utils.ModelLoader;
import org.cdnomlqko.jglutil.utils.TransformController;
import org.joml.Matrix3d;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL46.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class ObjectLoadTest extends ApplicationBase {

	static int width = 800, height = 600;
	static int squishFactor = 4;

	CameraGameObject camera;

	float fov = (float) Math.toRadians(70f);

	MeshedGameObject obj;

	TransformController controller;

	VFShader shader;
	Texture2D renderTexture;
	int fbo = -1;
	int depth_and_stencil = -1;

	String dirName;

	static String sceneFileName = "scene.glb";
	static String offsetFileName = "offset.xyz";
	static String imagesFileName = "images.json";
	static String outputDirectory = "depth_textures/";

	@SuppressWarnings("all")
	private class ImageData {
		public Vector3d position;
		public Vector3d rotation;
		public Vector2d principalPoint;
		public Vector2i imageDimensions;
		public String id;

		public ImageData(Vector3d position, Vector3d rotation, Vector2d principalPoint, Vector2i imageDimensions, String id) {
			this.position = position;
			this.rotation = rotation;
			this.principalPoint = principalPoint;
			this.imageDimensions = imageDimensions;
			this.id = id;
		}
	}

	ArrayList<ImageData> images = new ArrayList<>();

	Vector3d offset = new Vector3d();

	public ObjectLoadTest(String dirName) {
		this.dirName = dirName;
	}

	@Override
	public void init() {
		camera = new CameraGameObject(new Camera(fov, (float) window.getWidth() / (float) window.getHeight(), 0.01f, 1000f));
		camera.transform.setPosition(new Vector3f(0, 0, 2));
		camera.transform.updateWorldMatrix();

		frm.stop();

		AIScene aiSc = ModelLoader.loadAIScene(dirName + sceneFileName, ModelLoader.default_flags);

		shader = new VFShader("/res/shaders/verfrag_shaders/BasicVertexShader.vs",
				"/res/shaders/verfrag_shaders/DepthFragmentShader.fs");

		obj = new MeshedGameObject(ModelLoader.getMesh(aiSc, 0), null, shader);
		System.out.println("triangle count: " + obj.getMesh().getIndicesCount() / 3);

		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

//		glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

		// parse jsons
		JSONParser parser = new JSONParser();

		JSONObject jobj = null;
		try {
			Object j = parser.parse(new FileReader(dirName + imagesFileName));
			jobj = (JSONObject) j;
		} catch (Exception e) {
			e.printStackTrace();
		}

		jobj = (JSONObject) jobj.get("BlocksExchange");
		jobj = (JSONObject) jobj.get("Block");
		jobj = (JSONObject) jobj.get("Photogroups");
		Object pg = jobj.get("Photogroup");
		if (pg instanceof JSONArray) {
			JSONArray photogroup = (JSONArray) pg;

			for (int i = 0; i < photogroup.size(); i++) {
				parseImageGroup((JSONObject) photogroup.get(i));
			}
		} else {
			JSONObject photogroup = (JSONObject) pg;

			parseImageGroup(photogroup);
		}

		images.sort(new Comparator<ImageData>() {
			@Override
			public int compare(ImageData o1, ImageData o2) {
				return o1.id.compareTo(o2.id);
			}
		});

		// set up a texture to draw on and a depth buffer;
		renderTexture = new Texture2D(width, height, GL_R32F, GL_RED);

		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);

		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTexture.getID(), 0);

		depth_and_stencil = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, depth_and_stencil);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, width, height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, (ByteBuffer) null);
		glBindTexture(GL_TEXTURE_2D, 0);

		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, depth_and_stencil, 0);

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("Framebuffer done!");
		} else {
			System.out.println("FAIL");
			System.exit(1);
		}

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // we're not using the stencil buffer now
		glEnable(GL_DEPTH_TEST);

		// the new framebuffer will be bound
//		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		// parse the offset.xyz
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(dirName + offsetFileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		offset.x += Double.parseDouble(scanner.next());
		offset.y += Double.parseDouble(scanner.next());
		offset.z += Double.parseDouble(scanner.next());
		scanner.close();
		System.out.println(offset);

		System.out.println("Images Count: " + images.size());

		int percent = images.size() / 100;
		for (int i = 0; i < images.size() && !window.shouldClose(); i++) {
			GLFW.glfwPollEvents();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			ImageData data = images.get(i);

			if (width != data.imageDimensions.x || height != data.imageDimensions.y) {
				// resize everything;

				width = data.imageDimensions.x;
				height = data.imageDimensions.y;

				// resize the main texture
				renderTexture.bind();
				glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, width, height, 0, GL_RED, GL_UNSIGNED_BYTE, (ByteBuffer) null);
				renderTexture.setWidth(width);
				renderTexture.setHeight(height);
				renderTexture.unbind();

				// resize the viewport
				glViewport(0, 0, width, height);

				// resize depth and stencil buffer
				glBindTexture(GL_TEXTURE_2D, depth_and_stencil);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, width, height, 0, GL_DEPTH_STENCIL,
						GL_UNSIGNED_INT_24_8, (ByteBuffer) null);
				glBindTexture(GL_TEXTURE_2D, 0);
			}

			// set correct camera transform
			camera.transform.getPosition().set(data.position);
			camera.transform.getRotation().set(data.rotation);
			camera.transform.updateWorldMatrix();
			camera.update();
			Renderer.draw(obj);

			window.swapBuffers();

			renderTexture.saveBin(dirName + outputDirectory + i + ".bin");
//			renderTexture.save(dirName + outputDirectory + i + ".png");
			if (i % percent == 0)
				System.out.print("\rProcessing: " + i / percent + "%");
		}

		System.exit(0);
	}

	private void parseImageGroup(JSONObject group) {
		JSONObject ppoint = (JSONObject) group.get("PrincipalPoint");
		JSONObject dimensions = (JSONObject) group.get("ImageDimensions");

		Vector2d principalPoint = new Vector2d();
		principalPoint.x = Double.parseDouble((String) ppoint.get("x"));
		principalPoint.y = Double.parseDouble((String) ppoint.get("y"));

		Vector2i imageDimensions = new Vector2i();
		imageDimensions.x = Integer.parseInt((String) dimensions.get("Width"));
		imageDimensions.y = Integer.parseInt((String) dimensions.get("Height"));

		imageDimensions.set(imageDimensions.x / squishFactor, imageDimensions.y / squishFactor);

//		width = imageDimensions.x;
//		height = imageDimensions.y;
		System.out.println("Width: " + width + " Height: " + height);

		JSONArray photos = (JSONArray) group.get("Photo");

		for (int i = 0; i < photos.size(); i++) {
			JSONObject photo = (JSONObject) photos.get(i);
			System.out.println(photo.toJSONString());

			JSONObject pose = (JSONObject) photo.get("Pose");

			JSONObject position = (JSONObject) pose.get("Center");
			Vector3d pos = new Vector3d();
			pos.x = Double.parseDouble((String) position.get("x"));
			pos.y = Double.parseDouble((String) position.get("y"));
			pos.z = Double.parseDouble((String) position.get("z"));

			pos.sub(offset);

			// the rotation matrix
			JSONObject rotation = (JSONObject) pose.get("Rotation");
			Matrix3d mat = new Matrix3d();
			double[] m = { Double.parseDouble((String) rotation.get("M_00")),
					Double.parseDouble((String) rotation.get("M_10")),
					Double.parseDouble((String) rotation.get("M_20")),
					Double.parseDouble((String) rotation.get("M_01")),
					Double.parseDouble((String) rotation.get("M_11")),
					Double.parseDouble((String) rotation.get("M_21")),
					Double.parseDouble((String) rotation.get("M_02")),
					Double.parseDouble((String) rotation.get("M_12")),
					Double.parseDouble((String) rotation.get("M_22")) };
			mat.set(m);

			Vector3d rot = new Vector3d();
			Quaterniond q = new Quaterniond();
			mat.getUnnormalizedRotation(q);
			q.getEulerAnglesXYZ(rot);

			rot.set(rot.x + Math.PI, rot.y, rot.z);

			String name = (String) photo.get("ImagePath");

			ImageData curr = new ImageData(pos, rot, principalPoint, imageDimensions, name);
			images.add(curr);
		}
	}

	@Override
	public void loop() {
	}

	@Override
	public void gui() {
	}

	@Override
	public void cleanup() {
		glDeleteFramebuffers(fbo);
		renderTexture.delete();
		glDeleteTextures(depth_and_stencil);
	}

	public static void main(String[] args) {
		if (args.length == 5) {
			sceneFileName = args[1];
			offsetFileName = args[2];
			imagesFileName = args[3];
			outputDirectory = args[4];
		}
		new ObjectLoadTest(args[0]).run(new Window("Test", width, height, true, false, false));
	}
}
