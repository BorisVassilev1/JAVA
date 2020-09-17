package org.boby.RayTracing.utils;

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
		return new Vector3f(this.position);
	}
	
	public Vector3f getRotation() {
		return new Vector3f(this.rotation);
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Matrix4f getWorldMatrix() {
		return new Matrix4f(this.worldMatrix);
	}
	
	public void updateWorldMatrix() {
		this.worldMatrix.translation(this.position).rotateX(this.rotation.x).rotateY(this.rotation.y).rotateZ(this.rotation.z).scale(this.scale);
	}
}
