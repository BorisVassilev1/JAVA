package org.boby.RayTracing.utils;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.boby.RayTracing.examples.CubeExample;
import org.boby.RayTracing.main.Window;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class Input {
	
	public boolean[] isKeyPressed = new boolean[344];
	
	private DoubleBuffer mouse_x_pos, ypos;
	
	public Vector2f mousePos = new Vector2f();
	private Window window;
	public Vector2f mouseD = new Vector2f();
	
	
	public boolean LockMouse = true;
	
	public Input(Window window)
	{
		//windowId = windowid;
		this.window = window;
		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(window.getId(), (windowId, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(windowId, true); // We will detect this in the rendering loop
			if(action == GLFW_PRESS) {
				isKeyPressed[key] = true;
			}
			if(action == GLFW_RELEASE) {
				isKeyPressed[key] = false;
			}
		});
		
//		glfwSetKeyCallback(window.getId(), (windowId, key, scancode, action, mods) -> {
//			if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
//				glfwSetWindowShouldClose(windowId, true);
//		});
		mouse_x_pos = BufferUtils.createDoubleBuffer(1);
		ypos = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window.getId(), mouse_x_pos, ypos);
		mousePos = new Vector2f((float)mouse_x_pos.get(0),(float)ypos.get(0));
	}
	
	public void update() {
		glfwGetCursorPos(window.getId(), mouse_x_pos, ypos);
		
		// Calculate mouse movement
		mouseD = new Vector2f((float)mouse_x_pos.get(0),(float)ypos.get(0)).sub(mousePos);
		
		// Set the local mouse position variable
		mousePos.set((float)mouse_x_pos.get(0),(float)ypos.get(0));
		
		if(LockMouse) {
			// Lock the mouse in the center of the screen
			glfwSetCursorPos(window.getId(),window.getWidth() / 2d, window.getHeight() / 2d);
			
			mousePos.set(window.getWidth() / 2, window.getHeight() / 2);
		}
	}
	
	
}
