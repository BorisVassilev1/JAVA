package shaders;

public class BasicShader extends Shader{

	private static final String VERTEX_FILE = "", FRAGMENT_FILE = "";
	
	public BasicShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAllAttributes() {
		super.bindAttribute(0, "vertices");
	}
}
