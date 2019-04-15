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
import shaders.ComputeShader;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;

public class Main {

	// The window handle
	public static Window window;
	static Quad renderingQuad;
	static Texture tex;
	
	static ComputeShader comp = new ComputeShader("./res/shaders/RayTracingShader.comp") {
		
		@Override
		protected void createUniforms() {
			// TODO Auto-generated method stub
			try {
				super.createUniform("img_output");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		protected void bindAllAttributes() {
			// TODO Auto-generated method stub
			
		}
	};

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
		comp.create();
	}

	private void loop() {
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 0));
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 1));
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_COUNT, 2));
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 0));
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 1));
		System.out.println(GL46.glGetIntegeri(GL46.GL_MAX_COMPUTE_WORK_GROUP_SIZE, 2));
		System.out.println(GL46.glGetInteger(GL46.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS));
		
		int tex_w = 512, tex_h = 512;
    	int tex_output;
    	tex_output = glGenTextures();
    	glActiveTexture(GL_TEXTURE0);
    	glBindTexture(GL_TEXTURE_2D, tex_output);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, tex_w, tex_h, 0, GL_RGBA, GL_FLOAT,(ByteBuffer) null);
    	glBindImageTexture(0, tex_output, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
		
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			// System.out.println(1/Time.deltaTime);
			glfwPollEvents();
			comp.bind();
			
			GL46.glActiveTexture(GL46.GL_TEXTURE0);
			GL46.glBindTexture(GL46.GL_TEXTURE_2D, tex_output);
			
			GL46.glDispatchCompute(tex_w, tex_h, 1);
			GL46.glMemoryBarrier(GL46.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
			//tex.bind();
			GL46.glBindTexture(GL46.GL_TEXTURE_2D, tex_output);
			//GL46.glBindTexture(GL46.GL_TEXTURE_2D, tex.getID());
			Renderer.draw(renderingQuad);
			//tex.unbind();
			
			
			//Renderer.Compute(comp);
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
