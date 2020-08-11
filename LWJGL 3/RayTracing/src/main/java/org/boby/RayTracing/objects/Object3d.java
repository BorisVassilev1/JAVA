package org.boby.RayTracing.objects;

import org.boby.RayTracing.mesh.Mesh;
import org.boby.RayTracing.shaders.VFShader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Simple 3d object.
 * 
 * @author Boby
 *
 */
public class Object3d {
	
	private Mesh mesh;
	private VFShader shader;
	private Vector3f position;
	private Vector3f rotation;
	
	private Matrix4f worldMatrix;
	
	private float scale;
	
	public Object3d(Mesh mesh, VFShader shader) {
		this.mesh = mesh;
		this.mesh.create();
		this.shader = shader;
		position = new Vector3f();
		rotation = new Vector3f();
		scale = 1;
		worldMatrix = new Matrix4f();
	}
	
	public void delete()
	{
		mesh.delete();
		shader.delete();
	}
	
	public void updateWorldMatrix() {
		this.worldMatrix.translation(this.position)
		.rotateX(this.rotation.x).rotateY(this.rotation.y).rotateZ(this.rotation.z).scale(this.scale);
	}

	public Matrix4f getWorldMatrix() {
		return this.worldMatrix;
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
	
	public VFShader getShader()
	{
		return this.shader;
	}
	public void setShader(VFShader shader)
	{
		this.shader = shader;
	}
	public Mesh getMesh()
	{
		return this.mesh;
	}
	public void setMesh(Mesh mesh)
	{
		this.mesh = mesh;
	}
}
