package org.cdnomlqko.jglutil.shader.data;

/**
 * This class holds the properties of a GLSL uniform that is not part of a block (not in a UBO)
 * @author CDnoMlqko
 *
 */
public class NotBlockUniform {
	private final String name;
	private final int type;
	private final int location;
	
	public NotBlockUniform(String name, int type, int location) {
		this.name = name;
		this.type = type;
		this.location = location;
	}

	public int getType() {
		return type;
	}

	public int getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}
	
}
