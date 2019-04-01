package org.boby.RayTracing.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh extends MeshBase{
	private int VAOID;
	private int vertexBufferID;
	private int indicesBufferID;
	private int texCoordsBufferID;
	private int colorBufferID;
	private int vertexCount;
	private int indicesCount;
	private float vertices[];
	private int indices[];
	private float texCoords[]; 
	private float colors[];
	
	public Mesh(float[] vertices, int[] indices, float[] colors, float[] texCoords) {
		this.vertices = vertices;
		this.indices = indices;
		this.vertexCount = indices.length;
		this.texCoords = texCoords;
		this.colors = colors;
	}
	
	public void create()
	{
    	VAOID = super.createVertexArray();
    	indicesBufferID = super.bindIndicesBuffer(indices);
        vertexBufferID = super.storeData(0, 3, vertices);
        colorBufferID = super.storeData(1, 3, colors);
        texCoordsBufferID = super.storeData(2, 2, texCoords);
		indicesCount = indices.length;
		vertexCount = vertices.length;
        GL30.glBindVertexArray(0);
	}
	
	public void remove()
	{
		GL30.glDeleteVertexArrays(VAOID);
		GL15.glDeleteBuffers(vertexBufferID);
		GL15.glDeleteBuffers(indicesBufferID);
		GL15.glDeleteBuffers(texCoordsBufferID);
		GL15.glDeleteBuffers(colorBufferID);
	}

	public int getVAOID() {
		return VAOID;
	}

	public int getVertexBufferID() {
		return vertexBufferID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getIndicesBufferID()
	{
		return indicesBufferID;
	}
	
	public FloatBuffer getVerticesBuffer()
	{
		FloatBuffer buff = BufferUtils.createFloatBuffer(vertexCount);
		GL20.glGetBufferSubData(GL20.GL_ARRAY_BUFFER, 0, buff);
		return buff;
	}
	
	public IntBuffer getIndicesBuffer()
	{
		IntBuffer buff = BufferUtils.createIntBuffer(indicesCount);
		GL20.glGetBufferSubData(GL20.GL_ELEMENT_ARRAY_BUFFER, 0, buff);
		return buff;
	}

	public int getIndicesCount() {
		// TODO Auto-generated method stub
		return indicesCount;
	}
}