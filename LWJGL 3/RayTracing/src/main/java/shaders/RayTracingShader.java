package shaders;

public class RayTracingShader extends Shader{
	
	private static final String VERTEX_FILE = "./res/shaders/RayTracingVertexShader.vs", FRAGMENT_FILE = "./res/shaders/RayTracingFragmentShader.fs";
	
	public RayTracingShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAllAttributes() {
		// TODO: delet this
		
	}

	@Override
	protected void createUniforms() {
		try {
			super.createUniform("projectionMatrix");
			super.createUniform("worldMatrix");
			//super.createUniform("texture_sampler");
			super.createUniform("vertexArray");
			super.createUniform("indicesArray");
			super.createUniform("ray00");
			super.createUniform("ray10");
			super.createUniform("ray01");
			super.createUniform("ray11");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
