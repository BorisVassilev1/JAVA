package org.boby.RayTracing.examples;

import org.boby.RayTracing.main.*;
import org.boby.RayTracing.objects.*;
import org.boby.RayTracing.rendering.Camera;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
//import org.joml.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class RayTracingExample {

	public static Window window;
	static VFShader renderQuadShader;
	static Object3d renderingQuad;
	static Texture2D tex;
	static Texture2D renderTexture;

	static ComputeShader comp;
	
	static Camera camera;
	
	Input input;
	Time time;
	FramerateManager frm;
	
	public void run() {
		// Configuration.DEBUG.set(true);

		System.out.println("LWJGL version: " + Version.getVersion());

		init();
		loop();

		cleanup();
		

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		window = new Window("nqkva glupost bate", 800, 600, true);
		
		GL.createCapabilities(); // create the opengl context
		
		
		camera = new Camera((float)Math.toRadians(70f), window.getWidth() / window.getHeight(), 0.01f, 1000f);
		
		tex = new Texture2D("./res/rubyblock.png");

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
		
	}

	private void loop() {
		
		while (!window.shouldClose()) {
			time.updateTime();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			
			input.update();
			
			Vector3f camPos = camera.getPosition();
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

			
			Vector3f camRot = camera.getRotation();
			camRot.x += input.mouseD.y / 500;
			camRot.y += input.mouseD.x / 500;

			camera.UpdateProjectionMatrix();
			camera.UpdateViewMatrix();
			
			comp.bind();
			//TODO: finish this. ... and think of how to do it better;			
			Matrix4f _mat = new Matrix4f().translate(new Vector3f(camera.getPosition()).mul(-1)).rotateX(camera.getRotation().x).rotateY(camera.getRotation().y).rotateZ(camera.getRotation().z);
			comp.setUniform("cameraMatrix", _mat);
			comp.setUniform("resolution", new Vector2f(window.getWidth(), window.getHeight()));
			comp.setUniform("fov", camera.getFov());
			comp.unbind();

			renderTexture.bind(GL_TEXTURE0);
			Renderer.Compute(comp, renderTexture.getWidth(), renderTexture.getHeight(), 1);

			Renderer.draw(renderingQuad, camera);

			tex.bind();

			window.swapBuffers();

			frm.update();
		}
	}

	private void cleanup() {
		window.delete();
		renderingQuad.delete();
		renderQuadShader.delete();
		comp.delete();
		tex.delete();
		renderTexture.delete();
	}
	
	public static void main(String[] args) {
		new RayTracingExample().run();
	}

}
