package org.boby.RayTracing.materials;

import shaders.BasicShader;
import shaders.Shader;
import shaders.TextureOnScreenShader;

public class Material {
	Shader shader;
	public Material() {
		shader = new TextureOnScreenShader();
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
	public void setShader(Shader a) {
		this.shader = a;
	}
	
}
