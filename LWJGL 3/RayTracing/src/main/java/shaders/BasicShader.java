package shaders;

public class BasicShader extends Shader{

	private static final String VERTEX_FILE = "./res/shaders/BasicVertexShader.vs", FRAGMENT_FILE = "./res/shaders/BasicFragmentShader.fs";
	
	public BasicShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAllAttributes() {
		//super.bindAttribute(0, "position");
		//super.bindAttribute(1, "inColor");
	}

	@Override
	public void createUniforms() {
		try {
			super.createUniform("texture_sampler");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
