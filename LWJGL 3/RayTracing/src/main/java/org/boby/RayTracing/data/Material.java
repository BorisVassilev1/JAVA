package org.boby.RayTracing.data;

import java.nio.ByteBuffer;

import org.boby.RayTracing.utils.Texture2D;
import org.joml.Vector3f;

public class Material implements GameObjectProperty{
	
	Vector3f color;
	boolean use_texture;
	
	int texture_id;
	
	float diffuse_power;
	float specular_power;
	float specular_exponent;
	
	public Material(Texture2D texture) {
		this.color = null;
		this.texture_id = texture.getID();
		this.use_texture = true;
		this.diffuse_power = 1.0f;
		this.specular_power = 1.0f;
		this.specular_exponent = 20.0f;
	}
	
	public Material(Vector3f color) {
		this.color = color;
		this.texture_id = 0;
		this.use_texture = true;
		this.diffuse_power = 1.0f;
		this.specular_power = 1.0f;
		this.specular_exponent = 20.0f;
	}
	
	public Material() {
	}

	@Override
	public void writeToBuffer(ByteBuffer buff, int offset) {
		buff.putFloat(color.x);
		buff.putFloat(color.y);
		buff.putFloat(color.z);
		
		//buff.put(use_texture);
		buff.putFloat(0f);
		buff.putFloat(0f);
		buff.putFloat(0f);
		buff.putFloat(0f);
		
	}

	@Override
	public int getSize() {
		return 32;
	}
}
