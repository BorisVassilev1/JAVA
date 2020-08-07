package org.boby.RayTracing.shaders;

import static org.lwjgl.opengl.GL46.*;


/**
 * <h1>Implements a simple Shader. Includes a Vertex Shader and a Fragment Shader. Compiles GLSL from source. Supports
 * Uniforms</h1>
 * 
 * @author Boby
 */
public class VFShader extends Shader{

	public VFShader(String vertex_source_file, String fragment_source_file) {
		super(2);
		
		setSourceFiles(vertex_source_file, fragment_source_file);
		
		createShader(GL_VERTEX_SHADER, 0);
		
		createShader(GL_FRAGMENT_SHADER, 1);
		
		finishProgramCreation();
	}
	
}
