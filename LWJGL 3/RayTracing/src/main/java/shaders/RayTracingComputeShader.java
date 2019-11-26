package shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46.*;

public class RayTracingComputeShader extends ComputeShader{

	private static final String COMPUTE_FILE = "./res/shaders/RayTracingShader.comp";
	
	public RayTracingComputeShader() {
		super(COMPUTE_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAllAttributes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createUniforms() {
		try {
			super.createUniform("img_output");
			super.createUniform("resolution");
			super.createUniform("cameraMatrix");
			super.createUniform("fov");
			//super.createUniform("deltaTime");
			super.createSSBO("shader_data", 2);
			FloatBuffer buff = BufferUtils.createFloatBuffer(36);
			buff.put(new float[] {
					 0.5f, 1.3f, 1.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f,0.0f,0.0f,0.0f,
					 1.3f, 0.2f, 2.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.7f,0.0f,0.0f,0.0f,
					-0.4f, 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.5f, 0.0f, 0.5f,0.0f,0.0f,0.0f
					});
			buff.flip();
			super.setSSBO("shader_data", buff, GL_DYNAMIC_DRAW);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

}
