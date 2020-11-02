package org.cdnomlqko.jglutil.gameobject;

import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;

import org.cdnomlqko.jglutil.data.Light;
import org.cdnomlqko.jglutil.data.Scene;
import org.cdnomlqko.jglutil.data.Transformation;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

/**
 * A GameObject that stores Light data.
 * @see GameObject GameObject
 * @see Light Light
 * @author CDnoMlqko
 *
 */
public class LightGameObject extends GameObject{
	private final Light light;
	private int sceneBufferOffset = -1;
	private int sceneBuffer = -1;
	
	/**
	 * Creates the object, using the given {@link Light} data
	 * @see Light Light
	 * @param light
	 */
	public LightGameObject(Light light) {
		super();
		this.light = light;
	}
	
	/**
	 * Same as {@link #LightGameObject(Light)}, but calls the {@link Light#Light(org.cdnomlqko.jglutil.data.Light.Type, Vector3f, float) Light(Light.Type, Vector3f, float)} constructor itself.
	 * @param type - the type of the light
	 * @param color - color of the light
	 * @param intensity - intensity of the light
	 * 
	 * @see Light Light
	 * @see Light#Light(org.cdnomlqko.jglutil.data.Light.Type, Vector3f, float) Light(Light.Type, Vector3f, float))
	 */
	public LightGameObject(Light.Type type, Vector3f color, float intensity) {
		super();
		this.light = new Light(type, color, intensity);
	}
	
	public Light getLight() {
		return light;
	}
	
	/**
	 * Writes all the data from this object to a buffer for rendering use. Uses std140 alignment of the GLSL struct:<pre><code>
	 * struct Light {
	 *  	mat4 transform;
	 *  	vec3 color;
	 *  	float intensity;
	 *  	int type;
	 *};</code></pre>
	 * @param buff - buffer for the data to be put into
	 * @param offset - offset into the buffer to put the data
	 * 
	 * @see ByteBuffer
	 */
	public void writeToBuffer(ByteBuffer buff, int offset) {
		transform.getWorldMatrix().get(offset, buff);
		light.writeToBuffer(buff, offset + 64);
	}
	
	/**
	 * Get the size of the GLSL struct: <pre><code>
	 * struct Light {
	 *  	mat4 transform;
	 *  	vec3 color;
	 *  	float intensity;
	 *  	int type;
	 * };</code></pre>
	 * @return the size assuming the alignment is std140
	 */
	public static int getSize() {
		return Light.getSize() + 64;
	}
	
	/**
	 * Updates the object. updates the {@link Transformation} and {@link Light} data in the scene buffers if the object is in a {@link Scene}
	 * @see Scene Scene
	 * @see GameObject#isInScene() GameObject.isInScene()
	 */
	@Override
	public void update() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer buff = stack.malloc(LightGameObject.getSize());
			writeToBuffer(buff, 0);
			glBindBuffer(GL_ARRAY_BUFFER, sceneBuffer);
			glBufferSubData(GL_ARRAY_BUFFER, sceneBufferOffset, buff);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
	}
	
	/**
	 * offset in the {@link Scene} lights buffer where the object is written.
	 * @return -1 if the object is not registered in a {@link Scene}, otherwise an integer between 0 and the length of the OpenGL buffer {@link LightGameObject#getSceneBuffer() getSceneBuffer()}
	 */
	public int getSceneBufferOffset() {
		return sceneBufferOffset;
	}

	/**
	 * Do not use this unless you really know what you're doing!
	 * @see LightGameObject#getSceneBufferOffset()
	 * @param sceneBufferOffset
	 */
	public void setSceneBufferOffset(int sceneBufferOffset) {
		this.sceneBufferOffset = sceneBufferOffset;
	}
	
	/**
	 * the {@link Scene} lights buffer where the object is written.
	 * @return -1 if the object is not registered in a {@link Scene}, otherwise a valid OpenGL buffer pointer
	 */
	public int getSceneBuffer() {
		return sceneBuffer;
	}
	
	/**
	 * Do not use this unless you really know what you're doing!
	 * @see LightGameObject#getSceneBuffer() getSceneBuffer()
	 * @param sceneBuffer
	 */
	public void setSceneBuffer(int sceneBuffer) {
		this.sceneBuffer = sceneBuffer;
	}
}
