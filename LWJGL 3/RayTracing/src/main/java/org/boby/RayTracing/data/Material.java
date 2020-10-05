package org.boby.RayTracing.data;

import org.boby.RayTracing.utils.Texture2D;
import org.joml.Vector3f;

public class Material  {
	
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
	
	
	
	
	// TODO: this must be sent to the shader. Note the need of GL_OFFSET query!!
}
