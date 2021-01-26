package org.cdnomlqko.jglutil.shader;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

/**
 * Compiles, links, provides uniform and SSBO support for shader orpgrams.
 * For now, it can work with vertex shaders, fragment shaders and compute shaders.
 * 
 * @author CDnoMlqko
 *
 */
public class Shader {
	private int programId;
	private int[] shaderIds;
	private String[] fileNames;
	
	private final HashMap<String, Integer> uniforms; // uniform name and uniform location
	private final HashMap<String, Integer> SSBOs; // ssbo name and ssbo binding
	private final HashMap<String, Integer> UBOs;
	
	/**
	 * Prepares the shader to work with file_count shader files.
	 * 
	 * @param file_count - how many shader source files will be used in the shader.
	 */
	protected Shader(int file_count) {
		uniforms = new HashMap<String, Integer>();
		SSBOs = new HashMap<String, Integer>();
		UBOs = new HashMap<String, Integer>();
		
		shaderIds = new int[file_count];
		fileNames = new String[file_count];
		
		programId = glCreateProgram();
	}
	
	/**
	 * Attaches, links the shader programs, then checks for errors, then creates the Uniforms and SSBOs the parser has found in the source code.
	 */
	protected void finishProgramCreation() {
		attachShaders();
		
		checkLinkStatus();
		
		checkValidateStatus();
		
		ShaderParser.getBlockUniforms(this);
		ShaderParser.getNonBlockUniforms(this);
		ShaderParser.getSSBOs(this);
	}
	
	/**
	 * Sets the shader's source files to the provided file names later to be compiled and used.
	 * @param source_files
	 */
	protected void setSourceFiles(String... source_files) {
		if(source_files.length != fileNames.length) {
			System.err.println("Error in shader creation: shader source files are too many or less than needed. ");
		}
		for(int i = 0; i < fileNames.length; i++) {
			fileNames[i] = source_files[i];
		}
	}
	
	/**
	 * Attaches all the shader programs.
	 */
	private void attachShaders() {
		for(int id: shaderIds) {
			glAttachShader(programId, id);
		}
	}
	
	/**
	 * Creates a shader program.
	 * @param type accepts GL_VERTEX_SHADER, GL_FRAGMENT_SHADER, GL_COMPUTE_SHADER
	 * @param target - which source file to use. This is the index of the file name, provided to {@link #setSourceFiles(String...)}.
	 */
	protected void createShader(int type, int target) {
		String file = fileNames[target];
		String source = ShaderParser.ParseShaderFile(file);
		
		int id = glCreateShader(type);
		glShaderSource(id, source);
		glCompileShader(id); 
		shaderIds[target] = id;
		
		checkCompileStatus(target);
	}
	
	/**
	 * Checks the shader for linking errors.
	 */
	private void checkLinkStatus() {
		glLinkProgram(programId);
		if(glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Linking - \n" + glGetShaderInfoLog(programId));
			System.err.println(glGetProgramInfoLog(programId));
		}
	}
	
	/**
	 * Checks the shader for validation errors.
	 */
	private void checkValidateStatus() {
		glValidateProgram(programId);
		if(glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
			System.err.println("Error: Program Validation - \n" + glGetShaderInfoLog(programId));
			System.err.println(glGetProgramInfoLog(programId));
		}
	}
	
	/**
	 * Checks the specified shader program for compilation errors.
	 * @param target - the shader program. This is the index of the surce file name, provided to {@link #setSourceFiles(String...)}
	 */
	private void checkCompileStatus(int target) {
		if(glGetShaderi(shaderIds[target], GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Error: Shader Compilation - " + fileNames[target] + " - " + glGetShaderInfoLog(shaderIds[target]));
		}
	}
	
	/**
	 * Creates an uniform variable with the specified name if it exists in the GLSL
	 * shader code.
	 * 
	 * @param uniformName
	 *            - the name of the uniform variable.
	 * @throws RuntimeException
	 *             if the uniform is not used or does not exist in the GLSL shader,
	 *             an Exception is thrown.
	 */
	protected void createUniform(String uniformName) {
		int uniformLocation = glGetUniformLocation(programId, uniformName);
		if (uniformLocation < 0) {
			throw new RuntimeException("Could not find uniform: " + uniformName);
		}
		registerUniform(uniformName, uniformLocation);
	}
	
	protected void registerUniform(String name, int location) {
		uniforms.put(name, location);
	}
	
	/**
	 * Get the Uniform Location.
	 * @param uniformName
	 * @return 
	 * 
	 * @throws RuntimeException if the uniform does not exist, is not used or has not been created.
	 */
	int getUniformLocation(String uniformName) {
		if(!this.hasUniform(uniformName)) {
			throw new RuntimeException("the uniform " + uniformName + " does not exist in this shader.");
		}
		int id = uniforms.get(uniformName);
		return id;
	}
	
	public void setUniform(String uniformName, boolean value) {
		glUniform1i(getUniformLocation(uniformName), value ? 1 : 0);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, int value) {
		glUniform1i(getUniformLocation(uniformName), value);
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, Matrix4f value) {
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(getUniformLocation(uniformName), false, fb);
		}
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, FloatBuffer value) {
		glUniform3fv(getUniformLocation(uniformName), value);
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, IntBuffer value) {
		glUniform3iv(getUniformLocation(uniformName), value);
	}
	
	public void setUniform(String uniformName, Vector4f value) {
		glUniform4f(getUniformLocation(uniformName), value.x, value.y, value.z, value.w);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(getUniformLocation(uniformName), value.x, value.y, value.z);
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, Vector2f value) {
		glUniform2f(getUniformLocation(uniformName), value.x, value.y);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, Vector2i value) {
		glUniform2i(getUniformLocation(uniformName), value.x, value.y);
	}
	
	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, float value) {
		glUniform1f(getUniformLocation(uniformName), value);
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public void setUniform(String uniformName, double value) {
		glUniform1d(getUniformLocation(uniformName), value);
	}

	/**
	 * Sets the specified uniform variable.
	 * 
	 * @param uniformName
	 *            - name of an already created uniform variable.
	 * @param value
	 *            - the value for the uniform variable to be set to.
	 * @throws RuntimeException just as {@link #getUniformLocation(String)}
	 */
	public boolean hasUniform(String uniformName) {
		return uniforms.containsKey(uniformName);
	}

	/**
	 * Creates a SSBO (Shader Storage Buffer Object) that can be used to pass data
	 * to the shader through a FloatBuffer
	 * 
	 * @param name
	 *            - the GLSL buffer where the data from the SSBO will be written
	 * @param binding
	 *            - where the SSBO will be bound to the shader storage block
	 * @return pointer to a float buffer from where the data will be written on
	 *         execution
	 */
	public void createSSBO(String name, int binding) { //https://www.geeks3d.com/20140704/tutorial-introduction-to-opengl-4-3-shader-storage-buffers-objects-ssbo-demo/
		SSBOs.put(name, binding);
		
		int block_index = glGetProgramResourceIndex(programId, GL_SHADER_STORAGE_BLOCK, name);
		// Sets the shader to look for the data in that buffer on the correct location.
		// Can be skipped if "layout (std430, binding=<something>)" is used
		glShaderStorageBlockBinding(programId, block_index, binding);
	}

	/**
	 * Binds the given buffer to the binding point, used by the SSBO with the given name.
	 * @param name - name of the SSBO in the shader
	 * @param bufferId - a valid OpenGL buffer pointer to be bound to that SSBO
	 */
	public void setSSBO(String name, int bufferId) {//https://www.geeks3d.com/20140704/tutorial-introduction-to-opengl-4-3-shader-storage-buffers-objects-ssbo-demo/
		int binding_point_index = getSSBOBinding(name);
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, binding_point_index, bufferId);
	}
	
	/**
	 * Binds the given buffer to the given binding point to be used in a SSBO.
	 * @param bufferId - a valid pointer to a OpenGL buffer
	 * @param binding - the binding point of the SSBO that is to use the buffer
	 */
	public static void setSSBO(int bufferId, int binding) {
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, binding, bufferId);
	}
	
	/**
	 * Registers that UBO. Then it can be set with {@link #setUBO(String, int)}. Note that {@link #setUBO(int, int)} can be used even with UBO-s that have not been registered that way.
	 * @param name - the name of the SSBO in the Shader source
	 * @param binding - the binding of the buffer object. If it is set with layout(binding = X), this method overwrites that
	 */
	public void createUBO(String name, int binding) {
		UBOs.put(name, binding);
		
		int block_index = glGetUniformBlockIndex(programId, name);
		
		glUniformBlockBinding(programId, block_index, binding);
	}
	
	/**
	 * Binds the given UBO to use the given buffer. Uses the binding that is given to {@link #createUBO(String, int)}. Note that {@link #setUBO(int, int)} can be used for UBO-s that have not been registered.
	 * @param name - name of the UBO in the Shader source code
	 * @param bufferId - a valid OpenGL buffer pointer
	 */
	public void setUBO(String name, int bufferId) {
		int binding_point_index = getUBOBinding(name);
		glBindBufferBase(GL_UNIFORM_BUFFER, binding_point_index, bufferId);
	}
	
	/**
	 * Binds the given buffer to the given binding point so it can be used by a UBO, bound on the same binding point
	 * @param bufferId
	 * @param binding
	 */
	public static void setUBO(int bufferId, int binding) {
		glBindBufferBase(GL_UNIFORM_BUFFER, binding, bufferId);
	}
	
	/**
	 * @param name - name of the SSBO in the Shader source code
	 * @return the binding point of the SSBO with the given name.
	 * @throws RuntimeException if the SSBO has not been registered in this Shader.
	 */
	public int getSSBOBinding(String name) {
		Integer binding = SSBOs.get(name);
		if(binding == null) {
			throw new RuntimeException("This SSBO does not exist or has not been created: " + name);
		}
		return binding;
	}
	
	/**
	 * @param name - name of the UBO in the Shader source code
	 * @return the binding point of the UBO with the given name.
	 * @throws RuntimeException if the UBO has not been registered in this Shader.
	 */
	public int getUBOBinding(String name) {
		Integer binding = UBOs.get(name);
		if(binding == null) {
			throw new RuntimeException("This UBO does not exist or has not been created: " + name);
		}
		return binding;
	}
	
	public int getProgramId() {
		return this.programId;
	}
	
	/**
	 * The shader will be used after this line till it is either unbound or another.
	 * shader is bound.
	 */
	public void bind() {
		glUseProgram(programId);
	}

	/**
	 * No shader will be used after this line till a shader is bound.
	 */
	public void unbind() {
		glUseProgram(0);
	}

	/**
	 * Deletes the shader. It cannot be used again.
	 */
	public void delete() {
		for(int a: shaderIds) {
			glDetachShader(programId, a);
			glDeleteShader(a);
		}
		glDeleteProgram(programId);
	}
	
	@Override
	public String toString() {
		String s = "Shader: [ProgramId: " + programId + ", Uniforms: {";
		for(HashMap.Entry<String, Integer> entry: uniforms.entrySet()) {
			s += "{Name: " + entry.getKey() + ", Location: " + entry.getValue() + "}, ";
		}
		s += "}, SSBOs: {";
		for(HashMap.Entry<String, Integer> entry: SSBOs.entrySet()) {
			s += "{Name: " + entry.getKey() + ", Binding: " + entry.getValue() + "}, ";
		}
		s += "}, Files: {";
		for(String a: fileNames) {
			s += "\"" + a + "\"" + ", ";
		}
		s += "}]";
		return s;
	}
	
	
}
