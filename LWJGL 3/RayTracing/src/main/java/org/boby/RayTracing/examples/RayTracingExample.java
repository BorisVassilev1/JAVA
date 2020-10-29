package org.boby.RayTracing.examples;

import org.boby.RayTracing.main.*;
import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.data.Camera;
import org.boby.RayTracing.data.Transformation;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

// TODO: this throws some opengl errors every frame. Fix it!

public class RayTracingExample extends ApplicationBase{

	public static Window window;
	static VFShader renderQuadShader;
	static Object3d renderingQuad;
	static Texture2D renderTexture;

	static ComputeShader comp;
	
	static Camera camera;
	static Transformation cameraTransform;
	
	Input input;
	Time time;
	FramerateManager frm;
	
	TransformController controller;
	
	@Override
	public void init() {
		window = new Window("nqkva glupost bate", 800, 600, false, true);
		
		camera = new Camera((float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		cameraTransform = new Transformation();
		
		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		
		System.out.println(renderQuadShader.toString());
		
		renderingQuad = new Quad(renderQuadShader);
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());

		comp = new ComputeShader("./res/shaders/compute_shaders/RayTracingShader.comp");

		// TODO: This should really be easier. Maybe make a class that holds everything
		int glbuff = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, glbuff);
		glBufferData(GL_SHADER_STORAGE_BUFFER, new float[] { 
						0.5f, 1.3f, 1.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f,
						1.3f, 0.2f, 2.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.7f, 0.0f, 0.0f, 0.0f,
						-0.4f, 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f
						}, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);

		comp.setSSBO("spheres", glbuff);

		System.out.println(comp.toString());
		
		input = new Input(window);
		input.lockMouse = true;
		input.hideMouse();
		
		time = new Time();
		frm = new FramerateManager(time);
		
		frm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
			
		controller = new TransformController(input, cameraTransform);
	}

	@Override
	public void loop() {
		
		while (!window.shouldClose()) {
			time.updateTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			
			input.update();
			controller.update();

			if (input.getKey(GLFW_KEY_2) == GLFW_PRESS)
				renderTexture.save("./res/image.png");

			
			if (input.getKey(GLFW_KEY_9) == GLFW_PRESS)
				camera.setFov(camera.getFov() + 0.01f);
			if (input.getKey(GLFW_KEY_0) == GLFW_PRESS)
				camera.setFov(camera.getFov() - 0.01f);
			
			camera.UpdateProjectionMatrix();
			
			comp.bind();
			//TODO: finish this. ... and think of how to do it better;			
			comp.setUniform("cameraMatrix", cameraTransform.getWorldMatrix());
			comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
			comp.setUniform("fov", camera.getFov());
			comp.unbind();
			
			renderTexture.bind(GL_TEXTURE0);
			Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);

			Renderer.draw(renderingQuad);

			window.swapBuffers();

			frm.update();
		}
	}

	@Override
	public void cleanup() {
		window.delete();
		renderingQuad.delete();
		renderQuadShader.delete();
		comp.delete();
		renderTexture.delete();
	}
	
	public static void main(String[] args) {
		new RayTracingExample().run();
	}

}
