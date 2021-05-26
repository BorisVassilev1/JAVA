package org.cdnomlqko.jglutil.examples.GUITest;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;


import org.cdnomlqko.jglutil.JGLUtil;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.data.Camera;
import org.cdnomlqko.jglutil.data.Light;
import org.cdnomlqko.jglutil.data.Material;
import org.cdnomlqko.jglutil.data.Scene;
import org.cdnomlqko.jglutil.examples.ApplicationBase;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.LightGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.joml.Vector3f;

import imgui.ImGui;

public class GUITest extends ApplicationBase{

	CameraGameObject camera;
	
	float fov = (float) Math.toRadians(70f);
	
	Scene sc;
	
	MeshedGameObject obj;
	
	@Override
	public void init() {
		camera = new CameraGameObject(new Camera(fov,(float) window.getWidth() / (float)window.getHeight(), 0.01f, 1000f));
		camera.transform.setPosition(new Vector3f(0, 0, 2));
		camera.transform.updateWorldMatrix();
		
		JGLUtil.init();
		
		sc = new Scene(null);
		sc.setActiveCamera(camera);
		
		obj = new MeshedGameObject(MeshUtils.makeCube(1.f), new Material(1.0f, 1.0f, 0.0f), null);
		obj.register(sc);
		
		LightGameObject light1 = new LightGameObject(Light.Type.DIRECTIONAL_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 0.7f);
	    light1.transform.setPosition(new Vector3f(0, 1, 0));
	    light1.transform.setRotation(new Vector3f(-1f, 2.7f, 0f));
	    light1.transform.setScale(.5f);
	    light1.transform.updateWorldMatrix();
	    light1.register(sc);
	    
	    LightGameObject light2 = new LightGameObject(Light.Type.AMBIENT_LIGHT, new Vector3f(1.0f, 1.0f, 1.0f), 0.1f);
	    light2.transform.setScale(.5f);
	    light2.transform.updateWorldMatrix();
	    light2.register(sc);
		
		sc.updateBuffers();
	}
	
	@Override
	public void loop() {
			Vector3f rotation = obj.transform.getRotation();
			rotation.add(0.01f, 0.01f, 0.01f);
			obj.transform.updateWorldMatrix();
			
			sc.draw();
	}
	
	@Override
	public void gui() {
		ImGui.begin("asdf");
		
		if(ImGui.button("click me!")) {
			obj.getMaterial().setColor(new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()));
			sc.updateBuffers();
		}
		
		ImGui.end();
	}

	@Override
	public void cleanup() {
		JGLUtil.delete();
	}
	
	public static void main(String[] args) {
		new GUITest().run(new Window("I want to die", 800, 600, true, true));
	}
}
