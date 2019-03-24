package engine.rendering;

import engine.maths.Matrix4f;
import engine.maths.Vector3f;

public class Transformations {
	private Vector3f translation, rotation, scale;
	
	public Transformations() {
		translation = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
	}
	
	public Transformations(Vector3f t, Vector3f r, Vector3f s) {
		translation = t;
		rotation = r;
		scale = s;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().translate(translation);
		Matrix4f rotationMatrix = new Matrix4f().rotate(rotation);
		Matrix4f scaleMatrix = new Matrix4f().scale(scale);
		
		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}

	public Vector3f getTranslation() {
		return translation;
	}
	
	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	
	public void setTranslation(float x, float y, float z) {
		translation = new Vector3f(x, y, z);
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void setRotation(float x, float y, float z) {
		rotation = new Vector3f(x, y, z);
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public void setScale(float x, float y, float z) {
		scale = new Vector3f(x, y, z);
	}
}
