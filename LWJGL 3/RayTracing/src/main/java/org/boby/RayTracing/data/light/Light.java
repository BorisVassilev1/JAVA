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
		return 96;
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		buff.putFloat(offset + 0, color.x);
		buff.putFloat(offset + 4, color.y);
		buff.putFloat(offset + 8, color.z);
		buff.putFloat(offset + 12, intensity);
		buff.putInt(offset + 16, type.id);
	}
}
