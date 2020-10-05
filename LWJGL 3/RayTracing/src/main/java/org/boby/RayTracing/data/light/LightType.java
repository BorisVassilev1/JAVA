package org.boby.RayTracing.data.light;

public enum LightType {
	AMBIENT_LIGHT(0),
	DIRECTIONAL_LIGHT(1),
	POINT_LIGHT(2);
	
	public int id;
	
	private LightType(int id) {
		this.id = id;
	}
}
