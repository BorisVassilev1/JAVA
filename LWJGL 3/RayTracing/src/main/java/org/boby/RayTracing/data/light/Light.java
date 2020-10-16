package org.boby.RayTracing.data.light;

import java.nio.ByteBuffer;

import org.boby.RayTracing.data.GameObjectProperty;
import org.joml.Vector3f;


public class Light implements GameObjectProperty{
	float intensity;
	Vector3f color;
	LightType type;
	
	public Light(LightType type, Vector3f color, float intensity) {
		this.type = type;
		this.color = color;
		this.intensity = intensity;
	}

	public Light() {}
	
	@Override
	public int getSize() {
		return 32;
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		buff.putFloat(color.x);
		buff.putFloat(color.y);
		buff.putFloat(color.z);
		
		//buff.putFloat(value)
		//buff.put(f)
	}
}
