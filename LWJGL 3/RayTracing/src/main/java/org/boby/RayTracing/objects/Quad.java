package org.boby.RayTracing.objects;

import org.boby.RayTracing.mesh.Mesh;
import org.boby.RayTracing.shaders.Shader;

/**
 * @author Boby
 */
public class Quad extends Object3d{

	public Quad(Shader shader) {
		super(new Mesh(
				new float[] {
						-1.0f, +1.0f, 0,
						+1.0f, +1.0f, 0,
						+1.0f, -1.0f, 0,
						-1.0f, -1.0f, 0
				},
				new int[] {
						0, 1, 2,
						0, 2, 3
				},
				new float[] {
						1.0f, 0.0f, 0.0f,
					    0.0f, 1.0f, 0.0f,
					    0.0f, 0.0f, 1.0f,
					    0.0f, 1.0f, 1.0f,
				},
				new float[] {
						0,0,
						1,0,
						1,1,
						0,1
				}), shader);
	}

}
