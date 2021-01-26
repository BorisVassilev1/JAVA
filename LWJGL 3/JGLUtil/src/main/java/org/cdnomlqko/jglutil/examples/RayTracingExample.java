package org.cdnomlqko.jglutil.examples;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import org.cdnomlqko.jglutil.*;
import org.cdnomlqko.jglutil.data.Camera;
import org.cdnomlqko.jglutil.data.Texture2D;
import org.cdnomlqko.jglutil.data.Transformation;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.*;
import org.cdnomlqko.jglutil.utils.*;


/**
 * A test
 * Implements a simple ray traced scene with three reflecting and colorful spheres.
 * Controll with the WASD, Space and Shift keys. '2' takes a screenshot, '9' and '0' change the field of the view of the camera. '1' toggles input and visibility of the mouse
 * @author CDnoMlqko
 *
 */
public class RayTracingExample extends ApplicationBase{

	public static Window window;
	static VFShader renderQuadShader;
	static ShadedGameObject renderingQuad;
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
		//create a window, camera, initialize shaders, objects
		window = new Window("nqkva glupost bate", 800, 600, false, true);
		
		camera = new Camera((float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		cameraTransform = new Transformation();
		
		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");
		
		renderingQuad = new ShadedGameObject(MeshUtils.makeQuad(2f), renderQuadShader);
		
		renderTexture = new Texture2D(window.getWidth(), window.getHeight());

		comp = new ComputeShader("./res/shaders/compute_shaders/RayTracingShader.comp");
		
		// pass data for sphere properties to the compute shader
		int glbuff = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, glbuff);
		glBufferData(GL_SHADER_STORAGE_BUFFER, new float[] { 
						0.5f, 1.3f, 1.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f,
						1.3f, 0.2f, 2.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.7f, 1.0f, 0.0f, 0.0f,
						-0.4f, 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, 2.0f, 0.0f, 0.0f
						}, GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);

		comp.setSSBO("spheres", glbuff);
		comp.bind();
		comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
		comp.setUniform("fov", camera.getFov());
		comp.unbind();
		
		//init input, time, framerate manager, controller
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
		// the game loop
		while (!window.shouldClose()) {
			time.updateTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			
			input.update();
			controller.update();

			// handle key presses
			if (input.getKey(GLFW_KEY_2) == GLFW_PRESS)
				renderTexture.save("./res/image.png");
			
			if (input.getKey(GLFW_KEY_9) == GLFW_PRESS)
				camera.setFov(camera.getFov() + 0.01f);
			if (input.getKey(GLFW_KEY_0) == GLFW_PRESS)
				camera.setFov(camera.getFov() - 0.01f);
			// change in FOV must be updated
			camera.UpdateProjectionMatrix();
			
			// the shader will write to texture0. 
			renderTexture.bind(GL_TEXTURE0);
			renderTexture.bindImage(0);
			Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1, () -> {
				comp.setUniform("cameraMatrix", cameraTransform.getWorldMatrix());
				comp.setUniform("fov", camera.getFov());
			});
			//draw the quad on the screen. it will use texture0.
			Renderer.draw(renderingQuad);

			window.swapBuffers();
			
			frm.update();
		}
	}

	@Override
	public void cleanup() {
		//delete everything
		window.delete();
		renderingQuad.getMesh().delete();
		renderQuadShader.delete();
		comp.delete();
		renderTexture.delete();
	}
	
	// the main method will start the application
	public static void main(String[] args) {
		new RayTracingExample().run();
	}

}
