package org.boby.RayTracing.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.boby.RayTracing.utils.Input;
import org.boby.RayTracing.utils.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	private int width;
	private int height;
	private String name;
	private long id;
	private boolean resized;
	
	public Window(int width, int height, String name) {
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	
	public void create() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		// Create the window
		id = glfwCreateWindow(width, height, name, NULL, NULL);
		if (id == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		Input.initInput(id);

		glfwSetFramebufferSizeCallback(id, (window, width, height) -> {
		    Window.this.width = width;
		    Window.this.height = height;
		    Window.this.setResized(true);
		});
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(id, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(id, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(id);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(id);
		Time.initTime();
	}
	/**
	 * swaps the window buffers
	 */
	public void swapBuffers()
	{
		glfwSwapBuffers(id);
	}
	/**
	 * 
	 * @return is there a request for the window to be closed
	 */
	public boolean shouldClose()
	{
		return glfwWindowShouldClose(id);
	}
	/**
	 * Should be used only if VSync is turned off.
	 * @param fps - the maximum frames per second for the window to reach
	 */
	public void sync(int fps)
	{
		long deltatime = Time.deltaTimeI;
		if(deltatime < 1000000000/fps)
		{
			try {
				Thread.sleep((int) (1000000000/fps - deltatime) / 1000000, (int) (1000000000/fps - deltatime) % 1000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public long getId()
	{
		return id;
	}
	
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public boolean isResized() {
		return resized;
	}
	
	public void setResized(boolean resized) {
		this.resized = resized;
	}
}
