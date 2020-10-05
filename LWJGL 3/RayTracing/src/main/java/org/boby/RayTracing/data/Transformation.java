package org.boby.RayTracing.data;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private Matrix4f worldMatrix;
	
	public Transformation() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.worldMatrix = new Matrix4f();
		this.scale = 1.0f;
	}
	
	public void setPosition(Vector3f pos) {
		this.position.set(pos);
	}
	
	public void setRotation(Vector3f rot) {
		this.rotation.set(rot);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Matrix4f getWorldMatrix() {
		return worldMatrix;
	}
	
	public void updateWorldMatrix() {
		this.worldMatrix.translation(this.position)
			.rotateZ(this.rotation.z)
			.rotateY(this.rotation.y)
			.rotateX(this.rotation.x)
			.scale(this.scale);
	}
}
