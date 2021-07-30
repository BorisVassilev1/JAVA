package org.cdnomlqko.jglutil.mesh;

import static org.lwjgl.opengl.GL46.*;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;

public abstract class Mesh {
	protected int vao;
	protected int ibo;
	
	protected int indicesCount;
	protected int verticesCount;
	
	protected int drawMode = GL_TRIANGLES;
	
	/**
	 * Creates the VAO for the mesh.
	 * @return a pointer to the VAO
	 */
	protected int createVAO() {
        vao = glGenVertexArrays();
        return vao;
    }
	
	/**
	 * Creates the Index Buffer Object (IBO) for that mesh. Do not call this twice unless you want a memory leak on the GPU
	 * @param data - Indices for the mesh
	 * @return pointer to a openGL buffer, created by {@link GL46#glGenBuffers() glGenBuffers()}
	 */
	protected int createIBO(IntBuffer data) {
		indicesCount = data.limit();
        int indicesBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_READ);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        ibo = indicesBufferId;
        return indicesBufferId;
    }
	
	/**
	 * Deletes everything. You should call {@link #deleteVAOandVBO()} here.
	 */
	public abstract void delete();
	
	protected void deleteVAOandVBO() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(ibo);
	}
	
	/**
	 * Binds the mesh for rendering
	 */
	public void bind() {
		bindVAO();
		enableVBOs();
		bindIBO();
	}
	
	/**
	 * Disables everything so that something else may be rendered later
	 */
	public void unbind() {
		unbindIBO();
		disableVBOs();
		unbindVAO();
	}
	
	/**
	 * Binds the mesh's VAO for drawing
	 */
	public void bindVAO() {
		glBindVertexArray(vao);
	}
	
	/**
	 * Unbinds the mesh's VAO
	 */
	public void unbindVAO() {
		glBindVertexArray(0);
	}
	
	/**
	 * Enables all the mesh's vbos for drawing
	 */
	public abstract void enableVBOs();
	
	/**
	 * Disables all the mesh's vbos after drawing
	 */
	public abstract void disableVBOs();
	
	/**
	 * binds the IBO buffer to {@link GL46#GL_ELEMENT_ARRAY_BUFFER}
	 */
	public void bindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	/**
	 * unbinds the IBO buffer from {@link GL46#GL_ELEMENT_ARRAY_BUFFER}
	 */
	public void unbindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public int getVerticesCount() {
		return verticesCount;
	}

	public int getDrawMode() {
		return drawMode;
	}

	/**
	 * determines how the mesh will be drawn
	 * @param drawMode - {@link GL46#GL_TRIANGLES}, {@link GL46#GL_LINES} or any of the sort
	 */
	public void setDrawMode(int drawMode) {
		this.drawMode = drawMode;
	}
	
	/**
	 * 
	 * @return a pointer to the VAO buffer
	 */
	public int getVao() {
		return vao;
	}

	/**
	 * 
	 * @return a pointer to the IBO buffer
	 */
	public int getIbo() {
		return ibo;
	}
}
