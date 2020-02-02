package org.boby.RayTracing.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46.*;


public class RayTracingComputeShader extends ComputeShader{

	private static final String COMPUTE_FILE = "./res/shaders/compute_shaders/RayTracingShader.comp";
	
	public RayTracingComputeShader() {
		super(COMPUTE_FILE);
	}

	@Override
	protected void createUniforms() {
		super.createUniform("img_output");
		super.createUniform("resolution");
		super.createUniform("cameraMatrix");
		super.createUniform("fov");
		super.createSSBO("spheres", 2);
		FloatBuffer buff = BufferUtils.createFloatBuffer(36);
		buff.put(new float[] {
				 0.5f, 1.3f, 1.5f, 0.0f,	 1.0f, 0.0f, 0.0f, 1.0f,	 0.5f,0.0f,0.0f,0.0f,
				 1.3f, 0.2f, 2.0f, 0.0f,	 0.0f, 1.0f, 0.0f, 1.0f,	 0.7f,0.0f,0.0f,0.0f,
				-0.4f, 0.5f, 1.5f, 0.0f,	 0.0f, 0.0f, 1.0f, 1.0f,	 0.5f,0.0f,0.0f,0.0f
				});
		buff.flip();
		super.setSSBO("spheres", buff, GL_DYNAMIC_DRAW);
	}

}
