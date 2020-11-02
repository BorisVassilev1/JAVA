package org.cdnomlqko.jglutil.data;

import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;

import org.cdnomlqko.jglutil.gameobject.GameObject;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

/**
 * This class holds the properties of a material for an object, made to be passed to shaders.
 * @author CDnoMlqko
 *
 */
public class Material implements Bufferable{
	
	private Vector3f color;
	private boolean hasTexture;
	private int textureId = -1;
	
	private float diffusePower;
	private float specularPower;
	private float specularExponent;
	
	private int sceneBufferOffset = -1;
	private int sceneBuffer = -1;
	
	/**
	 * Creates a textured material
	 * @param texture - the {@link Texture2D} to be used
	 */
	public Material(Texture2D texture) {
		this.color = null;
		this.textureId = texture.getID();
		this.hasTexture = true;
		this.diffusePower = 1.0f;
		this.specularPower = 1.0f;
		this.specularExponent = 20.0f;
	}
	
	/**
	 * Creates a solid-color material
	 * @param color - color for the material to have
	 */
	public Material(Vector3f color) {
		this.color = color;
		this.textureId = 0;
		this.hasTexture = true;
		this.diffusePower = 1.0f;
		this.specularPower = 1.0f;
		this.specularExponent = 20.0f;
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		buff.putFloat(offset,  color.x);
		buff.putFloat(offset + 4, color.y);
		buff.putFloat(offset + 8, color.z);
		
		buff.put(offset + 12, hasTexture ? (byte)1 : (byte)0);
		buff.putFloat(offset + 16, diffusePower);
		buff.putFloat(offset + 20, specularPower);
		buff.putFloat(offset + 24, specularExponent);
	}
	
	/**
	 * size of the buffer which will be occupied by {@link #writeToBuffer(ByteBuffer, int)}
	 * @return that size
	 */
	public static int getSize() {
		return 32;
	}

	/**
	 * Updates the {@link Scene} buffers if there exists a {@link GameObject} which has that material and is registered in a {@link Scene}
	 */
	public void update() {
		if(sceneBuffer == -1) return;
		try(MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer buff = stack.malloc(getSize());
			writeToBuffer(buff, 0);
			glBindBuffer(GL_ARRAY_BUFFER, sceneBuffer);
			glBufferSubData(GL_ARRAY_BUFFER, sceneBufferOffset, buff);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public boolean hasTexture() {
		return hasTexture;
	}

	public void setHasTexture(boolean use_texture) {
		this.hasTexture = use_texture;
	}

	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int texture_id) {
		this.textureId = texture_id;
	}

	public float getDiffusePower() {
		return diffusePower;
	}

	public void setDiffusePower(float diffuse_power) {
		this.diffusePower = diffuse_power;
	}

	public float getSpecularPower() {
		return specularPower;
	}

	public void setSpecularPower(float specular_power) {
		this.specularPower = specular_power;
	}

	public float getSpecularExponent() {
		return specularExponent;
	}

	public void setSpecularExponent(float specular_exponent) {
		this.specularExponent = specular_exponent;
	}
	
	/**
	 * offset in the {@link Scene} lights buffer where the object is written.
	 * @return -1 if no {@link GameObject} with this material is registered in a {@link Scene}, otherwise an integer between 0 and the length of the OpenGL buffer {@link #getSceneBuffer() getSceneBuffer()}
	 */
	public int getSceneBufferOffset() {
		return sceneBufferOffset;
	}

	/**
	 * Do not use this unless you really know what you're doing!
	 * @see #getSceneBufferOffset()
	 * @param sceneBufferOffset
	 */
	public void setSceneBufferOffset(int sceneBufferOffset) {
		this.sceneBufferOffset = sceneBufferOffset;
	}
	
	/**
	 * the {@link Scene} lights buffer where the object is written.
	 * @return -1 if no {@link GameObject} with this material is registered in a {@link Scene}, otherwise a valid OpenGL buffer pointer
	 */
	public int getSceneBuffer() {
		return sceneBuffer;
	}
	
	/**
	 * Do not use this unless you really know what you're doing!
	 * @see #getSceneBuffer()
	 * @param sceneBuffer
	 */
	public void setSceneBuffer(int sceneBuffer) {
		this.sceneBuffer = sceneBuffer;
	}
	
}
