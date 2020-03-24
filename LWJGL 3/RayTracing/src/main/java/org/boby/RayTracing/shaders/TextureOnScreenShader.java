package org.boby.RayTracing.shaders;

public class TextureOnScreenShader extends Shader{

	private static final String VERTEX_FILE = "./res/shaders/verfrag_shaders/TextureOnScreenVertexShader.vs", FRAGMENT_FILE = "./res/shaders/verfrag_shaders/TextureOnScreenFragmentShader.fs";
	
	public TextureOnScreenShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void createUniforms() {
			super.createUniform("texture_sampler");
	}

}
