package org.boby.RayTracing.examples;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import java.lang.instrument.Instrumentation;
import java.nio.FloatBuffer;

import org.boby.RayTracing.main.Window;
import org.boby.RayTracing.mesh.BasicMesh;
import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.rendering.Camera;
import org.boby.RayTracing.rendering.CameraController;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.Shader;
import org.boby.RayTracing.shaders.VFShader;
import org.boby.RayTracing.utils.FramerateManager;
import org.boby.RayTracing.utils.Input;
import org.boby.RayTracing.utils.ModelLoader;
import org.boby.RayTracing.utils.Time;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class AssimpTest extends ApplicationBase{
	Window window;
	Camera cam;
	Vector3f lightPos = new Vector3f(-6f, 4f, 1f);
	VFShader shader;
	
	Object3d light, cube, dragon;
	
	Time time;
	FramerateManager fm;
	Input input;
	CameraController controller;
	
	public static void main(String[] args) {
		new AssimpTest().run();
	}
	
	public static double getAllocatedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (double) (1024 * 1024);
	}

	@Override
	public void init() {
		window = new Window("assimp test", 1500, 900, false, true);
	    GL.createCapabilities();
	    cam = new Camera((float) Math.toRadians(70f), window.getWidth() / (float)window.getHeight(), 0.01f, 1000f);
	    
	    shader = new VFShader("./res/shaders/verfrag_shaders/BasicVertexShader.vs", "./res/shaders/verfrag_shaders/BasicFragmentShader.fs");
	    shader.bind();
	    
	    if(shader.hasUniform("textureWeight"))
	    shader.setUniform("textureWeight", 0.0f);
	    if(shader.hasUniform("colorWeight"))
	    shader.setUniform("colorWeight", 1.0f);
	    if(shader.hasUniform("lightPosition"))
	    shader.setUniform("lightPosition", lightPos);
	    shader.unbind();
	    
	    shader.createUBO("Matrices", 0);
	    
	    cam.CreateMatricesUBO();
	    
	    
	    BasicMesh lightMesh = ModelLoader.load("./res/cube.obj");
	    light = new Object3d(lightMesh, shader);
	    light.setPosition(lightPos);
	    light.setScale(.3f);
	    light.updateWorldMatrix();
	    
	    BasicMesh cubeMesh = ModelLoader.load("./res/cube.obj");
	    cube = new Object3d(cubeMesh, shader);
	    cube.setPosition(new Vector3f(0f, 4f, 0f));
	    cube.setScale(.5f);
	    cube.updateWorldMatrix();
	    
	    BasicMesh dragonMesh = ModelLoader.load("./res/dragon.obj"); 
	    dragon = new Object3d(dragonMesh, shader);
	    dragon.setScale(10);
	    dragon.updateWorldMatrix();
	    
	    time = new Time();
	    
	    input = new Input(window);
	    input.hideMouse();
	    input.lockMouse = true;
	    
	    fm = new FramerateManager(time);
	    fm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
	    
	    controller = new CameraController(input, cam);
	    
	    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	    
	    
	}
	
	int uboMatrices;
	
	@Override
	public void loop() {
		while(!window.shouldClose()) {
			time.updateTime();
			
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
		    
			input.update();
			controller.update();
			
//			Matrix4f mat = new Matrix4f().rotateY(0.01f);
//			lightPos.mulDirection(mat);
			
			
			//lightPos.x = (float) (Math.sin(glfwGetTime()) * 7);
			shader.bind();
			if(shader.hasUniform("lightPosition"));
			shader.setUniform("lightPosition", lightPos);
			shader.unbind();
			light.setPosition(lightPos);
			light.updateWorldMatrix();
			
			cam.UpdateMatricesUBO();
			
//			System.out.println(cam.getPosition());
//			System.out.println(cam.getViewMatrix().invert().toString());
			
		    Renderer.draw(dragon, cam);
		    Renderer.draw(light, cam);
		    Renderer.draw(cube, cam);
		    
		    window.swapBuffers();
		    fm.update();
	    }
	}

	@Override
	public void cleanup() {
		dragon.delete();
		cube.delete();
		light.delete();
		shader.delete();
		window.delete();
	}
	
}
