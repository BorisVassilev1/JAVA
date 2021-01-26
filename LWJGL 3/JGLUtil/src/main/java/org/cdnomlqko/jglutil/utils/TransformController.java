package org.cdnomlqko.jglutil.utils;

import static org.lwjgl.glfw.GLFW.*;

import org.cdnomlqko.jglutil.Input;
import org.cdnomlqko.jglutil.data.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * This class makes controlling a {@link Transformation} using keyboard and mouse input possible.
 * Control with WASD, Shift, Space and mouse movement. Moves like a Minecraft player in Creative mode while flying.
 * @author CDnoMlqko
 *
 */
public class TransformController {
	private Input input;
	private Transformation transform;
	
	private boolean changed = false;
	
	/**
	 * movement speed
	 */
	public float speed = 0.1f;
	
	/**
	 * @param input - an {@link Input} class to manage input.
	 * @param transform - {@link Transformation} to be controlled
	 */
	public TransformController(Input input, Transformation transform) {
		this.input = input;
		this.transform = transform;
	}
	/**
	 * Must be called every game loop for it to work.s
	 */
	public void update() {
		if(!input.isActive()) {
			return;
		}
		changed = false;
		
		Vector3f camRot = transform.getRotation();
		
		if(input.mouseD.x != 0f || input.mouseD.y != 0f) {
			changed = true;
		}
		
		camRot.x += input.mouseD.y / 500;
		camRot.y += input.mouseD.x / 500;
		
		Vector3f camPos = transform.getPosition();
		
		Matrix4f rotationMatrix = new Matrix4f().rotateX(-camRot.x).rotateY(-camRot.y).rotateZ(-camRot.z);
		
		Vector3f forward = new Vector3f(0f, 0f, -1f).mulDirection(rotationMatrix);
		Vector3f forwardXZ = new Vector3f(forward.x, 0f, forward.z).normalize();
		forwardXZ.mul(speed);
		
		Vector3f sideways = new Vector3f(1f, 0f, 0f).mulDirection(rotationMatrix);
		Vector3f sidewaysXZ = new Vector3f(sideways.x, 0f, sideways.z).normalize();
		sidewaysXZ.mul(speed);
		
		if(input.getKey(GLFW_KEY_SPACE) == GLFW_PRESS) {
			camPos.y += speed;
			changed = true;
		}
		if (input.getKey(GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
			camPos.y -= speed;
			changed = true;
		}
		if (input.getKey(GLFW_KEY_A) == GLFW_PRESS) {
			camPos.sub(sidewaysXZ);
			changed = true;
		}
		if (input.getKey(GLFW_KEY_D) == GLFW_PRESS) {
			camPos.add(sidewaysXZ);
			changed = true;
		}
		if (input.getKey(GLFW_KEY_W) == GLFW_PRESS) {
			camPos.add(forwardXZ);
			changed = true;
		}
		if (input.getKey(GLFW_KEY_S) == GLFW_PRESS) {
			camPos.sub(forwardXZ);
			changed = true;
		}
		
		transform.setPosition(camPos);
		transform.setRotation(camRot);
		
		transform.updateWorldMatrix();
	}
	public boolean hasChanged() {
		return changed;
	}
}
