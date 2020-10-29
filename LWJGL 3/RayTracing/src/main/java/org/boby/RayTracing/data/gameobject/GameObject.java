package org.boby.RayTracing.data.gameobject;

import org.boby.RayTracing.data.Scene;
import org.boby.RayTracing.data.Transformation;

public class GameObject {
	
	public Transformation transform;
	boolean isInScene;
	
	public GameObject() {
		transform = new Transformation();
		isInScene = false;
	}
	
	public void register(Scene scene) {
		scene.regsiterGameObject(this);
		isInScene = true;
	}
	
	public void update() {
		transform.updateWorldMatrix();
	}
}
