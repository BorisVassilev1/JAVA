package org.cdnomlqko.jglutil.shader.data;

/**
 * This class holds the properties of a GL_BUFFER_VARIABLE, that is in a SSBO
 * @author CDnoMlqko
 * @see SSBO
 */
public class BufferVariable {
	private final String name;
	private final int type;
	private final int offset;
	
	public BufferVariable(String name, int type, int offset) {
		this.name = name;
		this.type = type;
		this.offset = offset;
	}

	public int getType() {
		return type;
	}

	public int getOffset() {
		return offset;
	}

	public String getName() {
		return name;
	}
}
