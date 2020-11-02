package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

/**
 * This class contains static methods for {@link Mesh} creation.
 * @author CDnoMlqko
 */
// TODO: add more primitives
public class MeshGenerator {
	/**
	 * Creates a Quad mesh with the described size on the XY plane.
	 * @param sizex - size along the X axis
	 * @param sizey - size along the Y axis
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(float sizex, float sizey) {
		float sx = sizex, sy = sizey;
		return new BasicMesh(
				(FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[] {
						-sx, +sy, 0,
						+sx, +sy, 0,
						+sx, -sy, 0,
						-sx, -sy, 0
				}).flip(),
				(FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[] {
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f,
						0.0f, 0.0f, 1.0f
				}).flip(),
				(IntBuffer) BufferUtils.createIntBuffer(6).put(new int[] {
						0, 1, 2,
						0, 2, 3
				}).flip(),
				(FloatBuffer)BufferUtils.createFloatBuffer(8).put(new float[] {
						0,0,
						1,0,
						1,1,
						0,1
				}).flip(),
				(FloatBuffer) BufferUtils.createFloatBuffer(16).put(new float[] {
						1,0,0,1,
						0,1,0,1,
						0,0,1,1,
						1,1,0,1
				}).flip());
	}
	
	/**
	 * {@link #makeQuad(float, float)}, but with a vector for size
	 * @param size 
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(float size) {
		return makeQuad(size, size);
	}
	
	/**
	 * {@link #makeQuad(float, float)}, but for squares
	 * @param size
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(Vector2f size) {
		return makeQuad(size.x, size.y);
	}
}
