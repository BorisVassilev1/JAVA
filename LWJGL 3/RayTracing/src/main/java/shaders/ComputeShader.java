package shaders;

public class ComputeShader extends Shader{

	private static final String COMPUTE_FILE = "./res/shaders/ComputeShader.cs";
	
	public ComputeShader() {
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
		try {
			//super.createUniform("projectionMatrix");
			//super.createUniform("worldMatrix");
//			super.createUniform("ray00");
//			super.createUniform("ray10");
//			super.createUniform("ray01");
//			super.createUniform("ray11");
//			super.createUniform("eye");
			super.createUniform("roll");
			super.createUniform("destTex");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
