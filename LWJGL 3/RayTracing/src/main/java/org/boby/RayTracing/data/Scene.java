package org.boby.RayTracing.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.boby.RayTracing.data.gameobject.GameObject;
import org.boby.RayTracing.data.light.Light;
import org.boby.RayTracing.data.mesh.Mesh;
import org.boby.RayTracing.shaders.Shader;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46.*;

public class Scene {

	private ArrayList<GameObject> gameObjects;

	private ArrayList<Mesh> meshes;
	private ArrayList<Material> materials;
	private ArrayList<Transformation> transformations; // just why???
	private ArrayList<Shader> shaders; //  TODO: store the default shader so that buffers can be created
	private ArrayList<Light> lights; // TODO: there is no point in this being stored here
	private ArrayList<Camera> cameras; // TODO: store only the active camera separately
	
	public class SceneBuffersHolder {
		public int cameraInfoBuffer;
		public int cameraInfoBufferSize;
		
		public int materialsBuffer;
		public int materialsBufferSize;
		
		public int lightsBuffer;
		public int lightsBufferSize;
		
		public Scene scene;
		
		public SceneBuffersHolder(Scene scene, Shader defaultShader) {
			this.scene = scene;
			createBuffers(defaultShader);
		}
		
		public void createBuffers(Shader defaultShader) {
			cameraInfoBuffer = glGenBuffers();
			materialsBuffer = glGenBuffers();
			lightsBuffer = glGenBuffers();
			
			// TODO: this currently breaks everything because the buffer is null
			//defaultShader.setUBO("Matrices", cameraInfoBuffer);
			defaultShader.setUBO("Materials", materialsBuffer);
			
			defaultShader.setUBO("Lights", lightsBuffer);
		}
		
		public void updateBuffersLength() {
			int matSize = new Material().getSize() * materials.size();
			
			glBufferData(materialsBuffer, BufferUtils.createByteBuffer(matSize), GL_DYNAMIC_COPY);
			
			int lightSize = new Light().getSize() * lights.size();
			glBufferData(lightsBuffer, BufferUtils.createByteBuffer(lightSize), GL_DYNAMIC_COPY);
		}
		
		public void updateBuffers() {
			ByteBuffer matBuff = BufferUtils.createByteBuffer(materialsBufferSize);
			// TODO: fill the buffers!!!
			
			ByteBuffer lightBuff = BufferUtils.createByteBuffer(lightsBufferSize);
			
		}
	}
	
	private SceneBuffersHolder buffHolder;
	
	public Scene(Shader defaultShader) {
		this.gameObjects = new ArrayList<GameObject>();

		this.meshes = new ArrayList<Mesh>();
		this.materials = new ArrayList<Material>();
		this.transformations = new ArrayList<Transformation>();
		this.shaders = new ArrayList<Shader>();
		this.lights = new ArrayList<Light>();
		this.cameras = new ArrayList<Camera>();
		
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

	public int registerTransformation(Transformation transform) {
		return registerField(transform, transformations);
	}

	public int registerMesh(Mesh mesh) {
		return registerUniqueField(mesh, meshes);
	}

	public int registerMaterial(Material material) {
		return registerUniqueField(material, materials);
	}

	public int registerShader(Shader shader) {
		return registerUniqueField(shader, shaders);
	}

	public int registerLight(Light light) {
		return registerUniqueField(light, lights);
	}

	public int registerCamera(Camera camera) {
		return registerUniqueField(camera, cameras);
	}
	
	
}
