package engine.rendering.models;

import engine.maths.Matrix4f;
import engine.maths.Vector3f;
import engine.rendering.Transformations;

public class ModelEntity {
	private TexturedModel model;
	private Transformations transformation;
	public ModelEntity(TexturedModel model, Vector3f position, Vector3f angle, Vector3f scale) {
		this.model = model;
		transformation = new Transformations(position, angle, scale);
	}
	
	public Matrix4f getTransformationMatrix() {
		return transformation.getTransformation();
	}
	
	public void addPosition(float x, float y, float z) {
		transformation.setTranslation(transformation.getTranslation().add(new Vector3f(x, y, z)));
	}
	
	public void addRotation(float x, float y, float z) {
		transformation.setRotation(transformation.getRotation().add(new Vector3f(x, y, z)));
	}
	
	public void addScale(float x, float y, float z) {
		transformation.setScale(transformation.getScale().add(new Vector3f(x, y, z)));
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public Vector3f getRotation() {
		return transformation.getRotation();
	}	
	
	public void setRotation(Vector3f vector) {
		transformation.setRotation(vector);
	}
	
	public Vector3f getPosition() {
		return transformation.getTranslation();
	}	
	
	public void setPosition(Vector3f vector) {
		transformation.setTranslation(vector);
	}
	
	public Vector3f getScale() {
		return transformation.getScale();
	}
	
	public void setScale(Vector3f vector) {
		transformation.setScale(vector);
	}
}