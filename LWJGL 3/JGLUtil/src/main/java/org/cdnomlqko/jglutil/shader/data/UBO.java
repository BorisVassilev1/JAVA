package org.cdnomlqko.jglutil.shader.data;

/**
 * This class holds the properties of a UBO (Uniform Buffer Object)
 * @author Boby
 *
 */
public class UBO {
	private final String name;
	private final int numActiveVariables;
	private final int bufferBinding;
	private final BlockUniform uniforms[];
	
	public UBO(String name, int numActiveVariables, int bufferBinding) {
		this.name = name;
		this.numActiveVariables = numActiveVariables;
		this.bufferBinding = bufferBinding;
		uniforms = new BlockUniform[numActiveVariables];
	}

	public int getNumActiveVariables() {
		return numActiveVariables;
	}

	public int getBufferBinding() {
		return bufferBinding;
	}
	
	public BlockUniform[] getUniforms() {
		return this.uniforms;
	}

	public String getName() {
		return name;
	}
}
