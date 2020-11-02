package org.cdnomlqko.jglutil.data;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

import org.cdnomlqko.jglutil.shader.Shader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

/**
 * This class holds the properties of a camera
 * @author CDnoMlqko
 *
 */
public class Camera {
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	private int uboMatrices;
	
	private float ASPECT;
	
	private float FOV;

	private final float Z_NEAR;

	private final float Z_FAR;
	
	/**
	 * Creates a camera
	 * @param FOV - field of view
	 * @param ASPECT - aspect ratio width / height
	 * @param Z_NEAR - near clip plane
	 * @param Z_FAR - far clip plane
	 */
	public Camera(float FOV, float ASPECT, float Z_NEAR, float Z_FAR) {
		this.FOV = FOV;
		this.ASPECT = ASPECT;
		this.Z_NEAR = Z_NEAR;
		this.Z_FAR = Z_FAR;
		
		projectionMatrix = new Matrix4f().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
		viewMatrix = new Matrix4f();
	}
	
	/**
	 * Updates the camera's projection matrix
	 */
	public void UpdateProjectionMatrix() {
		projectionMatrix.identity().setPerspective(this.FOV, this.ASPECT, this.Z_NEAR, this.Z_FAR);
	}
	
	/**
	 * Updates the camera view matrix
	 * @param transform - {@link Transformation} to use 
	 */
	public void updateViewMatrix(Transformation transform) {
		viewMatrix.translation(transform.getPosition())
			.rotateZ(-transform.getRotation().z)
			.rotateY(-transform.getRotation().y)
			.rotateX(-transform.getRotation().x)
			.scale(transform.getScale()).invert();
	}
	
	/**
	 * Creates a UBO containing the camera's world, view and projection matrices.
	 */
	public void createMatricesUBO() {
		uboMatrices = glGenBuffers();
	    
	    glBindBuffer(GL_UNIFORM_BUFFER, uboMatrices);
	    glBufferData(GL_UNIFORM_BUFFER, BufferUtils.createFloatBuffer(3 * 16), GL_DYNAMIC_DRAW);
	    glBindBuffer(GL_UNIFORM_BUFFER, 0);
	    
	    Shader.setUBO(uboMatrices, 0);
	}
	
	/**
	 * Updates the data in the UBO, created by {@link Camera#createMatricesUBO() createMatricesUBO()}.
	 * @param worldMatrix - world matrix of the camera. Must be taken from {@link Transformation#getWorldMatrix()} using the {@link Transformation} passed to {@link Camera#updateViewMatrix(Transformation)}
	 */
	public void updateMatricesUBO(Matrix4f worldMatrix) {
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
