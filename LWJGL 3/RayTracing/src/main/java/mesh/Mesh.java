package mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
public class Mesh {
	
	private int VAOId;
	private int VBOId;
	private int indicesBufferId;
	private int vertexCount;
	private float vertices[];
	private int indices[];
	
	public Mesh(float[] vertices, int[] indices) {
		this.vertices = vertices;
		this.indices = indices;
		this.vertexCount = indices.length;
	}
	
	public void create()
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		VAOId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAOId);
		VBOId = GL30.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		indicesBufferId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public void remove()
	{
		GL30.glDeleteVertexArrays(VAOId);
		GL15.glDeleteBuffers(VBOId);
		GL15.glDeleteBuffers(indicesBufferId);
	}

	public int getVAOId() {
		return VAOId;
	}

	public int getVBOId() {
		return VBOId;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public float[] getVertices()
	{
		return vertices;
	}
}
