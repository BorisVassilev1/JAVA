package org.boby.RayTracing.rendering;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	Matrix4f projectionMatrix;
	Matrix4f viewMatrix;
	
	float ASPECT;
	
	float FOV;

	final float Z_NEAR;

	final float Z_FAR;
	
	Vector3f position;

	Vector3f rotation;
	
	public Camera(float FOV, float ASPECT, float Z_NEAR, float Z_FAR) {
		this.FOV = FOV;
		this.ASPECT = ASPECT;
		this.Z_NEAR = Z_NEAR;
		this.Z_FAR = Z_FAR;
		
		projectionMatrix = new Matrix4f().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
		viewMatrix = new Matrix4f();
		
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
//	public Camera() {
//		this((float) Math.toRadians(70f), )
//		this.FOV = (float) Math.toRadians(70f);
//		this.Z_NEAR = 0.01f;
//		this.Z_FAR = 1000f;
//	}
	
	public void UpdateProjectionMatrix() {
		projectionMatrix.identity().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
	}
	
	public void UpdateViewMatrix() {
		viewMatrix.identity()
				.rotateX(this.rotation.x)
				.rotateY(this.rotation.y)
				.rotateZ(this.rotation.z)
				.translate(new Vector3f(this.position).mul(-1));
	}
	
	public float getFov() {
		return FOV;
	}

	public void setFov(float fOV) {
		FOV = fOV;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public float getAspect() {
		return ASPECT;
	}
	
	public void setAspect(float aspect) {
		this.ASPECT = aspect;
	}

	public float getZNear() {
		return Z_NEAR;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getZFar() {
		return Z_FAR;
	}

	
}
