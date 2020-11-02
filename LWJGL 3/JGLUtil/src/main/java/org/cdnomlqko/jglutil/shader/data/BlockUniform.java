package org.cdnomlqko.jglutil.shader.data;

/**
 * This class holds the properties of a GLSL Uniform that is in an UBO.
 * @author CDnoMlqko
 * @see UBO
 */
public class BlockUniform {
	private final String name;
	private final int type;
	private final int location;
	private final int offset;
	
	public BlockUniform(String name, int type, int location, int offset) {
		this.name = name;
		this.type = type;
		this.location = location;
		this.offset = offset;
	}

	public int getType() {
		return type;
	}

	public int getLocation() {
		return location;
	}

	public int getOffset() {
		return offset;
	}

	public String getName() {
		return name;
	}
}
