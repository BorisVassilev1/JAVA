package org.boby.RayTracing.data.light;

import org.joml.Vector3f;

public class Light {
	float intensity;
	Vector3f color;
	LightType type;
	
	public Light(LightType type, Vector3f color, float intensity) {
		this.type = type;
		this.color = color;
		this.intensity = intensity;
	}
	
}
