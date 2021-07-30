package org.cdnomlqko.jglutil.examples;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import org.cdnomlqko.jglutil.*;
import org.cdnomlqko.jglutil.data.Camera;
import org.cdnomlqko.jglutil.data.Transformation;
import org.cdnomlqko.jglutil.data.textures.Texture2D;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.shader.*;
import org.cdnomlqko.jglutil.utils.*;

/**
 * A test Implements a simple ray traced scene with three reflecting and
 * colorful spheres. Controll with the WASD, Space and Shift keys. '2' takes a
 * screenshot, '9' and '0' change the field of the view of the camera. '1'
 * toggles input and visibility of the mouse
 * 
 * @author CDnoMlqko
 *
 */
public class RayTracingExample extends ApplicationBase {

	static VFShader renderQuadShader;
	static ShadedGameObject renderingQuad;
	static Texture2D renderTexture;

	static ComputeShader comp;

	static Camera camera;
	static Transformation cameraTransform;

	TransformController controller;

	@Override
	public void init() {
		// create a camera, initialize shaders, objects
		camera = new Camera((float) Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		cameraTransform = new Transformation();

		renderQuadShader = new VFShader("./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs",
				"./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs");

		renderingQuad = new ShadedGameObject(MeshUtils.makeQuad(2f), renderQuadShader);

		renderTexture = new Texture2D(window.getWidth(), window.getHeight());

		comp = new ComputeShader("./res/shaders/compute_shaders/RayTracingShader.comp");

		// pass data for sphere properties to the compute shader
		int glbuff = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, glbuff);
		glBufferData(GL_SHADER_STORAGE_BUFFER,
				new float[] { 0.5f, 1.3f, 1.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.3f, 0.2f, 2.0f,
						0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.7f, 1.0f, 0.0f, 0.0f, -0.4f, 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.0f,
						1.0f, 0.5f, 2.0f, 0.0f, 0.0f },
				GL_DYNAMIC_DRAW);
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);

		comp.setSSBO("spheres", glbuff);
		comp.bind();
		comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
		comp.setUniform("fov", camera.getFov());
		comp.unbind();

		controller = new TransformController(input, cameraTransform);
		
		input.lockMouse = true;
		input.hideMouse();
	}

	@Override
	public void loop() {
		controller.update();

		// handle key presses
		if (input.getKey(GLFW_KEY_2) == GLFW_PRESS)
			renderTexture.save("./res/images/image.png");

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
		// draw the quad on the screen. it will use texture0.
		Renderer.draw(renderingQuad);
	}

	@Override
	public void cleanup() {
		// delete everything
		renderingQuad.getMesh().delete();
		renderQuadShader.delete();
		comp.delete();
		renderTexture.delete();
	}

	// the main method will start the application
	public static void main(String[] args) {
		new RayTracingExample().run(new Window("nqkva glupost bate", 800, 600, false, true));
	}

	@Override
	public void gui() {

	}

}
