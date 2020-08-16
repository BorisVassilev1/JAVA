package org.boby.RayTracing.mesh;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * Contains the geometry of a 3d object. 
 * 
 * @author Boby
 *
 */
public class BasicMesh extends MeshBase {
	
	private int VAOID;
	private int vertexBufferID;
	private int indicesBufferID;
	private int texCoordsBufferID;
	private int colorBufferID;
	private int vertexCount;
	private float vertices[];
	private int indicesCount;
	private int indices[];
	private float texCoords[];
	private float colors[];

	public BasicMesh(float[] vertices, int[] indices, float[] colors, float[] texCoords) {
		this.vertices = vertices;
		this.indices = indices;
		this.indicesCount = indices.length;
		this.vertexCount = vertices.length;
		this.texCoords = texCoords;
		this.colors = colors;
	}

	public void create() {
		VAOID = super.createVertexArray();
		indicesBufferID = super.bindIndicesBuffer(indices);
		vertexBufferID = super.storeData(0, 3, vertices);
		colorBufferID = super.storeData(1, 3, colors);
		texCoordsBufferID = super.storeData(2, 2, texCoords);
		indicesCount = indices.length;
		vertexCount = vertices.length;
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteVertexArrays(VAOID);
		glDeleteBuffers(vertexBufferID);
		glDeleteBuffers(indicesBufferID);
		glDeleteBuffers(texCoordsBufferID);
		glDeleteBuffers(colorBufferID);
	}

	public int getVAOID() {
		return VAOID;
	}

	public int getVertexBufferID() {
		return vertexBufferID;
	}

	public int getIndicesBufferID() {
		return indicesBufferID;
	}

	public int getTexCoordsBufferID() {
		return texCoordsBufferID;
	}

	public int getColorBufferID() {
		return colorBufferID;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public FloatBuffer getVertexBuffer() {
		FloatBuffer toReturn = BufferUtils.createFloatBuffer(vertexCount);
		glGetBufferSubData(GL_ARRAY_BUFFER, 0, toReturn);
		return toReturn;
	}

	public IntBuffer getIndicesBuffer() {
		IntBuffer toReturn = BufferUtils.createIntBuffer(indicesCount);
		glGetBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, toReturn);
		return toReturn;
	}
}
