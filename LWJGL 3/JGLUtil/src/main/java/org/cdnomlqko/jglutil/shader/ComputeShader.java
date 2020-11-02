package org.cdnomlqko.jglutil.shader;

import static org.lwjgl.opengl.GL46.*;

/**
 * <h1>Implements a simple Compute Shader. Compiles GLSL from source. Supports
 * Uniforms, SSBO-s, UBO-s</h1>
 * 
 * @author CDnoMlqko
 */
public class ComputeShader extends Shader {

	public ComputeShader(String compute_source_file) {
		super(1);
		setSourceFiles(compute_source_file);
		
		createShader(GL_COMPUTE_SHADER, 0);

		finishProgramCreation();
	}
}
