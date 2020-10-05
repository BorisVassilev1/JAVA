package org.boby.RayTracing.data.gameobject;

import org.boby.RayTracing.data.Camera;
import org.boby.RayTracing.data.Scene;

public class CameraGameObject extends GameObject{
	
	private Camera camera;
	private int cameraId;
	
	public CameraGameObject(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return camera;
	}
	
	@Override
	void register(Scene scene) {
		super.register(scene);
		cameraId = scene.registerCamera(camera);
	}
}
