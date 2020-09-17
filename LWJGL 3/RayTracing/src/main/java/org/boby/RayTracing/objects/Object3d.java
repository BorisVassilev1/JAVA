package org.boby.RayTracing.objects;

import org.boby.RayTracing.mesh.BasicMesh;
import org.boby.RayTracing.shaders.VFShader;
import org.boby.RayTracing.utils.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Simple 3d object.
 * 
 * @author Boby
 *
 */
public class Object3d {
	
	private BasicMesh mesh;
	private VFShader shader;
	
	public Transformation transform;
	
	public Object3d(BasicMesh mesh, VFShader shader) {
		this.mesh = mesh;
		this.shader = shader;
		this.transform = new Transformation();
	}
	
	public void delete()
	{
		mesh.delete();
		shader.delete();
	}
	
	public VFShader getShader()
	{
		return this.shader;
	}
	public void setShader(VFShader shader)
	{
		this.shader = shader;
	}
	public BasicMesh getMesh()
	{
		return this.mesh;
	}
	public void setMesh(BasicMesh mesh)
	{
		this.mesh = mesh;
	}
}
