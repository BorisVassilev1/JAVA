package Mesh;

import org.lwjgl.util.vector.Vector3f;

public class CubeMesh extends Mesh{
	public CubeMesh() {
		super(new Vector3f[] {
				new Vector3f(-1,-1,-1),
				new Vector3f(-1,-1, 1),
				new Vector3f(-1, 1,-1),
				new Vector3f(-1, 1, 1),
				new Vector3f( 1,-1,-1),
				new Vector3f( 1,-1, 1),
				new Vector3f( 1, 1,-1),
				new Vector3f( 1, 1, 1)
			},
			new int[] {
				0,1,2,
				1,2,3,
				4,5,6,
				5,6,7,
				0,4,5,
				0,1,5,
				1,3,7,
				1,7,5,
				2,4,0,
				2,4,6,
				3,7,6,
				3,2,6
			},
			new Vector3f[] {
				new Vector3f(0,0,1),
				new Vector3f(0,0,1),
				new Vector3f(0,1,0),
				new Vector3f(0,1,0),
				new Vector3f(1,0,0),
				new Vector3f(1,0,0),
				new Vector3f(0,1,1),
				new Vector3f(0,1,1),
				new Vector3f(1,0,1),
				new Vector3f(1,0,1),
				new Vector3f(1,1,0),
				new Vector3f(1,1,0)
			});
	}

}
