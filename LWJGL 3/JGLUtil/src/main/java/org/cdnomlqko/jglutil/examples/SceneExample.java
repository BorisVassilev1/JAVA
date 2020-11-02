package org.cdnomlqko.jglutil.examples;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.cdnomlqko.jglutil.Input;
import org.cdnomlqko.jglutil.Time;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.data.*;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.LightGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.mesh.BasicMesh;
import org.cdnomlqko.jglutil.shader.*;
import org.cdnomlqko.jglutil.utils.*;
import org.joml.Vector3f;

/**
 * A test.
 * Implements a sample scene with 2 stanford dragons: one red and one yellow, one yellow cube, one unchanging light and another that changes color.
 * Controll camera with WASD, Shift and Space. '1' toggles input and visibility of the mouse.
 * @author CDnoMlqko
 */
public class SceneExample extends ApplicationBase{
	Window window;
	CameraGameObject cam;
	
	VFShader shader;
	
	MeshedGameObject cube, dragon1, dragon2;
	LightGameObject light1, light2;
	
	Scene sc;
	
	Time time;
	FramerateManager fm;
	Input input;
	TransformController controller;
	
	// the main methods starts the application
	public static void main(String[] args) {
		new SceneExample().run();
	}
	
	@Override
	public void init() {
		// create everything
		window = new Window("scene", 1500, 900, false, true);
		
	    cam = new CameraGameObject(new Camera((float) Math.toRadians(70f), window.getWidth() / (float)window.getHeight(), 0.01f, 1000f));
	    
	    shader = new VFShader("./res/shaders/verfrag_shaders/BasicVertexShader.vs", "./res/shaders/verfrag_shaders/BasicFragmentShader.fs");
	    
	    sc = new Scene(shader);
	    sc.setActiveCamera(cam);
	    
	    Material mat1 = new Material(new Vector3f(1.0f, 0.0f, 0.0f));
	    Material mat2 = new Material(new Vector3f(1.0f, 1.0f, 0.0f));	    
	    
	    light1 = new LightGameObject(Light.Type.POINT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 30.0f);
	    light1.transform.setPosition(new Vector3f(-6f, 4f, 1f));
	    light1.transform.updateWorldMatrix();
	    light1.register(sc);
	    
	    light2 = new LightGameObject(Light.Type.POINT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 30.0f);
	    light2.transform.setPosition(new Vector3f(4f, 4f, -7f));
	    light2.transform.updateWorldMatrix();
	    light2.register(sc);
	    
	    BasicMesh cubeMesh = ModelLoader.load("./res/cube.obj");
	    cube = new MeshedGameObject(cubeMesh, mat2, null);
	    cube.transform.setPosition(new Vector3f(3f, 1f, -2f));
	    cube.transform.setScale(.5f);
	    cube.transform.updateWorldMatrix();
	    cube.register(sc);
	    
	    BasicMesh dragonMesh = ModelLoader.load("./res/dragon.obj"); 
	    dragon1 = new MeshedGameObject(dragonMesh, mat1, null);
	    dragon1.transform.setScale(10);
	    dragon1.transform.updateWorldMatrix();
	    dragon1.register(sc);
	    
	    dragon2 = new MeshedGameObject(dragonMesh, mat2, null);
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
	    // change GL_FILL to GL_LINE for wirefame rendering
	    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
	
	@Override
	public void loop() {
		// the game loop
		while(!window.shouldClose()) {
			time.updateTime();
			
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
		    
			input.update();
			controller.update();
			
			float k = (float)(glfwGetTime() / 4 % 1);
			// change the color of the light and update it
			light1.getLight().setColor(ColorConverter.hsvToRgb(new Vector3f(k, 1, 1)));
			light1.update();
			
			// draw the entire scene
			sc.draw();
			
		    window.swapBuffers();
		    fm.update();
	    }
	}

	@Override
	public void cleanup() {
		// delete everything
		dragon1.getMesh().delete();
		cube.getMesh().delete();
		shader.delete();
		
		window.delete();
	}
}
