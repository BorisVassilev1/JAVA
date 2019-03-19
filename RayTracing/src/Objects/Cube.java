package Objects;

import org.lwjgl.util.vector.Vector3f;

import Mesh.CubeMesh;

public class Cube extends Object3d{

	public Cube() {
		super(new CubeMesh());
	}

}
