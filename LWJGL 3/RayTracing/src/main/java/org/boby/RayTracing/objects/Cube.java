package org.boby.RayTracing.objects;

import org.boby.RayTracing.mesh.Mesh;
import org.boby.RayTracing.shaders.Shader;

public class Cube extends Object3d{

	public Cube(Shader shader) {
		super(new Mesh(new float[] {
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
				
		}, new int[] {
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
		}, new float[] {
				1.0f, 0.0f, 0.0f,
			    0.0f, 1.0f, 0.0f,
			    0.0f, 0.0f, 1.0f,
			    0.0f, 0.5f, 1.0f,
			    1.0f, 0.0f, 0.0f,
			    0.0f, 1.0f, 0.0f,
			    0.0f, 0.0f, 1.0f,
			    0.0f, 1.0f, 1.0f,
		}, new float[] {
				0,0,
				0,1,
				1,1,
				1,0,
				1,0,
				0,0,
				1,1,
				0,1
		}),shader);
		getPosition().set(0,0,-3);
	}

}
