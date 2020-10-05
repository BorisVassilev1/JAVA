package org.boby.RayTracing.examples;

import org.boby.RayTracing.main.*;
import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.rendering.Camera;
import org.boby.RayTracing.rendering.CameraController;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

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
	
	Input input;
	Time time;
	FramerateManager frm;
	
	CameraController controller;
	
	@Override
	public void init() {
		window = new Window("nqkva glupost bate", 800, 600, false, true);
		
		camera = new Camera((float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		//camera.UpdateMatricesUBO();
		
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
		
		ShaderParser.findBlockUniforms(comp.getProgramId());
		
		System.out.println();
		ShaderParser.getNonBlockUniforms(comp.getProgramId());
		ShaderParser.getSSBOs(comp.getProgramId());
		
		time = new Time();
		frm = new FramerateManager(time);
		
		frm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
			
		controller = new CameraController(input, camera);
		
	}

	@Override
	public void loop() {
		
		while (!window.shouldClose()) {
			time.updateTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			
			input.update();
			controller.update();
			
			Vector3f camPos = camera.transform.getPosition();
			if(input.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
				camPos.y += 0.1;
			}
			if (input.getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
				camPos.y -= 0.1;
			if (input.getKey(GLFW_KEY_A) == GLFW_PRESS)
				camPos.x -= 0.1;
			if (input.getKey(GLFW_KEY_D) == GLFW_PRESS)
				camPos.x += 0.1;
			if (input.getKey(GLFW_KEY_W) == GLFW_PRESS)
				camPos.z -= 0.1;
			if (input.getKey(GLFW_KEY_S) == GLFW_PRESS)
				camPos.z += 0.1;

			if (input.getKey(GLFW_KEY_1) == GLFW_PRESS)
				renderTexture.save("./res/image.png");

			
			if (input.getKey(GLFW_KEY_9) == GLFW_PRESS)
				camera.setFov(camera.getFov() + 0.01f);
			if (input.getKey(GLFW_KEY_0) == GLFW_PRESS)
				camera.setFov(camera.getFov() - 0.01f);

			
			Vector3f camRot = camera.transform.getRotation();
			camRot.x += input.mouseD.y / 500;
			camRot.y += input.mouseD.x / 500;
			
			camera.updateMatrices();
			
			comp.bind();
			//TODO: finish this. ... and think of how to do it better;			
			Matrix4f _mat = new Matrix4f().translate(new Vector3f(camera.transform.getPosition()).mul(-1)).rotateX(camera.transform.getRotation().x).rotateY(camera.transform.getRotation().y).rotateZ(camera.transform.getRotation().z);
			comp.setUniform("cameraMatrix", camera.transform.getWorldMatrix());
			comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
			comp.setUniform("fov", camera.getFov());
			comp.unbind();
			
			renderTexture.bind(GL_TEXTURE0);
			Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);

			Renderer.draw(renderingQuad, camera);

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
