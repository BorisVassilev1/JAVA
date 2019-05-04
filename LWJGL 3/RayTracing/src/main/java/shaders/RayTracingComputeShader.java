package shaders;

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
		// TODO Auto-generated method stub
		
	}

}
