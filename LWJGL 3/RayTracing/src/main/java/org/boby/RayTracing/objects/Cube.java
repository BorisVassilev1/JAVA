package org.boby.RayTracing.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.boby.RayTracing.data.mesh.BasicMesh;
import org.boby.RayTracing.shaders.VFShader;
import org.lwjgl.BufferUtils;

/**
 * Cube
 * 
 * @author Boby
 *
 */
public class Cube extends Object3d{

	public Cube(VFShader shader) {
		super(new BasicMesh(
			(FloatBuffer) BufferUtils.createFloatBuffer(3 * 8).put(new float[] {
				// VO
			    -1.0f,  1.0f,  1.0f,
			    // V1
			    -1.0f, -1.0f,  1.0f,
			    // V2
			     1.0f, -1.0f,  1.0f,
			    // V3
			     1.0f,  1.0f,  1.0f,
			    // V4
			    -1.0f,  1.0f, -1.0f,
			    // V5
			     1.0f,  1.0f, -1.0f,
			    // V6
			    -1.0f, -1.0f, -1.0f,
			    // V7
			     1.0f, -1.0f, -1.0f,
			}).flip(), 
			(FloatBuffer) BufferUtils.createFloatBuffer(3 * 8).put(new float[] {
				-0.5773502691896258f,  0.5773502691896258f,  0.5773502691896258f,
				-0.5773502691896258f, -0.5773502691896258f,  0.5773502691896258f,
				 0.5773502691896258f, -0.5773502691896258f,  0.5773502691896258f,
				 0.5773502691896258f,  0.5773502691896258f,  0.5773502691896258f,
				-0.5773502691896258f,  0.5773502691896258f, -0.5773502691896258f,
				 0.5773502691896258f,  0.5773502691896258f, -0.5773502691896258f,
				-0.5773502691896258f, -0.5773502691896258f, -0.5773502691896258f,
				 0.5773502691896258f, -0.5773502691896258f, -0.5773502691896258f
			}).flip(), 
			(IntBuffer) BufferUtils.createIntBuffer(36).put( new int[] {
				// Front face
			    0, 1, 3, 3, 1, 2,
			    // Top Face
			    4, 0, 3, 5, 4, 3,
			    // Right face
			    3, 2, 7, 5, 3, 7,
			    // Left face
			    6, 1, 0, 6, 0, 4,
			    // Bottom face
			    2, 1, 6, 2, 6, 7,
			    // Back face
			    7, 6, 4, 7, 4, 5,
			}).flip()),
			shader);
	}

}
