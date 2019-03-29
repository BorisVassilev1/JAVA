package org.boby.RayTracing.objects;

import org.boby.RayTracing.materials.Material;
import org.boby.RayTracing.mesh.Mesh;
import org.joml.Vector3f;
public class Object3d {
	
	private Mesh mesh;
	private Material material;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Object3d(Mesh mesh) {
		this.mesh = mesh;
		this.mesh.create();
		material = new Material();
		position = new Vector3f();
		rotation = new Vector3f();
		scale = 1;
	}
	
	public void destroy()
	{
		mesh.remove();
		material.remove();
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	public void setMaterial(Material mat)
	{
		material.remove();
		this.material = mat;
	}
	public Mesh getMesh()
	{
		return this.mesh;
	}
	public void setMesh(Mesh mesh)
	{
		mesh.remove();
		this.mesh = mesh;
	}
}
