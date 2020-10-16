package org.boby.RayTracing.data.gameobject;

import java.nio.FloatBuffer;

import org.boby.RayTracing.data.Scene;
import org.boby.RayTracing.data.Transformation;

public class GameObject {
	
	Transformation transform;
	int transformationId;
	boolean isInScene;
	
	public GameObject() {
		transform = new Transformation();
		isInScene = false;
	}
	
	public void register(Scene scene) {
		transformationId = scene.registerTransformation(transform);
		scene.regsiterGameObject(this);
		isInScene = true;
	}
	
	public void update() {
		transform.updateWorldMatrix();
	}
}
