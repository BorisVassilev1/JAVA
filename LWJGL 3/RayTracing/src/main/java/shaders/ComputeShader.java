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
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL46.*;

public abstract class ComputeShader {
	private int computeShaderID;
	private int programID;
	
	private int ssboID;
	
	
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
		programID = glCreateProgram();
		
		computeShaderID = glCreateShader(GL_COMPUTE_SHADER);
		glShaderSource(computeShaderID, readFile(computeFile));
		glCompileShader(computeShaderID);
		
		if(glGetShaderi(computeShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Error: Fragment Shader - " + glGetShaderInfoLog(computeShaderID));
		}
		
		bindAllAttributes();
		
		glAttachShader(programID, computeShaderID);
		
		glLinkProgram(programID);
		if(glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Linking - \n" + glGetShaderInfoLog(programID,1024));
		}
		
		glValidateProgram(programID);
		if(glGetProgrami(programID, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Validation - \n" + glGetShaderInfoLog(programID,1024));
		}
		
		
		createUniforms();
		
		createSSBO();//TODO: this shouldnt be here.
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
		glBindAttribLocation(programID, index, location);
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
	    int uniformLocation = glGetUniformLocation(programID,
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
	    glUniform1i(uniforms.get(uniformName), value);
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
	        glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
	    }
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, FloatBuffer value)
	{
		glUniform3fv(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, IntBuffer value)
	{
		glUniform3iv(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, Vector3f value)
	{
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, Vector2f value) {
		glUniform2f(uniforms.get(uniformName), value.x, value.y);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}
	/**
	 * Sets the specified uniform variable.
	 * @param uniformName - name of an already created uniform variable. Will throw a NullPointerException if the uniform has not been created.
	 * @param value - the value for the uniform variable to be set to.
	 */
	public void setUniform(String uniformName, double value) {
		glUniform1d(uniforms.get(uniformName), value);
	}
	
	/**
	 * Checks if the shader has a definition for a uniform.
	 * @param uniformName - the name of the searched uniform
	 * @return true if the uniform has already been defined, false if it is not.
	 */
	public boolean hasUniform(String uniformName) {
		return uniforms.containsKey(uniformName);
	}
	
	
	public int createSSBO() {
		System.out.println(glGetError());
		int ssbo = glGenBuffers();
		
		
		FloatBuffer buff = BufferUtils.createFloatBuffer(21);
		
		//Sphere(vec3(0.5,1.3,1.5),0.5 ,vec3(1.0,0.0,0.0)),
		//Sphere(vec3(1.3,0.2,2.0)  ,0.7 ,vec3(0.0,1.0,0.0)),
		//Sphere(vec3(-0.4,0.5,1.5) ,0.5 ,vec3(0.0,0.0,1.0)),
		//buff.put(1.0f);
		
		//buff.put(new float[] {0.5f, 1.3f, 1.5f, 0.5f, 1.0f, 0.0f, 0.0f});
		//buff.put(new float[] {1.3f, 0.2f, 2.0f, 0.7f, 0.0f, 1.0f, 0.0f});
		//buff.put(new float[] {-0.4f, 0.5f, 1.5f, 0.5f, 0.0f, 0.0f, 1.0f});
		
		/*buff.put(new float[] {
				0.5f, 1.3f, 1.5f, 0.5f, 1.0f, 0.0f, 0.0f,
				1.3f, 0.2f, 2.0f, 0.7f, 0.0f, 1.0f, 0.0f,
				-0.4f, 0.5f, 1.5f, 0.5f, 0.0f, 0.0f, 1.0f
				});*/
		
		buff.put(0.5f);
		buff.put(1.3f);
		buff.put(1.5f);
		buff.put(0.5f);
		buff.put(1.0f);
		buff.put(0.0f);
		buff.put(0.0f);
		
		buff.put(1.3f);
		buff.put(0.2f);
		buff.put(2.0f);
		buff.put(0.7f);
		buff.put(0.0f);
		buff.put(1.0f);
		buff.put(0.0f);
		
		buff.put(-0.4f);
		buff.put(0.5f);
		buff.put(1.5f);
		buff.put(0.5f);
		buff.put(0.0f);
		buff.put(0.0f);
		buff.put(1.0f);
		
		buff.flip();
		
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		System.out.println(glGetError());
		
		glBufferData(GL_SHADER_STORAGE_BUFFER, buff, GL_DYNAMIC_READ);
		System.out.println(glGetError());
		
		IntBuffer b = BufferUtils.createIntBuffer(1);
		glGetBufferParameteriv(GL_SHADER_STORAGE_BUFFER, GL_BUFFER_SIZE, b);
		System.out.println("buffer size: " + b.get(0));
		
		FloatBuffer a = BufferUtils.createFloatBuffer(21);
		glGetBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, a);
		System.out.println();
		for(int i = 0; i < a.capacity(); i++) {
			System.out.print(a.get(i) + " ");
		}System.out.println();
		
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		ssboID = ssbo;
		
		int block_index = glGetProgramResourceIndex(programID, GL_SHADER_STORAGE_BLOCK, "shader_data");
		System.out.println(block_index);
		
		int ssbo_binding_point_index = 2;
		glShaderStorageBlockBinding(programID, block_index, ssbo_binding_point_index);
		
		
		int binding_point_index = 2;
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, binding_point_index, ssbo);
		
		return ssbo;
	}
	
	
	/**
	 * the shader will be used after this line till it is either unbound or another shader is bound.
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
	 * deletes the shader. It can be created again with create() and used normally after that.
	 */
	public void remove()
	{
		glDetachShader(programID, computeShaderID);
		glDeleteShader(computeShaderID);
		glDeleteProgram(programID);
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
