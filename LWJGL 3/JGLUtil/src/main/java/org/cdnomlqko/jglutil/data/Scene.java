package org.cdnomlqko.jglutil.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.cdnomlqko.jglutil.Renderer;
import org.cdnomlqko.jglutil.gameobject.CameraGameObject;
import org.cdnomlqko.jglutil.gameobject.GameObject;
import org.cdnomlqko.jglutil.gameobject.LightGameObject;
import org.cdnomlqko.jglutil.gameobject.MeshedGameObject;
import org.cdnomlqko.jglutil.gameobject.ShadedGameObject;
import org.cdnomlqko.jglutil.shader.Shader;
import org.cdnomlqko.jglutil.shader.ShaderUtils;
import org.cdnomlqko.jglutil.shader.VFShader;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

public class Scene {

	private ArrayList<GameObject> gameObjects;

	private ArrayList<Material> materials;
	//private ArrayList<Shader> shaders; //  TODO: store the default shader so that buffers can be created
	private CameraGameObject activeCamera;
	
	private SceneBuffersHolder buffHolder;

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
			
			bindBuffersToSSBOs();
		}
		
		public void bindBuffersToSSBOs() {
			defaultShader.setSSBO("Materials", materialsBuffer);
			defaultShader.setSSBO("Lights", lightsBuffer);
		}
		
		public void updateBuffersLength() {
			materialsBufferSize = Material.getSize() * materials.size();
			
			int lightsCount = 0;
			for(GameObject go : gameObjects) {
				if(go instanceof LightGameObject) {
					lightsCount ++;
				}
			}
			
			lightsBufferSize = LightGameObject.getSize() * lightsCount;
		}
		
		public void updateBuffers() {
			int offset;
			
			ByteBuffer matBuff = BufferUtils.createByteBuffer(materialsBufferSize);
			offset = 0;
			for(Material m : materials) {
				m.writeToBuffer(matBuff, offset);
				m.setSceneBuffer(materialsBuffer);
				m.setSceneBufferOffset(offset);
				offset += Material.getSize();
			}
			glBindBuffer(GL_ARRAY_BUFFER, materialsBuffer);
			glBufferData(GL_ARRAY_BUFFER, matBuff, GL_STATIC_DRAW);
			
			
			ByteBuffer lightBuff = BufferUtils.createByteBuffer(lightsBufferSize);
			offset = 0;
			for(GameObject go : gameObjects) {
				if(go instanceof LightGameObject) {
					LightGameObject l = (LightGameObject) go;
					l.writeToBuffer(lightBuff, offset);
					l.setSceneBuffer(lightsBuffer);
					l.setSceneBufferOffset(offset);
					offset += LightGameObject.getSize();
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
		//this.shaders = new ArrayList<Shader>();
		this.activeCamera = null;
		
		if(defaultShader == null) {
			if(!ShaderUtils.isActive())
				ShaderUtils.init();
			defaultShader = ShaderUtils.getLitShader();
		}
		
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

	//public int registerShader(Shader shader) {
	//	return registerUniqueField(shader, shaders);
	//}

	public void setActiveCamera(CameraGameObject cam) {
		this.activeCamera = cam;
	}
	
	public void updateBuffers() {
		buffHolder.updateBuffersLength();
		buffHolder.updateBuffers();
	}
	
	public void bindBuffersToSSBOs() {
		buffHolder.bindBuffersToSSBOs();
	}
	
	public void draw() {
		activeCamera.update();
		for(GameObject go : gameObjects) {
			if(go instanceof Renderable) {
				Renderer.draw((Renderable)go);
			}
			if(go instanceof LightGameObject) {
				Renderer.draw((LightGameObject)go);
			}
		}
	}
	
	public SceneBuffersHolder getBuffHolder() {
		return buffHolder;
	}

	public void setBuffHolder(SceneBuffersHolder buffHolder) {
		this.buffHolder = buffHolder;
	}

	public VFShader getDefaultShader() {
		return defaultShader;
	}

	public void setDefaultShader(VFShader defaultShader) {
		this.defaultShader = defaultShader;
	}

	public CameraGameObject getActiveCamera() {
		return activeCamera;
	}
}
