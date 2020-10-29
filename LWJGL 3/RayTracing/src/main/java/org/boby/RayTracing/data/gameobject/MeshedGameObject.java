package org.boby.RayTracing.data.gameobject;

import org.boby.RayTracing.data.Material;
import org.boby.RayTracing.data.Scene;
import org.boby.RayTracing.data.mesh.Mesh;

public class MeshedGameObject extends GameObject{
	
	public Mesh mesh;
	public Material material;
	int materialId;
	
	@Override
	public void register(Scene scene) {
		super.register(scene);
		
		materialId = scene.registerMaterial(material);
	}
	
	public MeshedGameObject(Mesh mesh, Material material) {
		super();
		this.mesh = mesh;
		this.material = material;
	}
	
	public int getMaterialId() {
		return materialId;
	}
}
