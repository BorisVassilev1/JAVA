package org.cdnomlqko.jglutil.data;

import java.nio.ByteBuffer;

import org.cdnomlqko.jglutil.mesh.MultiBufferedMesh;
import org.cdnomlqko.jglutil.mesh.MeshUtils;
import org.joml.Vector3f;

/**
 * This class holds the properties of a light, made to be passed to shaders.
 * @author CDnoMlqko
 *
 */
public class Light implements Bufferable{
	private float intensity;
	private Vector3f color;
	private Type type;
	
	/**
	 * This class holds the type of a light
	 * @author Boby
	 *
	 */
	public enum Type {
		AMBIENT_LIGHT(0, MeshUtils.makeLineCube(1,1,1)),
		DIRECTIONAL_LIGHT(1, MeshUtils.makeArrow()),
		POINT_LIGHT(2, MeshUtils.makeLineOctahedron());
		
		private final int id;
		private final MultiBufferedMesh displayMesh;
		
		private Type(int id, MultiBufferedMesh mesh) {
			this.id = id;
			this.displayMesh = mesh;
		}
		
		public int getId() {
			return id;
		}

		public MultiBufferedMesh getDisplayMesh() {
			return displayMesh;
		}
	}
	
	/**
	 * Creates a light
	 * @param type - {@link Type} of the light
	 * @param color - {@link Vector3f} representation of a RGB color, where the range for each color channel is [0, 1]
	 * @param intensity - intensity of the light
	 */
	public Light(Type type, Vector3f color, float intensity) {
		this.type = type;
		this.color = color;
		this.intensity = intensity;
	}
	
	/**
	 * size of the buffer which will be occupied by {@link #writeToBuffer(ByteBuffer, int)}
	 * @return that size
	 */
	public static int getSize() {
		return 32;
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		buff.putFloat(offset + 0, color.x);
		buff.putFloat(offset + 4, color.y);
		buff.putFloat(offset + 8, color.z);
		buff.putFloat(offset + 12, intensity);
		buff.putInt(offset + 16, type.id);
	}
	
	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
