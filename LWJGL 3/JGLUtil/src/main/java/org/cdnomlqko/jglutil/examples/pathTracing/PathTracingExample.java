package org.cdnomlqko.jglutil.examples.pathTracing;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.cdnomlqko.jglutil.*;
import org.cdnomlqko.jglutil.data.*;
import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy.Node;
import org.cdnomlqko.jglutil.data.textures.Texture2D;
import org.cdnomlqko.jglutil.data.textures.TextureCubeMap;
import org.cdnomlqko.jglutil.examples.ApplicationBase;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.LightGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.mesh.BasicMesh;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.ComputeShader;
import org.cdnomlqko.jglutil.shader.ShaderUtils;
import org.cdnomlqko.jglutil.shader.VFShader;
import org.cdnomlqko.jglutil.utils.FramerateManager;
import org.cdnomlqko.jglutil.utils.ModelLoader;
import org.cdnomlqko.jglutil.utils.TransformController;

public class PathTracingExample extends ApplicationBase {

	VFShader renderQuadShader;
	ShadedGameObject renderingQuad;
	Texture2D renderTexture;
	Texture2D rawTexture;
	TextureCubeMap envTexture;
	
	ComputeShader tracer;
	ComputeShader generator;
	ComputeShader normalizer;
	
	CameraGameObject camera;
	//Transformation cameraTransform;
	
	TransformController controller;
	
	Scene sc;
	
	float[] fov = new float[] {(float) Math.toRadians(70f)};
	
	boolean ray_tracing_enabled = false;
	
	int rays_per_pixel = 100000;
	
	int ray_structure_size = 48;
	int rays_buffer_size = -1;
	int rays_buffer = -1;
	
	int rays_sent = 0;
	
	long random_seed = 0;
	Random rand = new Random(0);
	
	int[] max_depth = new int[]{6};
	
	float[] base_sph_arr = new float[] {
			 0.2f, 1.3f, 0.2f, 0.0f, 1.0f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.0f, 0.0f,
			 1.0f, 0.2f, 0.7f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.7f, 0.0f, 0.0f, 0.0f,
			-0.7f, 0.5f, 0.2f, 0.0f, 0.9f, 0.9f, 1.0f, 1.0f, 0.5f, 2.0f, 0.0f, 0.0f,
			-0.1f, 1.3f, 1.7f, 0.0f, 10.0f, 10.0f, 10.0f, 1.0f, 0.4f, 3.0f, 0.0f, 0.0f
			};
	
	float[] small_sph_arr;
	
	int sph_count = 60;
	float sph_spawn_range = 3;
	int sph_size = 12;
	int spheres_buff = -1;
	float sph_spread = 2.f;
	
	boolean must_update_rays = true;
	
	public static BoundingVolumeHierarchy bvh;
	
	
	int fps = -1;
	
	void fill_sph_arr() {

		sph_count = (int)sph_spawn_range * (int)sph_spawn_range;
		small_sph_arr = new float[sph_count * sph_size];
		Random rand = new Random(); 
		for(int i = 0; i < sph_spawn_range; i ++) {
			for(int j = 0; j < sph_spawn_range; j ++) {
				Vector3f pos = new Vector3f((rand.nextFloat() * (sph_spread - .1f)) + i * sph_spread - sph_spawn_range / 2 * sph_spread, 0.2f, (rand.nextFloat() * (sph_spread - .1f)) + j * sph_spread - sph_spawn_range / 2 * sph_spread);
				
				Vector3f color = new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
				
				float type = (float) Math.floor(rand.nextFloat() * 4);
				if(type == 3.0f)  color.mul(10);
				
				int id = i + j * (int)sph_spawn_range;
				small_sph_arr[id * sph_size    ] = pos.x;
				small_sph_arr[id * sph_size + 1] = pos.y;
				small_sph_arr[id * sph_size + 2] = pos.z;
	
				small_sph_arr[id * sph_size + 4] = color.x;
				small_sph_arr[id * sph_size + 5] = color.y;
				small_sph_arr[id * sph_size + 6] = color.z;
				small_sph_arr[id * sph_size + 7] = 1.0f;
	
				small_sph_arr[id * sph_size + 8] = pos.y;
				small_sph_arr[id * sph_size + 9] = type;
			}
		}
	}
	
	void fill_sph_buffer() {
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, spheres_buff);
		fill_sph_arr();
		int sph_buff_size = sph_size * (sph_count + 4);
		glBufferData(GL_SHADER_STORAGE_BUFFER, BufferUtils.createFloatBuffer(sph_buff_size), GL_DYNAMIC_DRAW);
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, base_sph_arr);
		glBufferSubData(GL_SHADER_STORAGE_BUFFER, 4 * sph_size * 4, small_sph_arr);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		must_update_rays = true;
	}
	
	void init_shaders() {
		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		renderingQuad = new ShadedGameObject(MeshUtils.makeQuad(2f), renderQuadShader);
		
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());
		renderTexture.bindImage(0);
		rawTexture = new Texture2D(renderTexture.getWidth(), renderTexture.getHeight());
		rawTexture.bindImage(1);
		
		envTexture = new TextureCubeMap("./res/skybox", ".jpg");
		//envTexture.bind(GL_TEXTURE3);
		//glBindTextureUnit(5, );
		
		int rays_count = renderTexture.getWidth() * renderTexture.getHeight();
		
		generator = new ComputeShader("./res/shaders/compute_shaders/PathTracing/RayGenerator.comp");
		
		rays_buffer_size = rays_count * ray_structure_size;
		System.out.println(rays_buffer_size);
		rays_buffer = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, rays_buffer);
		glBufferData(GL_SHADER_STORAGE_BUFFER, BufferUtils.createByteBuffer(rays_buffer_size), GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		
		generator.bind();
		generator.setUniform("resolution", new Vector2f(renderTexture.getWidth(), renderTexture.getHeight()));
		generator.setUniform("fov", camera.getCamera().getFov());
		generator.setUniform("cameraMatrix", camera.transform.getWorldMatrix());
		generator.unbind();
		
		tracer = new ComputeShader("./res/shaders/compute_shaders/PathTracing/PathTracingShader.comp");
		// pass data for sphere properties to the compute shader
		spheres_buff = glGenBuffers();
		fill_sph_buffer();
		
		tracer.bind();
		//tracer.setUniform("rays_sent", rays_per_pixel);
		tracer.setUniform("resolution", new Vector2f(renderTexture.getWidth(), renderTexture.getHeight()));
		tracer.setUniform("img_output", 1);
		tracer.unbind();
		
		Renderer.Compute(generator, renderTexture.getWidth(), renderTexture.getHeight(), 1);
		
		normalizer = new ComputeShader("./res/shaders/compute_shaders/PathTracing/MultisampleNormalizer.comp");
		normalizer.bind();
		normalizer.setUniform("img_input", 1);
		normalizer.setUniform("img_output", 0);
		normalizer.unbind();
		
		set_ray_tracing_ssbos();
		
		ShaderUtils.init();
		MeshUtils.init();
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	
	void set_ray_tracing_ssbos() {
		generator.setSSBO("Rays", rays_buffer);
		tracer.setSSBO("spheres", spheres_buff);
		tracer.setSSBO("Rays", rays_buffer);
	}
	
	void init_utils() {
		input.lockMouse = true;
		controller = new TransformController(input, camera.transform);
		controller.speed /= 1.;
		frm.setSecondPassedCallback( (fps) -> {
			this.fps = (int)fps;
		});
	}
	
	void init_mesh() {
		sc = new Scene(ShaderUtils.getLitShader());
		sc.setActiveCamera(camera);
		
		//BasicMesh modelMesh = ModelLoader.loadMesh("./res/cube.obj");
		//BasicMesh modelMesh = ModelLoader.loadMesh("D:/Boby/3D_Maya/Modeling/scenes/firestorm.obj");
		BasicMesh modelMesh = ModelLoader.loadMesh("D:/Boby/blender/Shardblade.obj");
		//BasicMesh modelMesh = ModelLoader.loadMesh("C:/Users/Boby/Documents/sumTest.obj");
		
		
		MeshedGameObject model = new MeshedGameObject(modelMesh, new Material(new Vector3f(1.0f, 0.0f, 0.0f)), null);
		model.transform.setScale(0.1f);
		model.transform.setRotation(new Vector3f(0, .0f, 0));
		model.transform.setPosition(new Vector3f(-1.8f, 0f, 0.8f));
		model.transform.updateWorldMatrix();
		model.register(sc);
		
		LightGameObject light = new LightGameObject(Light.Type.DIRECTIONAL_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 0.7f);
	    light.transform.setPosition(new Vector3f(4, 5, 0));
	    light.transform.setRotation(new Vector3f(1f, -.3f, 0f));
	    light.transform.setScale(0.5f);
	    light.transform.updateWorldMatrix();
	    light.register(sc);
	    
	    LightGameObject ambient = new LightGameObject(Light.Type.AMBIENT_LIGHT, new Vector3f(.1f), 1.f);
	    ambient.register(sc);
		
		sc.updateBuffers();
		
		bvh = new BoundingVolumeHierarchy(modelMesh, model.transform);
		System.out.println("Max depth: " + bvh.max_depth);
		
		int vertices = modelMesh.getVertices().getBufferId();
		int normals = modelMesh.getNormals().getBufferId();
		int indices = modelMesh.getIbo();
		
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, indices);
		int size[] = {-1};
		glGetBufferParameteriv(GL_SHADER_STORAGE_BUFFER, GL_BUFFER_SIZE, size);
		System.out.println(size[0]);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		System.out.println(vertices + " " + normals + " " + indices);
		
		tracer.setSSBO("Vertices", vertices);
		tracer.setSSBO("Normals", normals);
		tracer.setSSBO("Indices", indices);
		
		//int nodes_count = size[0] / 4 / 3 * 4; // 4 bytes * 3 floats 
		int nodes_count = (int)Math.pow(2, bvh.max_depth + 1);
		int buff_size = nodes_count * Node.size;
		ByteBuffer buff = BufferUtils.createByteBuffer(buff_size);
		bvh.writeToBuffer(buff, 0);
		buff.rewind();
		
		int BVH = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, BVH);
		glBufferData(GL_SHADER_STORAGE_BUFFER, buff, GL_STATIC_READ);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		
		tracer.setSSBO("BVH", BVH);
		tracer.bind();
		if(tracer.hasUniform("max_depth"))
			tracer.setUniform("max_depth", bvh.max_depth);
		if(tracer.hasUniform("max_bounces"))
			tracer.setUniform("max_bounces", max_depth[0]);
		if(tracer.hasUniform("bvh_matrix"))
			tracer.setUniform("bvh_matrix", model.transform.getWorldMatrix());
		tracer.unbind();
	}
	
	@Override
	public void init() {
		camera = new CameraGameObject(new Camera(fov[0],(float) window.getWidth() / (float)window.getHeight(), 0.01f, 1000f));
		//cameraTransform = new Transformation();
		
		camera.transform.setPosition(new Vector3f(-3.989e0f, 2.000e0f,  1.741e0f));
		camera.transform.setRotation(new Vector3f( 3.520e-1f, 1.266e0f,  0.000e0f));
		camera.transform.updateWorldMatrix();
		try {
		init_utils();
		
		init_shaders();
		
		init_mesh();
		} catch(Exception e) {
			e.printStackTrace();
			cleanup();
			System.exit(1);
		}
		
		
		if(ray_tracing_enabled) {
			set_ray_tracing_ssbos();
		} else {
			sc.bindBuffersToSSBOs();
		}
	}
	
	void trace_rays() {
		renderTexture.bind(GL_TEXTURE0);
		//envTexture.bind(GL_TEXTURE5);
		glBindTextureUnit(5,  envTexture.getID());
		
		// if there is a change in the camera's position/rotation
		if(controller.hasChanged() || must_update_rays) {
			// init render again
			glClearTexImage(rawTexture.getID(), 0, GL_RGBA, GL_FLOAT, new float[] {0f,0f,0f,0f});
			rays_sent = 0;
			must_update_rays = false;
		}
		
		// continue next iteration
		if(rays_sent < rays_per_pixel) {
			Renderer.Compute(generator, renderTexture.getWidth(), renderTexture.getHeight(), 1, () -> {
				generator.setUniform("cameraMatrix", camera.transform.getWorldMatrix());
				generator.setUniform("fov", camera.getCamera().getFov());
				float randf = rand.nextFloat();
				generator.setUniform("random_seed", randf);
			});
			
			// light bounces...
			
			//for(int i = 0; i < max_depth; i ++) {
			//	final boolean is_end = i == max_depth - 1;
				Renderer.Compute(tracer, renderTexture.getWidth(), renderTexture.getHeight(), 1, () -> {
					if(tracer.hasUniform("random_seed"))
						tracer.setUniform("random_seed", rand.nextFloat());
					if(tracer.hasUniform("skybox")) 
						tracer.setUniform("skybox", 5);
						
					//if(tracer.hasUniform("end"))
					//	tracer.setUniform("end", 1);
				});
			//}
			rays_sent ++;
		}
		else if(rays_sent == rays_per_pixel) {
			System.out.println("image finished!");
			rays_sent ++;
		}
		
		//envTexture.unbind(GL_TEXTURE5);
		
		Renderer.Compute(normalizer, renderTexture.getWidth(), renderTexture.getWidth(), 1, () -> {
			normalizer.setUniform("samples", rays_sent);
		});
		
		//draw the quad on the screen. it will use texture0.
		
		renderTexture.bind(GL_TEXTURE0);
		Renderer.draw(renderingQuad);
		renderTexture.unbind(GL_TEXTURE0);
	}
	
	@Override
	public void loop() {
			controller.update();

			// the shader will write to texture0. 
			//renderTexture.bind(GL_TEXTURE0);
			
			if(ray_tracing_enabled) {
				trace_rays();
			} else {
				sc.draw();
				//bvh.draw();
			}
		
			
			// handle key presses
			if (input.getKey(GLFW_KEY_2) == GLFW_PRESS) {
				renderTexture.save("./res/image.png");
				System.out.println("saved image");
			}
			if(input.getKey(GLFW_KEY_I) == GLFW_PRESS) {
				System.out.println("Samples: " + rays_sent);
				System.out.println("camera: " + camera.transform.getPosition() + "\n" + camera.transform.getRotation());
			}
			if(input.getKey(GLFW_KEY_R) == GLFW_PRESS) {
				fill_sph_buffer();
				System.out.println("reloaded spheres");
			}
			if(input.getKey(GLFW_KEY_T) == GLFW_PRESS && !ray_tracing_enabled) {
				ray_tracing_enabled = true;
				set_ray_tracing_ssbos();
				must_update_rays = true;
			}
			if(input.getKey(GLFW_KEY_Y) == GLFW_PRESS && ray_tracing_enabled) {
				ray_tracing_enabled = false;
				//sc.updateBuffers();
				System.out.println("asdfsa");
				sc.bindBuffersToSSBOs();
				System.out.println("asda");
			}
	}

	@Override
	public void cleanup() {
		//delete everything
		renderingQuad.getMesh().delete();
		renderQuadShader.delete();
		generator.delete();
		tracer.delete();
		renderTexture.delete();
	}
	
	// the main method will start the application
	public static void main(String[] args) {
		new PathTracingExample().run(new Window("nqkva glupost bate", 1000, 800, false, true));
	}

	@Override
	public void gui() {
		ImGui.begin("Settings");
		
		ImGui.text("FPS: " + fps);
		
		if(ImGui.sliderInt("maximul light bounces", max_depth, 0, 8)) {
			tracer.bind();
			if(tracer.hasUniform("max_bounces"))
				tracer.setUniform("max_bounces", max_depth[0]);
			tracer.unbind();
			must_update_rays = true;
		}
		
		if(ImGui.sliderAngle("FOV", fov, 0, 180)) {
			generator.bind();
			if(generator.hasUniform("fov"))
				generator.setUniform("fov", fov[0]);
			generator.unbind();
			camera.getCamera().setFov(fov[0]);
			must_update_rays = true;
		}
		
		ImGui.end();
	}

}
