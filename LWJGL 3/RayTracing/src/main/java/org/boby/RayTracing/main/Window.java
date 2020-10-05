package org.boby.RayTracing.main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

public class Window {

	private int width;
	private int height;
	private String name;
	private long id;
	private boolean vsync;

	@FunctionalInterface
	public interface ResizeCallback {
		public void apply();
	}
	
	ResizeCallback onResize;
	
	public Window(String name, int width, int height, boolean resizable, boolean vsync) {
		this.width = width;
		this.height = height;
		this.name = name;
		this.vsync = vsync;

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		if(resizable)
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		else
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will not be 
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		// Create the window
		id = glfwCreateWindow(width, height, name, NULL, NULL);
		if (id == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetFramebufferSizeCallback(id, (window, _width, _height) -> {
			this.width = _width;
			this.height = _height;
			if(onResize != null)
			onResize.apply();
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
		
		// create the opengl context
		GL.createCapabilities(); 
		Callback debugProc = GLUtil.setupDebugMessageCallback();
		nglDebugMessageControl(GL_DEBUG_SOURCE_API, GL_DEBUG_TYPE_OTHER, GL_DEBUG_SEVERITY_NOTIFICATION, 0, NULL, false);
		
		// Enable v-sync
		if (this.vsync)
			glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(id);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0f, 0f, 0f, 1f);
		
		//glEnable(GL_DEBUG_OUTPUT);
		/*glDebugMessageCallback(new GLDebugMessageCallback() {
			
			@Override
			public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
				String msg = getMessage(length, message);
				System.err.printf("GL ERROR CALLBACK: %s type = 0x%x, severity = 0x%x, message = %s\n", ( type == GL_DEBUG_TYPE_ERROR ? "** GL ERROR **" : "" ),
			            type, severity, msg);
				
			}
		}, 0);*/
	}

	/**
	 * swaps the window buffers
	 */
	public void swapBuffers() {
		glfwSwapBuffers(id);
	}

	/**
	 * Checks if glfw requests the window to be closed.
	 * 
	 * @return is there a request for the window to be closed
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(id);
	}

	/**
	 * destroys the window.
	 */
	public void delete() {
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void SetResizedCallback(ResizeCallback onresize) {
		this.onResize = onresize;
	}

	public long getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String getName() {
		return this.name;
	}
}
