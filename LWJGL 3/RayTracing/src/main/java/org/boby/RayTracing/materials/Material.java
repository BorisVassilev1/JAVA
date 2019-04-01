package org.boby.RayTracing.materials;

import shaders.BasicShader;
import shaders.Shader;

public class Material {
	Shader shader;
	Shader computeShader;
	public Material() {
		shader = new BasicShader();
		shader.create();
		//computeShader = new ComputeShader();
		//computeShader.createComputeShader();
	}
	public Shader getShader()
	{
		return shader;
		//return computeShader;
	}
	public void remove()
	{
		shader.remove();
	}
}
