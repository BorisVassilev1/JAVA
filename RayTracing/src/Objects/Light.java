package Objects;

import Mesh.CubeMesh;

public class Light extends Object3d{// this will be an Enum with different types of lights
	
	public float intensity = 0.5f;
	
	public Light() {
		super(new CubeMesh());
		this.scale.set(0.1f, 0.1f, 0.1f);
	}
}
