package shaders;

public class RayTracingComputeShader extends ComputeShader{

	private static final String COMPUTE_FILE = "./res/shaders/DeleteThisShaderLater.comp";
	
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
			//super.createUniform("cameraMatrix");
			//super.createUniform("fov");
			super.createUniform("deltaTime");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

}
