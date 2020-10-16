package org.boby.RayTracing.data.gameobject;

import org.boby.RayTracing.data.Scene;
import org.boby.RayTracing.data.light.Light;
import org.boby.RayTracing.data.light.LightType;
import org.joml.Vector3f;

public class LightGameObject extends GameObject{
	Light light;
	int lightId;
	
	@Override
	public void register(Scene scene) {
		super.register(scene);
		
		lightId = scene.registerLight(light);
	}
	
	public LightGameObject(Light light) {
		super();
		this.light = light;
	}
	
	public LightGameObject(LightType type, Vector3f color, float intensity) {
		super();
		this.light = new Light(type, color, intensity);
	}
	
	public Light getLight() {
		return light;
	}
	
}
