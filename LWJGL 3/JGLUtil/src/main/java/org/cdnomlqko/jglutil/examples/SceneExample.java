package org.cdnomlqko.jglutil.examples;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.cdnomlqko.jglutil.Input;
import org.cdnomlqko.jglutil.JGLUtil;
import org.cdnomlqko.jglutil.Time;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.data.*;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.LightGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.mesh.BasicMesh;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.cdnomlqko.jglutil.utils.*;
import org.joml.Vector3f;

/**
 * A test.
 * Implements a sample scene with 2 stanford dragons: one red and one yellow, one yellow cube, one ambient light, one directional and one point light that changes color.
 * Controll camera with WASD, Shift and Space. '1' toggles input and visibility of the mouse.
 * @author CDnoMlqko
 */
public class SceneExample extends ApplicationBase{
	Window window;
	CameraGameObject cam;
	
	MeshedGameObject cube, dragon1, dragon2, plane;
	ShadedGameObject arrow;
	LightGameObject light1, light2, light3;
	
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
	    
	    JGLUtil.init();
	    
	    sc = new Scene(null);
	    sc.setActiveCamera(cam);
	    
	    Material mat1 = new Material(new Vector3f(1.0f, 0.0f, 0.0f));
	    Material mat2 = new Material(new Vector3f(1.0f, 1.0f, 0.0f));  
	    Material mat3 = new Material(new Vector3f(1));
	    
	    light1 = new LightGameObject(Light.Type.POINT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 20.0f);
	    light1.transform.setPosition(new Vector3f(-6f, 6f, 1f));
	    light1.transform.setScale(.5f);
	    light1.transform.updateWorldMatrix();
	    light1.register(sc);
	    
	    light2 = new LightGameObject(Light.Type.DIRECTIONAL_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 0.7f);
	    light2.transform.setPosition(new Vector3f(4, 5, 0));
	    light2.transform.setRotation(new Vector3f(1f, -.3f, 0f));
	    light2.transform.setScale(.5f);
	    light2.transform.updateWorldMatrix();
	    light2.register(sc);
	    
	    light3 = new LightGameObject(Light.Type.AMBIENT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 0.1f);
	    light3.transform.setPosition(new Vector3f(2, 5, 0));
	    light3.transform.setScale(.5f);
	    light3.transform.updateWorldMatrix();
	    light3.register(sc);
	    
	    BasicMesh cubeMesh = MeshUtils.makeCube(1, 1, 1);
	    cube = new MeshedGameObject(cubeMesh, mat2, null);
	    cube.transform.setPosition(new Vector3f(3f, 1f, -2f));
	    cube.transform.updateWorldMatrix();
	    cube.register(sc);
	    
	    BasicMesh dragonMesh = ModelLoader.load("./res/dragon.obj"); 
	    dragon1 = new MeshedGameObject(dragonMesh, mat1, null);
	    dragon1.transform.setPosition(new Vector3f(0f, 3f, 0f));
	    dragon1.transform.setScale(10);
	    dragon1.transform.updateWorldMatrix();
	    dragon1.register(sc);
	    
	    dragon2 = new MeshedGameObject(dragonMesh, mat2, null);
	    dragon2.transform.setScale(10);
	    dragon2.transform.setPosition(new Vector3f(6f, 3f, 0f));
	    dragon2.transform.setRotation(new Vector3f(0f, (float)Math.toRadians(-60), 0f));
	    dragon2.transform.updateWorldMatrix();
	    dragon2.register(sc);
	    
	    plane = new MeshedGameObject(MeshUtils.makeQuad(30), mat3, null);
	    plane.transform.setRotation(new Vector3f(-(float)Math.PI / 2, 0, 0));
	    plane.transform.updateWorldMatrix();
	    plane.register(sc);
	    
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
			//sc.bindBuffersToSSBOs();
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
		JGLUtil.delete();
		
		window.delete();
	}
}
