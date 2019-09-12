package org.boby.RayTracing.utils;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.boby.RayTracing.main.Main;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class Input {
	
	public static boolean[] isKeyPressed = new boolean[344];
	
	private static DoubleBuffer xpos, ypos;
	
	public static Vector2f mousePos = new Vector2f();
	private static long windowId;
	public static Vector2f mouseD = new Vector2f();
	public static void initInput(long windowid)
	{
		windowId = windowid;
		// Setup a key callback. It will be called every time a key is pressed, repeated
		// or released.
		glfwSetKeyCallback(windowid, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			if(action == GLFW_PRESS) {
				isKeyPressed[key] = true;
			}
			if(action == GLFW_RELEASE) {
				isKeyPressed[key] = false;
			}
		});
		xpos = BufferUtils.createDoubleBuffer(1);
		ypos = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowid, xpos, ypos);
		mousePos = new Vector2f((float)xpos.get(0),(float)ypos.get(0));
		//System.out.println(a.get(0) + " " + b.get(0));
	}
	
	public static void update() {
		glfwGetCursorPos(windowId, xpos, ypos);
		mouseD = new Vector2f((float)xpos.get(0),(float)ypos.get(0)).sub(mousePos);
		mousePos.set((float)xpos.get(0),(float)ypos.get(0));
		glfwSetCursorPos(windowId,Main.window.getWidth() / 2d, Main.window.getHeight() / 2d);
		mousePos.set(Main.window.getWidth() / 2, Main.window.getHeight() / 2);
	}
	
	
}
