package org.boby.RayTracing.objects;

import org.boby.RayTracing.mesh.Mesh;

public class RenderingQuad extends Object3d{

	public RenderingQuad() {
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
				new float[] {// don't have to specify the texcoords because texturing is not implemented yet
						0,0,
						1,0,
						1,1,
						0,1
				}
				));
		// TODO Auto-generated constructor stub
	}

}