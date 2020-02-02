package org.boby.RayTracing.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

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
		programID = GL20.glCreateProgram();
		
		vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertexShaderID, readFile(vertexFile));
		GL20.glCompileShader(vertexShaderID);
		
		if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error: Vertex Shader - " + GL20.glGetShaderInfoLog(vertexShaderID));
		}
		
		fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragmentShaderID, readFile(fragmentFile));
		GL20.glCompileShader(fragmentShaderID);
		
		if(GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Error: Fragment Shader - " + GL20.glGetShaderInfoLog(fragmentShaderID));
		}
		
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
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
	
	protected abstract void createUniforms();
	
	protected void createUniform(String uniformName) {
	    int uniformLocation = GL20.glGetUniformLocation(programID,
	        uniformName);
	    if (uniformLocation < 0) {
	        throw new RuntimeException("Could not find uniform:" +
	            uniformName);
	    }
	    uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, int value) {
	    GL20.glUniform1i(uniforms.get(uniformName), value);
	}
    
	public void setUniform(String uniformName, Matrix4f value) {
	    // Dump the matrix into a float buffer
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        FloatBuffer fb = stack.mallocFloat(16);
	        value.get(fb);
	        GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
	    }
	}
	
	public void setUniform(String uniformName, FloatBuffer value)
	{
		GL20.glUniform3fv(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, IntBuffer value)
	{
		GL20.glUniform3iv(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, Vector3f vec)
	{
		GL20.glUniform3f(uniforms.get(uniformName), vec.x, vec.y, vec.z);
	}
	
	public boolean hasUniform(String uniformName) {
		return uniforms.containsKey(uniformName);
	}
	
	public void bind()
	{
		GL20.glUseProgram(programID);
	}
	
	public void unbind()
	{
		GL20.glUseProgram(0);
	}
	
	public void delete()
	{
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
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
