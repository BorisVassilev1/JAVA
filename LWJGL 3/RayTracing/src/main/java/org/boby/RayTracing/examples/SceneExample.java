package org.boby.RayTracing.examples;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;

import org.boby.RayTracing.data.*;
import org.boby.RayTracing.data.gameobject.*;
import org.boby.RayTracing.data.light.LightType;
import org.boby.RayTracing.data.mesh.BasicMesh;
import org.boby.RayTracing.main.Window;
import org.boby.RayTracing.shaders.*;
import org.boby.RayTracing.utils.*;
import org.joml.Vector3f;

public class SceneExample extends ApplicationBase{
	Window window;
	CameraGameObject cam;
	
	VFShader shader;
	
	MeshedGameObject cube, dragon, dragon2;
	
	Scene sc;
	
	Time time;
	FramerateManager fm;
	Input input;
	TransformController controller;
	
	public static void main(String[] args) {
		new SceneExample().run();
	}
	
	@Override
	public void init() {
		window = new Window("scene", 1500, 900, false, true);
		
	    cam = new CameraGameObject(new Camera((float) Math.toRadians(70f), window.getWidth() / (float)window.getHeight(), 0.01f, 1000f));
	    
	    shader = new VFShader("./res/shaders/verfrag_shaders/BasicVertexShader.vs", "./res/shaders/verfrag_shaders/BasicFragmentShader.fs");
	    
	    sc = new Scene(shader);
	    sc.setActiveCamera(cam);
	    
	    Material mat1 = new Material(new Vector3f(1.0f, 0.0f, 0.0f));
	    Material mat2 = new Material(new Vector3f(1.0f, 1.0f, 0.0f));	    
	    
	    LightGameObject light1 = new LightGameObject(LightType.POINT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 30.0f);
	    light1.transform.setPosition(new Vector3f(-6f, 4f, 1f));
	    light1.transform.updateWorldMatrix();
	    light1.register(sc);
	    
	    LightGameObject light2 = new LightGameObject(LightType.POINT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 30.0f);
	    light2.transform.setPosition(new Vector3f(4f, 4f, -7f));
	    light2.transform.updateWorldMatrix();
	    light2.register(sc);
	    
	    BasicMesh cubeMesh = ModelLoader.load("./res/cube.obj");
	    cube = new MeshedGameObject(cubeMesh, mat2);
	    cube.transform.setPosition(new Vector3f(3f, 1f, -2f));
	    cube.transform.setScale(.5f);
	    cube.transform.updateWorldMatrix();
	    cube.register(sc);
	    
	    BasicMesh dragonMesh = ModelLoader.load("./res/dragon.obj"); 
	    dragon = new MeshedGameObject(dragonMesh, mat1);
	    dragon.transform.setScale(10);
	    dragon.transform.updateWorldMatrix();
	    dragon.register(sc);
	    
	    dragon2 = new MeshedGameObject(dragonMesh, mat2);
	    dragon2.transform.setScale(10);
	    dragon2.transform.setPosition(new Vector3f(6f, 0f, 0f));
	    dragon2.transform.setRotation(new Vector3f(0f, (float)Math.toRadians(-60), 0f));
	    dragon2.transform.updateWorldMatrix();
	    dragon2.register(sc);
	    
	    time = new Time();
	    
	    input = new Input(window);
	    input.hideMouse();
	    input.lockMouse = true;
	    
		sc.updateBuffers();
		
	    fm = new FramerateManager(time);
	    fm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
	    
	    controller = new TransformController(input, cam.transform);
	    
	    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
	
	@Override
	public void loop() {
		while(!window.shouldClose()) {
			time.updateTime();
			
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
		    
			input.update();
			controller.update();
			
			sc.draw();
			
		    window.swapBuffers();
		    fm.update();
	    }
	}

	@Override
	public void cleanup() {
		dragon.mesh.delete();
		cube.mesh.delete();
		shader.delete();
		
		window.delete();
	}
}
