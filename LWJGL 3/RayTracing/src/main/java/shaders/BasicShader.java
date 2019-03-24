package shaders;

public class BasicShader extends Shader{

	private static final String VERTEX_FILE = "src/main/java/shaders/BasicVertexShader.vs", FRAGMENT_FILE = "src/main/java/shaders/BasicFragmentShader.fs";
	
	public BasicShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	public void bindAllAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "inColor");
	}
}
