package org.cdnomlqko.jglutil.data;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class contains transformation data in 3d: position, rotation, scale and maintains a matrix that does these transformations - worldMatrix
 * @author CDnoMlqko
 *
 */
public class Transformation {
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private Matrix4f worldMatrix;
	
	/**
	 * Initializes position and rotation as (0, 0, 0) and scale = 1.
	 */
	public Transformation() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.worldMatrix = new Matrix4f();
		this.scale = 1.0f;
		updateWorldMatrix();
	}
	
	/**
	 * Initializes the class with the given position, rotation and scale.
	 * @param position
	 * @param rotation
	 * @param scale
	 */
	public Transformation(Vector3f position, Vector3f rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.worldMatrix = new Matrix4f();
		this.scale = scale;
		updateWorldMatrix();
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
	
	/**
	 * Refreshes the world matrix according to the current values of position, rotation and scale. Must be done after every change to one of the latter.
	 */
	public void updateWorldMatrix() {
		this.worldMatrix.translation(this.position)
			.rotateX(this.rotation.x)
			.rotateY(this.rotation.y)
			.rotateZ(this.rotation.z)
			.scale(this.scale);
	}
}
