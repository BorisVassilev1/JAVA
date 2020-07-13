package org.boby.RayTracing.shaders;

public class BasicShader extends Shader{

	private static final String VERTEX_FILE = "./res/shaders/verfrag_shaders/BasicVertexShader.vs", FRAGMENT_FILE = "./res/shaders/verfrag_shaders/BasicFragmentShader.fs";
	
	public BasicShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void createUniforms() {
		super.createUniform("texture_sampler");
		super.createUniform("worldMatrix");
		super.createUniform("projectionMatrix");
		super.createUniform("viewMatrix");
	}
	
}
