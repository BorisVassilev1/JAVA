package org.boby.RayTracing.main;

import org.boby.RayTracing.utils.Texture;
import org.boby.RayTracing.utils.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import objects.Object3d;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

	// The window handle
	private Window window;
	Object3d obj;
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		window = new Window(800, 600, "lmao tva raboti");
		window.create();
		
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window.getId());
		glfwDestroyWindow(window.getId());
		obj.destroy();
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	

	private void loop() {
		GL.createCapabilities();// create the opengl context
		
		glEnable(GL_TEXTURE_2D);

		glClearColor(1, 0, 0, 0);
		Texture tex = new Texture("./res/rubyblock.png");
		obj = new Object3d();
		
		
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			//System.out.println(1/Time.deltaTime);
			
			glfwPollEvents();
			
//			tex.bind();
//			glBegin(GL_QUADS);
//			{
//				glTexCoord2f(0, 0);
//				glVertex2f(-0.5f, -0.5f);
//				
//				glTexCoord2f(0, 1);
//				glVertex2f(-0.5f, 0.5f);
//				
//				glTexCoord2f(1, 1);
//				glVertex2f(0.5f, 0.5f);
//				
//				glTexCoord2f(1, 0);
//				glVertex2f(0.5f, -0.5f);
//			}
//			glEnd();
			
			obj.draw();
			
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
