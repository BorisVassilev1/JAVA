package shaders;

public class RayMarchingComputeShader extends ComputeShader {
	
	private static final String COMPUTE_FILE = "./res/shaders/RayMarchingShader.comp";
	
	public RayMarchingComputeShader() {
		super(COMPUTE_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createUniforms() {
		// TODO Auto-generated method stub
		try {
			super.createUniform("img_output");
			super.createUniform("resolution");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void bindAllAttributes() {
		// TODO Auto-generated method stub
		
	}

}
