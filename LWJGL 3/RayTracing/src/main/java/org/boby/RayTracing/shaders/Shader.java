package org.boby.RayTracing.shaders;

import static org.lwjgl.opengl.GL46.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;


/**
 * <h1>Implements a simple Shader. Includes a Vertex Shader and a Fragment Shader. Compiles GLSL from source. Supports
 * Uniforms</h1>
 * 
 * @author Boby
 */
public abstract class Shader {
	private int vertexShaderID;
	private int fragmentShaderID;
	private int programID;
	
	private String vertexFile;
	private String fragmentFile;
    
    private final HashMap<String, Integer> uniforms;
    
	public Shader(String vertexFile, String fragmentFile) {
		this.vertexFile = vertexFile;
		this.fragmentFile = fragmentFile;
		uniforms = new HashMap<String, Integer>();
	}
	
	/**
	 * creates the shader from the shader files, specified in the constructor
	 */
	public void create()
	{
		programID = glCreateProgram();
		
		vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderID, readFile(vertexFile));
		glCompileShader(vertexShaderID);
		
		if(glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Error: Vertex Shader - " + glGetShaderInfoLog(vertexShaderID));
		}
		
		fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderID, readFile(fragmentFile));
		glCompileShader(fragmentShaderID);
		
		if(glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Error: Fragment Shader - " + glGetShaderInfoLog(fragmentShaderID));
		}
		
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Linking - \n" + glGetShaderInfoLog(programID,1024));
		}
		
		glValidateProgram(programID);
		if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Validation - \n" + glGetShaderInfoLog(programID,1024));
		}
		
		createUniforms();
	}
	
	/**
	 * in this method use createUniform() to create uniform variables for your
	 * shader
	 */
	protected abstract void createUniforms();
	
	/**
	 * Creates an uniform variable with the specified name if it exists in the GLSL
	 * shader code.
	 * 
	 * @param uniformName
	 *            - the name of the uniform variable.
	 * @throws Exception
	 *             if the uniform is not used or does not exist in the GLSL shader,
	 *             an Exception is thrown.
	 */
	protected void createUniform(String uniformName) {
	    int uniformLocation = glGetUniformLocation(programID,
	        uniformName);
	    if (uniformLocation < 0) {
	        throw new RuntimeException("Could not find uniform:" +
	            uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	
	/**
	 * Get the UniformLication.
	 * @param uniformName
	 * @return 
	 * 
	 * @throws RuntimeException if the uniform does not exist, is not used or has not been created.
	 */
	int getUniformId(String uniformName) {
		if(!this.hasUniform(uniformName)) {
			throw new RuntimeException("the uniform " + uniformName + " does not exist in this shader.");
		}
		int id = uniforms.get(uniformName);
		return id;
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public void setUniform(String uniformName, int value) {
	    glUniform1i(getUniformId(uniformName), value);
	}
    
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public void setUniform(String uniformName, Matrix4f value) {
	    // Dump the matrix into a float buffer
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        glUniformMatrix4fv(getUniformId(uniformName), false, fb);
	    }
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public void setUniform(String uniformName, FloatBuffer value)
	{
		glUniform3fv(getUniformId(uniformName), value);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public void setUniform(String uniformName, IntBuffer value)
	{
		glUniform3iv(getUniformId(uniformName), value);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public void setUniform(String uniformName, Vector3f vec)
	{
		glUniform3f(getUniformId(uniformName), vec.x, vec.y, vec.z);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as getUniformId
	 */
	public boolean hasUniform(String uniformName) {
		return uniforms.containsKey(uniformName);
	}
	
	/**
	 * the shader will be used after this line till it is either unbound or another
	 * shader is bound.
	 */
	public void bind()
	{
		glUseProgram(programID);
	}
	
	/**
	 * no shader will be used after this line till a shader is bound.
	 */
	public void unbind()
	{
		glUseProgram(0);
	}
	
	/**
	 * deletes the shader. It can be created again with create() and used normally
	 * after that.
	 */
	public void delete()
	{
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}
	
	/**
	 * a private method for reading text files
	 * 
	 * @param file
	 *            - file path
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
			e.printStackTrace();
		}
		return string.toString();
	}
}
