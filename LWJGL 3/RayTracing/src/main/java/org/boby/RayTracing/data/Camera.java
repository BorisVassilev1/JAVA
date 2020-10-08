package org.boby.RayTracing.data;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

import java.nio.FloatBuffer;

import org.boby.RayTracing.shaders.Shader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

public class Camera {
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	int uboMatrices;
	
	float ASPECT;
	
	float FOV;

	final float Z_NEAR;

	final float Z_FAR;
	
	public Camera(float FOV, float ASPECT, float Z_NEAR, float Z_FAR) {
		this.FOV = FOV;
		this.ASPECT = ASPECT;
		this.Z_NEAR = Z_NEAR;
		this.Z_FAR = Z_FAR;
		
		projectionMatrix = new Matrix4f().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
		viewMatrix = new Matrix4f();
	}
	
	public void UpdateProjectionMatrix() {
		projectionMatrix.identity().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
	}
	
	public void updateViewMatrix(Matrix4f worldMatrix) {
		worldMatrix.invert(viewMatrix);
	}
	
	
	public void CreateMatricesUBO() {
		uboMatrices = glGenBuffers();
	    
	    glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices);
	    glBufferData(GL_UNIFORM_BUFFER, BufferUtils.createFloatBuffer(3 * 16), GL_DYNAMIC_DRAW);
	    glBindBuffer(GL_UNIFORM_BUFFER, 0);
	    
	    Shader.setUBO(uboMatrices, 0);
	}
	
	public void UpdateMatricesUBO(Matrix4f worldMatrix) {
		glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices);
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buff = stack.mallocFloat(16);
			projectionMatrix.get(buff);
			glBufferSubData(GL_UNIFORM_BUFFER, 0, buff);
			
			viewMatrix.get(buff);
			glBufferSubData(GL_UNIFORM_BUFFER, 16 * 4, buff);
			
			worldMatrix.get(buff);
			glBufferSubData(GL_UNIFORM_BUFFER, 2 * 16 * 4, buff);
		}
		glBindBuffer(GL_UNIFORM_BUFFER, 0);
	}
	
	public float getFov() {
		return FOV;
	}

	public void setFov(float fOV) {
		FOV = fOV;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public float getAspect() {
		return ASPECT;
	}
	
	public void setAspect(float aspect) {
		this.ASPECT = aspect;
	}

	public float getZNear() {
		return Z_NEAR;
	}

	public float getZFar() {
		return Z_FAR;
	}
}
