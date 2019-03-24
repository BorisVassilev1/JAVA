package objects;

import mesh.Mesh;

public class Cube extends Object3d{

	public Cube() {
		super(new Mesh(new float[] {
				-1, -1, -1,
				-1, +1, -1,
				+1, +1, -1,
				+1, -1, -1,
				-1, -1, +1,
				-1, +1, +1,
				+1, +1, +1,
				+1, -1, +1
				
		}, new int[] {
				0, 1, 2,
				0, 2, 3,
				4, 5, 6,
				4, 6, 7
		}, new float[] {
				1, 1, 1,
				1, 1, 1, 
				1, 1, 1,
				1, 1, 1,
				1, 1, 1,
				1, 1, 1,
				1, 1, 1,
				1, 1, 1
		}, new float[] {
				// i'm lazy
		}));
	}

}
