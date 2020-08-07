package org.boby.RayTracing.main;

import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Main {

	// The window handle
	public static Window window;
	static VFShader renderQuadShader;
	static Object3d renderingQuad;
	static Texture2D tex;
	static Texture2D renderTexture;
	
	static ComputeShader comp;
	
	static Object3d cube;
	static VFShader cubeShader;
	
	public void run() {
//		Configuration.DEBUG.set(true);
		
		System.out.println("LWJGL version: " + Version.getVersion());

		window = new Window(800, 600, "nqkva glupost bate");
		window.create();
		
		init();
		loop();

		
		window.delete();
		
		renderingQuad.delete();
		
		cube.delete();
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GL.createCapabilities(); // create the opengl context
		Renderer.init();
		
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		tex = new Texture2D("./res/rubyblock.png");
		
		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs", "./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		System.out.println(renderQuadShader.toString());
		renderingQuad = new Quad(renderQuadShader);
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());

		comp = new ComputeShader("./res/shaders/compute_shaders/RayTracingShader.comp");
		
		
		// TODO: This should really be easier. Maybe make a class that holds everything
		float[] buff = {
				 0.5f, 1.3f, 1.5f, 0.0f,	 1.0f, 0.0f, 0.0f, 1.0f,	 0.5f,0.0f,0.0f,0.0f,
				 1.3f, 0.2f, 2.0f, 0.0f,	 0.0f, 1.0f, 0.0f, 1.0f,	 0.7f,0.0f,0.0f,0.0f,
				-0.4f, 0.5f, 1.5f, 0.0f,	 0.0f, 0.0f, 1.0f, 1.0f,	 0.5f,0.0f,0.0f,0.0f
				};
		int glbuff = glGenBuffers();

		glBindBuffer(GL_SHADER_STORAGE_BUFFER, glbuff);
		glBufferData(GL_SHADER_STORAGE_BUFFER, buff, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		
		comp.setSSBO("spheres", glbuff);
		
		
		System.out.println(comp.toString());
		
		cubeShader = new VFShader("./res/shaders/verfrag_shaders/BasicVertexShader.vs", "./res/shaders/verfrag_shaders/BasicFragmentShader.fs");
		System.out.println(cubeShader.toString());
		cube = new Cube(cubeShader);
		cube.setScale(0.5f);
		
		renderingQuad.setPosition(new Vector3f(0.0f, 0.0f, -1.0f));
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
		Input.update();
		
    	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Time.updateTime();
			Input.update();
			
			glfwPollEvents();
			
			if(Input.isKeyPressed[GLFW_KEY_SPACE]) Renderer.camPos.y += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_LEFT_SHIFT]) Renderer.camPos.y -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_A]) Renderer.camPos.x -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_D]) Renderer.camPos.x += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_W]) Renderer.camPos.z -= 0.1;
			if(Input.isKeyPressed[GLFW_KEY_S]) Renderer.camPos.z += 0.1;
			if(Input.isKeyPressed[GLFW_KEY_1]) {
				renderTexture.save("./res/image.png");
			}
//			if(Input.isKeyPressed[GLFW_KEY_2]) {
//				comp.create();
//			}
			
			if(Input.isKeyPressed[GLFW_KEY_9]) Renderer.FOV += -0.01f;
			if(Input.isKeyPressed[GLFW_KEY_0]) Renderer.FOV -= -0.01f;
			
			Renderer.camRot.x += Input.mouseD.y / 500;
			Renderer.camRot.y += Input.mouseD.x / 500;
			
			comp.bind();
			
	    	Matrix4f _mat = Renderer.transform.getWorldMatrix(new Vector3f(Renderer.camPos).mul(-1), Renderer.camRot , 1.0f);
	    	comp.setUniform("cameraMatrix", _mat);
	    	
	    	comp.setUniform("resolution", new Vector2f(Main.window.getWidth(), Main.window.getHeight()));
	    	comp.setUniform("fov", Renderer.FOV);
	    	
	    	comp.unbind();
	    	
	    	renderTexture.bind(GL_TEXTURE0);
	    	Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);
			
			
			Renderer.draw(renderingQuad);
			
			
			tex.bind();
			Renderer.draw(cube);
			
//			cube.setRotation(cube.getRotation().add(new Vector3f(0.001f, 0.001f, 0.001f)));
			
			//System.out.println(Renderer.camPos);
			
			
			window.swapBuffers();
		}
	}

	public static void main(String[] args) {
		new Main().run();
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		new LwjglApplication(new TestClass(), config);
	}

}
