package org.boby.RayTracing.examples;

import org.boby.RayTracing.main.*;
import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.rendering.Camera;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
//import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class RayTracingExample {

	public static Window window;
	static VFShader renderQuadShader;
	static Object3d renderingQuad;
	static Texture2D tex;
	static Texture2D renderTexture;

	static ComputeShader comp;
	
	static Camera camera;
	
	Input input;
	Time time;
	FramerateManager frm;
	
	public void run() {
		// Configuration.DEBUG.set(true);

		System.out.println("LWJGL version: " + Version.getVersion());

		init();
		loop();

		cleanup();
		

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		window = new Window("nqkva glupost bate", 800, 600, true);
		
		GL.createCapabilities(); // create the opengl context
		
		input = new Input(window);
		time = new Time();
		frm = new FramerateManager(time);
		
		camera = new Camera((float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		// glEnable(GL_BLEND);
		// glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		tex = new Texture2D("./res/rubyblock.png");

		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		
		System.out.println(renderQuadShader.toString());
		
		renderingQuad = new Quad(renderQuadShader);
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());

		comp = new ComputeShader("./res/shaders/compute_shaders/RayTracingShader.comp");

		// TODO: This should really be easier. Maybe make a class that holds everything
		int glbuff = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, glbuff);
		glBufferData(GL_SHADER_STORAGE_BUFFER,
				new float[] { 0.5f, 1.3f, 1.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.3f, 0.2f, 2.0f,
						0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.7f, 0.0f, 0.0f, 0.0f, -0.4f, 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.0f,
						1.0f, 0.5f, 0.0f, 0.0f, 0.0f },
				GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);

		comp.setSSBO("spheres", glbuff);

		System.out.println(comp.toString());
		
		// renderingQuad.setPosition(new Vector3f(0.0f, 0.0f, -1.0f));
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// glfwSwapInterval(1);

		while (!window.shouldClose()) {
			time.updateTime();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			input.update();

			glfwPollEvents();
			
			Vector3f camPos = camera.getPosition();
			if (input.isKeyPressed[GLFW_KEY_SPACE])
				camPos.y += 0.1;
			if (input.isKeyPressed[GLFW_KEY_LEFT_SHIFT])
				camPos.y -= 0.1;
			if (input.isKeyPressed[GLFW_KEY_A])
				camPos.x -= 0.1;
			if (input.isKeyPressed[GLFW_KEY_D])
				camPos.x += 0.1;
			if (input.isKeyPressed[GLFW_KEY_W])
				camPos.z -= 0.1;
			if (input.isKeyPressed[GLFW_KEY_S])
				camPos.z += 0.1;

			if (input.isKeyPressed[GLFW_KEY_1])
				renderTexture.save("./res/image.png");

			if (input.isKeyPressed[GLFW_KEY_9])
				camera.setFov(camera.getFov() + 0.01f);
			if (input.isKeyPressed[GLFW_KEY_0])
				camera.setFov(camera.getFov() - 0.01f);

			
			Vector3f camRot = camera.getRotation();
			camRot.x += input.mouseD.y / 500;
			camRot.y += input.mouseD.x / 500;

			camera.UpdateProjectionMatrix();
			camera.UpdateViewMatrix();
			
			comp.bind();

//TODO: finish this. ... and think of how to do it better;			
			Matrix4f _mat = new Matrix4f().translate(new Vector3f(camera.getPosition()).mul(-1)).rotateX(camera.getRotation().x).rotateY(camera.getRotation().y).rotateZ(camera.getRotation().z);
			comp.setUniform("cameraMatrix", _mat);

			comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
			comp.setUniform("fov", camera.getFov());

			comp.unbind();

			renderTexture.bind(GL_TEXTURE0);
			Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);

			Renderer.draw(renderingQuad, camera);

			tex.bind();

			// System.out.println(Renderer.camPos);

			window.swapBuffers();

			frm.update();

			if (frm.frame_count == 0)
				System.out.println(frm.average_framerate);

		}
	}

	private void cleanup() {
		window.delete();
		renderingQuad.delete();
		renderQuadShader.delete();
		comp.delete();
		tex.delete();
		renderTexture.delete();
	}
	
	public static void main(String[] args) {
		new RayTracingExample().run();
	}

}
