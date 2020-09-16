package org.boby.RayTracing.rendering;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL43.*;

import java.nio.FloatBuffer;

import org.boby.RayTracing.shaders.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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
	
	Vector3f position;

	Vector3f rotation;
	
	public Camera(float FOV, float ASPECT, float Z_NEAR, float Z_FAR) {
		this.FOV = FOV;
		this.ASPECT = ASPECT;
		this.Z_NEAR = Z_NEAR;
		this.Z_FAR = Z_FAR;
		
		projectionMatrix = new Matrix4f().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
		viewMatrix = new Matrix4f();
		
		position = new Vector3f();
		rotation = new Vector3f();
	}
	
	public void UpdateProjectionMatrix() {
		projectionMatrix.identity().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
	}
	
	public void UpdateViewMatrix() {
		viewMatrix.identity()
				.rotateX(this.rotation.x)
				.rotateY(this.rotation.y)
				.rotateZ(this.rotation.z)
				.translate(new Vector3f(this.position).mul(-1));
	}
	
	public void CreateMatricesUBO() {
		uboMatrices = glGenBuffers();
	    
	    glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices);
	    glBufferData(GL_UNIFORM_BUFFER, BufferUtils.createFloatBuffer(2 * 16), GL_STATIC_DRAW);
	    glBindBuffer(GL_UNIFORM_BUFFER, 0);
	    
	    Shader.setUBO(uboMatrices, 0);
	}
	
	public void UpdateMatricesUBO() {
		glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices);
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buff1 = stack.mallocFloat(16);
			projectionMatrix.get(buff1);
			glBufferSubData(GL_UNIFORM_BUFFER, 0, buff1);
			
			FloatBuffer buff2 = stack.mallocFloat(16);
			viewMatrix.get(buff2);
			glBufferSubData(GL_UNIFORM_BUFFER, 16 * 4, buff2);
		}
		glBindBuffer(GL_UNIFORM_BUFFER, 0);
	}
	
	public float getFov() {
		return FOV;
	}

	public void setFov(float fOV) {
		FOV = fOV;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
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

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getZFar() {
		return Z_FAR;
	}

	
}
