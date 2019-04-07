package org.boby.RayTracing.materials;

import shaders.BasicShader;
import shaders.RayTracingShader;
import shaders.Shader;

public class Material {
	Shader shader;
	public Material() {
		shader = new RayTracingShader();//TODO: make this a setting
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
