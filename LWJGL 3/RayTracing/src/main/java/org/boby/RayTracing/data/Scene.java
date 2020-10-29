package org.boby.RayTracing.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.boby.RayTracing.data.gameobject.CameraGameObject;
import org.boby.RayTracing.data.gameobject.GameObject;
import org.boby.RayTracing.data.gameobject.LightGameObject;
import org.boby.RayTracing.data.gameobject.MeshedGameObject;
import org.boby.RayTracing.data.light.Light;
import org.boby.RayTracing.rendering.Renderer;
import org.boby.RayTracing.shaders.Shader;
import org.boby.RayTracing.shaders.VFShader;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

public class Scene {

	private ArrayList<GameObject> gameObjects;

	private ArrayList<Material> materials;
	private ArrayList<Shader> shaders; //  TODO: store the default shader so that buffers can be created
	private CameraGameObject activeCamera;
	
	public SceneBuffersHolder buffHolder;
	
	private VFShader defaultShader;
	
	public class SceneBuffersHolder {
		public int materialsBuffer;
		public int materialsBufferSize;
		
		public int lightsBuffer;
		public int lightsBufferSize;
		
		public Scene scene;
		
		public SceneBuffersHolder(Scene scene, VFShader defaultShader) {
			this.scene = scene;
			createBuffers(defaultShader);
		}
		
		public void createBuffers(VFShader defaultShader) {
			materialsBuffer = glGenBuffers();
			lightsBuffer = glGenBuffers();
			
			defaultShader.setSSBO("Materials", materialsBuffer);
			defaultShader.setSSBO("Lights", lightsBuffer);
		}
		
		public void updateBuffersLength() {
			materialsBufferSize = new Material().getSize() * materials.size();;
			/*
			glBindBuffer(GL_ARRAY_BUFFER, materialsBuffer);
			glBufferData(GL_ARRAY_BUFFER, BufferUtils.createByteBuffer(materialsBufferSize), GL_STATIC_DRAW);
			*/
			
			int lightsCount = 0;
			for(GameObject go : gameObjects) {
				if(go instanceof LightGameObject) {
					lightsCount ++;
				}
			}
			
			lightsBufferSize = new Light().getSize() * lightsCount;
			/*
			glBindBuffer(GL_ARRAY_BUFFER, lightsBuffer);
			glBufferData(GL_ARRAY_BUFFER, BufferUtils.createByteBuffer(lightsBufferSize), GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			*/
		}
		
		public void updateBuffers() {
			int offset;
			
			ByteBuffer matBuff = BufferUtils.createByteBuffer(materialsBufferSize);
			offset = 0;
			for(Material m : materials) {
				m.writeToBuffer(matBuff, offset);
				offset += m.getSize();
			}
			glBindBuffer(GL_ARRAY_BUFFER, materialsBuffer);
			glBufferData(GL_ARRAY_BUFFER, matBuff, GL_STATIC_DRAW);
			
			
			ByteBuffer lightBuff = BufferUtils.createByteBuffer(lightsBufferSize);
			offset = 0;
			for(GameObject go : gameObjects) {
				if(go instanceof LightGameObject) {
					LightGameObject l = (LightGameObject) go;
					l.writeToBuffer(lightBuff, offset);
					offset += l.getLight().getSize();
				}
			}
			glBindBuffer(GL_ARRAY_BUFFER, lightsBuffer);
			glBufferData(GL_ARRAY_BUFFER, lightBuff, GL_STATIC_DRAW);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	public Scene(VFShader defaultShader) {
		this.gameObjects = new ArrayList<GameObject>();

		this.materials = new ArrayList<Material>();
		this.shaders = new ArrayList<Shader>();
		this.activeCamera = null;
		
		this.defaultShader = defaultShader;
		this.buffHolder = new SceneBuffersHolder(this, defaultShader);
	}

	private static <T> int registerUniqueField(T obj, ArrayList<T> list) {
		int id = list.indexOf(obj);
		if (id == -1) {
			id = list.size();
			list.add(obj);
		}
		return id;
	}

	private static <T> int registerField(T obj, ArrayList<T> list) {
		int id = list.size();
		list.add(obj);
		return id;
	}

	public int regsiterGameObject(GameObject go) {
		return registerField(go, gameObjects);
	}

	public int registerMaterial(Material material) {
		return registerUniqueField(material, materials);
	}

	public int registerShader(Shader shader) {
		return registerUniqueField(shader, shaders);
	}

	public void setActiveCamera(CameraGameObject cam) {
		this.activeCamera = cam;
	}
	
	public void updateBuffers() {
		buffHolder.updateBuffersLength();
		buffHolder.updateBuffers();
	}
	
	public void draw() {
		activeCamera.update();
		for(GameObject go : gameObjects) {
			if(go instanceof MeshedGameObject) {
				Renderer.draw((MeshedGameObject)go, defaultShader);
			}
		}
	}
}
