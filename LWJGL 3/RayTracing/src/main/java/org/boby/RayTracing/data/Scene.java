package org.boby.RayTracing.data;

import java.util.ArrayList;

import org.boby.RayTracing.data.gameobject.GameObject;
import org.boby.RayTracing.data.light.Light;
import org.boby.RayTracing.data.mesh.Mesh;
import org.boby.RayTracing.shaders.Shader;

public class Scene{
	
	private ArrayList<GameObject> gameObjects;

	private ArrayList<Mesh> meshes;
	private ArrayList<Material> materials;
	private	ArrayList<Transformation> transformations;
	private ArrayList<Shader> shaders;
	private ArrayList<Light> lights;
	
	public Scene() {
		this.gameObjects = new ArrayList<GameObject>();
		
		this.meshes = new ArrayList<Mesh>();
		this.materials = new ArrayList<Material>();
		this.transformations = new ArrayList<Transformation>();
		this.shaders = new ArrayList<Shader>();
		this.lights = new ArrayList<Light>();
	}
	private class Registerer <T> {
		public int registerUniqueField(T obj, ArrayList<T> list) {
			int id = list.indexOf(obj);
			if(id == -1) {
				id = list.size();
				list.add(obj);
			}
			return id;
		}
		
		private int registerField(T obj, ArrayList<T> list) {
			int id = list.size();
			list.add(obj);
			return id;
		}
	}
	
	public int regsiterGameObject(GameObject go) {
		return new Registerer<GameObject>().registerField(go, gameObjects);
	}
	
	public int registerTransformation(Transformation transform) {
		return new Registerer<Transformation>().registerField(transform, transformations);
	}
	
	public int registerMesh(Mesh mesh) {
		return new Registerer<Mesh>().registerUniqueField(mesh, meshes);
	}
	
	public int registerMaterial(Material material) {
		return new Registerer<Material>().registerUniqueField(material, materials);
	}
	
	public int registerShader(Shader shader) {
		return new Registerer<Shader>().registerUniqueField(shader, shaders);
	}
	
	public int registerLight(Light light) {
		return new Registerer<Light>().registerUniqueField(light, lights);
	}
	
}
