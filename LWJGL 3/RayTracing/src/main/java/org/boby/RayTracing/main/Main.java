package org.boby.RayTracing.main;

import org.boby.RayTracing.objects.Cube;
import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.utils.Texture;
import org.boby.RayTracing.utils.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import shaders.BasicShader;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	// The window handle
	public static Window window;
	static Quad renderingQuad;
	static Object3d obj;
	static Texture tex;

	public void run() {
		//Configuration.DEBUG.set(true);
		System.out.println("LWJGL version: " + Version.getVersion());

		window = new Window(800, 600, "something");
		window.create();
		
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window.getId());
		glfwDestroyWindow(window.getId());
		renderingQuad.destroy();
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GL.createCapabilities();// create the opengl context
		Renderer.init();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		try {
			tex = new Texture("./res/rubyblock.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderingQuad = new Quad();
		obj = new Cube();

		//bs.setUniform("projectionMatrix", Renderer.getMatrix());
	}

	private void loop() {
		

		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			// System.out.println(1/Time.deltaTime);
			glfwPollEvents();
			
			tex.bind();
			Renderer.draw(obj,renderingQuad);
			tex.unbind();
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
