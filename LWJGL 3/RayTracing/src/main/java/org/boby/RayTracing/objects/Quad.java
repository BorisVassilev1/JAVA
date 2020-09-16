package org.boby.RayTracing.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.boby.RayTracing.mesh.BasicMesh;
import org.boby.RayTracing.shaders.VFShader;
import org.lwjgl.BufferUtils;

/**
 * @author Boby
 */
public class Quad extends Object3d{

	public Quad(VFShader shader) {
		super(new BasicMesh(
				(FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[] {
						-1.0f, +1.0f, 0,
						+1.0f, +1.0f, 0,
						+1.0f, -1.0f, 0,
						-1.0f, -1.0f, 0
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
				}).flip()/*,
				new float[] {
						0,0,
						1,0,
						1,1,
						0,1
				}*/), shader);
	}

}
