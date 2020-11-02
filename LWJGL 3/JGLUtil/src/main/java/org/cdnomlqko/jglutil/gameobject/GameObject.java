package org.cdnomlqko.jglutil.gameobject;

import org.cdnomlqko.jglutil.data.Scene;
import org.cdnomlqko.jglutil.data.Transformation;

/**
 * Any kind of object. Can be registered in a Scene and has Transformation
 * @see org.cdnomlqko.jglutil.data.Scene Scene
 * @see org.cdnomlqko.jglutil.data.Transformation Transformation
 * 
 * @author CDnoMlqko
 */
public class GameObject {
	
	public final Transformation transform;
	private boolean isInScene;
	
	/**
	 * Creates a game object with a zero transform.
	 */
	public GameObject() {
		transform = new Transformation();
		isInScene = false;
	}
	
	/*
	 * Registers the object in a scene
	 */
	public void register(Scene scene) {
		scene.regsiterGameObject(this);
		isInScene = true;
	}
	
	/**
	 * Updates the object. 
	 */
	public void update() {
		transform.updateWorldMatrix();
	}
	
	/**
	 * tells whether the object is registered in a Scene or not
	 * @return true if the object has been registered and false if not
	 * 
	 * @see org.cdnomlqko.jglutil.data.Scene Scene
	 */
	public boolean isInScene() {
		return isInScene;
	}
}
