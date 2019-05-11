package org.boby.RayTracing.main;

import org.boby.RayTracing.objects.Cube;
import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.objects.RenderingQuad;
import org.boby.RayTracing.utils.Texture;
import org.boby.RayTracing.utils.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import shaders.BasicShader;
import shaders.ComputeShader;
import shaders.RayMarchingComputeShader;
import shaders.RayTracingComputeShader;
import shaders.TextureOnScreenShader;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {

	// The window handle
	public static Window window;
	static RenderingQuad renderingQuad;
	static Texture tex;
	static Texture renderTexture;
	
	static ComputeShader comp = new RayTracingComputeShader();

	public void run() {
		//Configuration.DEBUG.set(true);
		System.out.println("LWJGL version: " + Version.getVersion());

		window = new Window(1000, 600, "something");
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
		renderingQuad = new RenderingQuad();
		renderTexture = new Texture(window.getWidth(), window.getHeight());
		comp.create();
	}

	private void loop() {
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0));
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1));
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2));
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0));
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1));
//		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2));
//		System.out.println(GL46.glGetInteger(GL46.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS));
    	
		Renderer.Compute(comp, renderTexture);
    	
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			// System.out.println(1/Time.deltaTime);
			glfwPollEvents();
			
			Renderer.Compute(comp, renderTexture);
			
			Renderer.draw(renderingQuad);
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
