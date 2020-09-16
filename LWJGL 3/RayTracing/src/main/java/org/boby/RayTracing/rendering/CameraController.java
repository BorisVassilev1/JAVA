package org.boby.RayTracing.rendering;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.boby.RayTracing.utils.Input;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraController {
	
	Input input;
	Camera camera;
	
	public float speed = 0.1f;
	
	public CameraController(Input input, Camera camera) {
		this.input = input;
		this.camera = camera;
	}
	
	public void update() {
		
		Vector3f camRot = camera.getRotation();

		camRot.x += input.mouseD.y / 500;
		camRot.y += input.mouseD.x / 500;
		
		Vector3f camPos = camera.getPosition();
		
		Matrix4f rotationMatrix = new Matrix4f().rotateX(-camRot.x).rotateY(-camRot.y).rotateZ(-camRot.z);
		
		Vector3f forward = new Vector3f(0f, 0f, 1f).mulDirection(rotationMatrix);
		Vector3f forwardXZ = new Vector3f(forward.x, 0f, forward.z).normalize();
		forwardXZ.mul(speed);
		
		Vector3f sideways = new Vector3f(1f, 0f, 0f).mulDirection(rotationMatrix);
		Vector3f sidewaysXZ = new Vector3f(sideways.x, 0f, sideways.z).normalize();
		sidewaysXZ.mul(speed);
		
		if(input.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
			camPos.y += 0.1;
		}
		if (input.getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
			camPos.y -= 0.1;
		}
		if (input.getKey(GLFW_KEY_A) == GLFW_PRESS) {
			camPos.sub(sidewaysXZ);
		}
		if (input.getKey(GLFW_KEY_D) == GLFW_PRESS) {
			camPos.add(sidewaysXZ);
		}
		if (input.getKey(GLFW_KEY_W) == GLFW_PRESS) {
			camPos.sub(forwardXZ);
		}
		if (input.getKey(GLFW_KEY_S) == GLFW_PRESS) {
			camPos.add(forwardXZ);
		}

		if (input.getKey(GLFW_KEY_9) == GLFW_PRESS)
			camera.setFov(camera.getFov() + 0.01f);
		if (input.getKey(GLFW_KEY_0) == GLFW_PRESS)
			camera.setFov(camera.getFov() - 0.01f);

		camera.UpdateViewMatrix();
		camera.UpdateProjectionMatrix();
	}
}
