package org.boby.RayTracing.shaders;

public class RayMarchingComputeShader extends ComputeShader {
	
	private static final String COMPUTE_FILE = "./res/shaders/compute_shaders/RayMarchingShader.comp";
	
	public RayMarchingComputeShader() {
		super(COMPUTE_FILE);
	}

	@Override
	protected void createUniforms() {
		super.createUniform("img_output");
		super.createUniform("resolution");
	}
}
