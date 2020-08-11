package org.boby.RayTracing.examples;

import org.boby.RayTracing.main.Window;
import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.rendering.Camera;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Vector3f;
//import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class CubeExample {

	// The window handle
	public static Window window;
	Texture2D tex;

	Object3d cube;
	VFShader cubeShader;

	Input input;
	Time time;
	FramerateManager frm;
	

	Camera camera;
	
	public void run() {
//		Configuration.DEBUG.set(true);

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
		
		time = new Time();
		frm = new FramerateManager(time);
		
		camera = new Camera( (float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		
		input = new Input(window);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		// glEnable(GL_BLEND);
		// glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		tex = new Texture2D("./res/rubyblock.png");

		cubeShader = new VFShader("./res/shaders/verfrag_shaders/BasicVertexShader.vs",
				"./res/shaders/verfrag_shaders/BasicFragmentShader.fs");
		System.out.println(cubeShader.toString());
		
		cube = new Cube(cubeShader);
		cube.setScale(0.5f);
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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

			if (input.isKeyPressed[GLFW_KEY_9])
				camera.setFov(camera.getFov() + 0.01f);
			if (input.isKeyPressed[GLFW_KEY_0])
				camera.setFov(camera.getFov() - 0.01f);

			Vector3f camRot = camera.getRotation();

			camRot.x += input.mouseD.y / 500;
			camRot.y += input.mouseD.x / 500;

			if(window.isResized()) {
				glViewport(0, 0, window.getWidth(), window.getHeight());
				camera.setAspect(window.getWidth() / (float)window.getHeight());
				window.setResized(false);
			}
			
			camera.UpdateProjectionMatrix();
			camera.UpdateViewMatrix();
			
			tex.bind();
			Renderer.draw(cube, camera);

			// cube.setRotation(cube.getRotation().add(new Vector3f(0.001f, 0.001f,
			// 0.001f)));

			// System.out.println(Renderer.camPos);

			window.swapBuffers();

			frm.update();

			if (frm.frame_count == 0)
				System.out.println(frm.average_framerate);

		}
	}

	private void cleanup() {
		window.delete();
		cube.delete();
		cubeShader.delete();
		tex.delete();
	}

	public static void main(String[] args) {
		new CubeExample().run();
	}

}
