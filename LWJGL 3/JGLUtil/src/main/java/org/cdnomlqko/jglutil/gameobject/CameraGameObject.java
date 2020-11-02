package org.cdnomlqko.jglutil.gameobject;

import org.cdnomlqko.jglutil.data.Camera;

/**
 * A GameObject camera
 * @see Camera Camera
 * @see GameObject GameObject
 * 
 * @author CDnoMlqko
 *
 */
public class CameraGameObject extends GameObject{
	
	private final Camera camera;
	
	/**
	 * Constructor from given Camera data
	 * 
	 * @param camera - camera info to be used
	 * 
	 * @see org.cdnomlqko.jglutil.data.Camera Camera
	 */
	public CameraGameObject(Camera camera) {
		super();
		this.camera = camera;
		camera.createMatricesUBO();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Updates the camera position for rendering use
	 */
	@Override
	public void update() {
		super.update();
		camera.UpdateProjectionMatrix();
		camera.updateViewMatrix(transform);
		camera.updateMatricesUBO(transform.getWorldMatrix());
	}
}
