package org.cdnomlqko.jglutil.shader;

public class ShaderUtils {
	private static VFShader litShader;
	private static VFShader unlitShader;
	
	private static boolean active;
	
	public static boolean isActive() {return active;}

	
	public static void init() {
		litShader = new VFShader("/res/shaders/verfrag_shaders/BasicVertexShader.vs", "/res/shaders/verfrag_shaders/BasicFragmentShader.fs");
		unlitShader = new VFShader("/res/shaders/verfrag_shaders/BasicVertexShader.vs", "/res/shaders/verfrag_shaders/UnlitFragmentShader.fs");
	}
	
	public static void delete() {
		litShader.delete();
		unlitShader.delete();
	}
	
	public static VFShader getUnlitShader() {
		return unlitShader;
	}
	
	public static VFShader getLitShader() {
		return litShader;
	}
}
