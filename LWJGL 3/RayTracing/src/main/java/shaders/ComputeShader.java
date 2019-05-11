package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.system.MemoryStack;

public abstract class ComputeShader {
	private int computeShaderID;
	private int programID;
	
	private String computeFile;
    
    private final HashMap<String, Integer> uniforms;
    
	public ComputeShader(String computeFile) {
		this.computeFile = computeFile;
		uniforms = new HashMap<String, Integer>();
	}
	/**
	 * creates the shader from the shader files, specified in the constructor
	 */
	public void create()
	{
		programID = GL20.glCreateProgram();
		
		computeShaderID = GL20.glCreateShader(GL46C.GL_COMPUTE_SHADER);
		GL20.glShaderSource(computeShaderID, readFile(computeFile));
		GL20.glCompileShader(computeShaderID);
		
		if(GL20.glGetShaderi(computeShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error: Fragment Shader - " + GL20.glGetShaderInfoLog(computeShaderID));
		}
		
		bindAllAttributes();
		
		GL20.glAttachShader(programID, computeShaderID);
		
		GL20.glLinkProgram(programID);
		if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error: Program Linking - \n" + GL20.glGetShaderInfoLog(programID,1024));
		}
		
		GL20.glValidateProgram(programID);
		if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error: Program Validation - \n" + GL20.glGetShaderInfoLog(programID,1024));
		}
		
		createUniforms();
	}
	/**
	 * use bindAttribute() to pass parameters to the shader
	 */
	protected abstract void bindAllAttributes();
	/**
	 * use this to pass parameters to the shader
	 * @param index - in which VBO is the data stored in
	 * @param location - the name of the shader parameter this data should be passed to
	 */
	protected void  bindAttribute(int index, String location) {
		GL20.glBindAttribLocation(programID, index, location);
	}
	
	/**
	 * in this method use createUniform() to create uniform variables for your shader
	 */
	protected abstract void createUniforms();
	
	/**
	 * Creates an uniform variable with the specified name if it exists in the GLSL shader code. 
	 * @param uniformName - the name of the uniform variable.
	 * @throws Exception if the uniform is not used or does not exist in the GLSL shader, an Exception is thrown.
	 */
	protected void createUniform(String uniformName) throws Exception {
	    int uniformLocation = GL20.glGetUniformLocation(programID,
	        uniformName);
	    if (uniformLocation < 0) {
	        throw new Exception("Could not find uniform: " +
	            uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to. 
	 */
	public void setUniform(String uniformName, int value) {
	    GL20.glUniform1i(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, Matrix4f value) {
	    // Dump the matrix into a float buffer
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
	    }
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, FloatBuffer value)
	{
		GL20.glUniform3fv(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, IntBuffer value)
	{
		GL20.glUniform3iv(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, Vector3f vec)
	{
		GL20.glUniform3f(uniforms.get(uniformName), vec.x, vec.y, vec.z);
	}
	
	public void setUniform(String uniformName, Vector2f vec) {
		GL20.glUniform2f(uniforms.get(uniformName), vec.x, vec.y);
	}
	
	/**
	 * TODO: write this
	 * @param uniformName
	 * @return
	 */
	public boolean hasUniform(String uniformName) {
		return uniforms.containsKey(uniformName);
	}
	
	/**
	 * the shader will be used after this line till it is either unbound or another shader is bound.
	 */
	public void bind()
	{
		GL20.glUseProgram(programID);
	}
	/**
	 * no shader will be used after this line till a shader is bound.
	 */
	public void unbind()
	{
		GL20.glUseProgram(0);
	}
	/**
	 * deletes the shader. It can be created again with create() and used normally after that.
	 */
	public void remove()
	{
		GL20.glDetachShader(programID, computeShaderID);
		GL20.glDeleteShader(computeShaderID);
		GL20.glDeleteProgram(programID);
	}
	/**
	 * a private method for reading text files
	 * @param file - the file path beginning with ./res/
	 * @return the file in the form of a String
	 */
	private String readFile(String file)
	{
		BufferedReader reader = null;
		StringBuilder string = new StringBuilder();
		try {
			String line;
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null)
			{
				string.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string.toString();
	}
}
