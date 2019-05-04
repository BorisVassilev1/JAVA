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
		try {
			super.createUniform("img_output");
			super.createUniform("resolution");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> 6c583498aff69d780224ff0418b66f5de5feae54
		// TODO Auto-generated method stub
>>>>>>> 6c583498aff69d780224ff0418b66f5de5feae54
		
	}

}
