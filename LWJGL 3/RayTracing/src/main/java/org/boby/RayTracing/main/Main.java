package org.boby.RayTracing.main;

import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.shaders.ComputeShader;
import org.boby.RayTracing.shaders.RayMarchingComputeShader;
import org.boby.RayTracing.shaders.RayTracingComputeShader;
import org.boby.RayTracing.shaders.TextureOnScreenShader;
import org.boby.RayTracing.utils.Input;
import org.boby.RayTracing.utils.Texture2D;
import org.boby.RayTracing.utils.Time;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.function.Function;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL46.*;

public class Main {

	// The window handle
	public static Window window;
	static TextureOnScreenShader renderQuadShader;
	static Quad renderingQuad;
	static Texture2D tex;
	static Texture2D renderTexture;
	
	static ComputeShader comp = new RayTracingComputeShader();

	public void run() {
		//Configuration.DEBUG.set(true);
		System.out.println("LWJGL version: " + Version.getVersion());

		window = new Window(800, 600, "nqkva glupost bate");
		window.create();
		
		init();
		loop();

		
		window.delete();
		
		renderingQuad.delete();
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GL.createCapabilities();// create the opengl context
		Renderer.init();
		
		//glEnable(GL_TEXTURE_2D);
		//glEnable(GL_DEPTH_TEST);
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		tex = new Texture2D("./res/rubyblock.png");
		
		renderQuadShader = new TextureOnScreenShader();
		renderQuadShader.create();
		renderingQuad = new Quad(renderQuadShader);
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());
		comp.create();
	}

	private void loop() {
		renderTexture.bind(GL_TEXTURE0);
    	
		comp.bind();
		
    	comp.setUniform("resolution", new Vector2f(Main.window.getWidth(), Main.window.getHeight()));
    	
    	Matrix4f mat = Renderer.transform.getWorldMatrix(Renderer.camPos, Renderer.camRot , 1.0f);
    	comp.setUniform("cameraMatrix", mat);
    	
    	comp.setUniform("fov", Renderer.FOV);
    	
    	comp.unbind();
    	
		Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);
		
    	glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			Input.update();
			
			glfwPollEvents();
			
			if(Input.isKeyPressed[GLFW_KEY_UP]) Renderer.camPos.y += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_DOWN]) Renderer.camPos.y -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_LEFT]) Renderer.camPos.x -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_RIGHT]) Renderer.camPos.x += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_W]) Renderer.camPos.z += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_S]) Renderer.camPos.z -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_1]) {
				renderTexture.save("./res/image");
			}
			
			Renderer.camRot.x += Input.mouseD.y / 5;
			Renderer.camRot.y -= Input.mouseD.x / 5;
			
			comp.bind();
			
	    	Matrix4f _mat = Renderer.transform.getWorldMatrix(Renderer.camPos, Renderer.camRot , 1.0f);
	    	comp.setUniform("cameraMatrix", _mat);
	    	
	    	comp.unbind();
	    	
	    	renderTexture.bind(GL_TEXTURE0);
	    	Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);
			
			
			Renderer.draw(renderingQuad);
			
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

}
