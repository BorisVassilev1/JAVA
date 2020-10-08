package org.boby.RayTracing.data.gameobject;

import org.boby.RayTracing.data.Camera;
import org.boby.RayTracing.data.Scene;

public class CameraGameObject extends GameObject{
	
	private Camera camera;
	private int cameraId;
	
	public CameraGameObject(Camera camera) {
		this.camera = camera;
		camera.CreateMatricesUBO();
	}

	public Camera getCamera() {
		return camera;
	}
	
	@Override
	public void register(Scene scene) {
		super.register(scene);
		cameraId = scene.registerCamera(camera);
	}
	
	@Override
	public void update() {
		super.update();
		camera.UpdateProjectionMatrix();
		camera.updateViewMatrix(transform.getWorldMatrix());
		camera.UpdateMatricesUBO(transform.getWorldMatrix());
	}
}
