package RayTracing;

import java.util.ArrayList;

import Materials.Material;
import Objects.Light;
import Objects.Object3d;

public class Scene {
	
	public ArrayList<Object3d> objects;
	public ArrayList<Light> lights;
	public ArrayList<Material> materials;
	
	public Scene() {
		objects = new ArrayList<Object3d>();
		lights = new ArrayList<Light>();
		materials = new ArrayList<Material>();
	}
	
	public void addObject(Object3d obj)
	{
		objects.add(obj);
	}
	
	public void addLight(Light li)
	{
		lights.add(li);
	}
	
	public void addMaterial(Material mat)
	{
		materials.add(mat);
	}
}
