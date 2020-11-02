package org.cdnomlqko.jglutil.shader.data;

/**
 * This class holds the properties of a SSBO (Shader Storage Buffer Object)
 * @author CDnoMlqko
 *
 */
public class SSBO {
	private final String name;
	private final int numActiveVariables;
	private final int bufferBinding;
	private final BufferVariable[] variables;
	
	public SSBO(String name, int numActiveVariables, int bufferBinding) {
		this.name = name;
		this.numActiveVariables = numActiveVariables;
		this.bufferBinding = bufferBinding;
		this.variables = new BufferVariable[numActiveVariables];
	}

	public int getNumActiveVariables() {
		return numActiveVariables;
	}

	public int getBufferBinding() {
		return bufferBinding;
	}
	
	public BufferVariable[] getVariables() {
		return variables;
	}

	public String getName() {
		return name;
	}
}
