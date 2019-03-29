package org.boby.RayTracing.materials;

import shaders.BasicShader;
import shaders.Shader;

public class Material {
	Shader shader;
	public Material() {
		shader = new BasicShader();
		shader.create();
	}
	public Shader getShader()
	{
		return shader;
	}
	public void remove()
	{
		shader.remove();
	}
}
