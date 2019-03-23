package org.boby.RayTracing.utils;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
	public static void initInput(long windowid)
	{
		// Setup a key callback. It will be called every time a key is pressed, repeated
				// or released.
				glfwSetKeyCallback(windowid, (window, key, scancode, action, mods) -> {
					if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
						glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
				});
	}
	
	
}
