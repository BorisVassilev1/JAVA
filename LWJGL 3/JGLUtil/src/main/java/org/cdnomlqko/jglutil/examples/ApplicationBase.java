package org.cdnomlqko.jglutil.examples;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.cdnomlqko.jglutil.Input;
import org.cdnomlqko.jglutil.Time;
import org.cdnomlqko.jglutil.Window;
import org.cdnomlqko.jglutil.utils.FramerateManager;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

/**
 * This class is used as a sample structure of an application.
 * Has a {@link Window}, {@link Time}, {@link FramerateManager}, {@link Input}, all initialized.
 * 
 * Also has {@link ImGuiImplGlfw} and {@link ImGuiImplGl3} so UI can be made via the {@link #gui()} method. 
 * Currently, the Viewports flag of ImGui is always enabled. TODO
 * 
 * @author CDnoMlqko
 *
 */
public abstract class ApplicationBase {
	
	protected Window window;
	protected Time time;
	protected Input input;
	protected FramerateManager frm;
	
	private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private final ImGuiImplGl3 imGuilGL3 = new ImGuiImplGl3();
	
	/**
	 * starts the program
	 */
	public void run(Window window) {
		this.window = window;
		
		System.out.println("LWJGL version: " + Version.getVersion());
		
		input = new Input(window);
		input.showMouse();
		
		time = new Time();
		frm = new FramerateManager(time);
	
		frm.setSecondPassedCallback((framerate) -> {
			System.out.println("Framerate: " + framerate);
		});
		
		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		//io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		
		imGuiGlfw.init(window.getId(), true);
		imGuilGL3.init("#version 430");
		
		init();
		
		while(!window.shouldClose()) {
			time.updateTime();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			input.update();
			
			loop();
			
			imGuiGlfw.newFrame();
			ImGui.newFrame();
			
			gui();
			
			ImGui.render();
			imGuilGL3.renderDrawData(ImGui.getDrawData());
			
			if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
				final long backupWindowPtr = GLFW.glfwGetCurrentContext();
				ImGui.updatePlatformWindows();
				ImGui.renderPlatformWindowsDefault();
				GLFW.glfwMakeContextCurrent(backupWindowPtr);
			}
			
			window.swapBuffers();
			frm.update();
		}
		
		cleanup();
		window.delete();
		frm.stop();
		imGuilGL3.dispose();
		imGuiGlfw.dispose();
		ImGui.destroyContext();
	}
	/**
	 * called first, for initialization
	 */
	public abstract void init();
	
	/**
	 * this will be the game loop
	 */
	public abstract void loop();
	
	/**
	 * here you draw your GUI with ImGui
	 */
	public abstract void gui();
	
	/**
	 * called just before exit, for deallocation of memory.
	 */
	public abstract void cleanup();
}
