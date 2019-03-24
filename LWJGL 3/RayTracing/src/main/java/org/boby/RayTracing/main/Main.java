package org.boby.RayTracing.main;

import org.boby.RayTracing.utils.Texture;
import org.boby.RayTracing.utils.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import objects.Object3d;
import objects.Quad;
import shaders.BasicShader;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

	// The window handle
	private Window window;
	Quad obj;
	BasicShader bs;
	Texture tex;

	public void run() {
		//Configuration.DEBUG.set(true);
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		window = new Window(800, 600, "something");
		window.create();
		
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window.getId());
		glfwDestroyWindow(window.getId());
		obj.destroy();
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GL.createCapabilities();// create the opengl context
		
		glEnable(GL_TEXTURE_2D);
		tex = new Texture("./res/rubyblock.png");
		obj = new Quad();
		bs = new BasicShader();
		bs.create();
	}

	private void loop() {
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			// System.out.println(1/Time.deltaTime);
			glfwPollEvents();

			bs.bind();
			tex.bind();
			obj.draw();
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
