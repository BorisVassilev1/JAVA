package org.boby.RayTracing.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

/**
 * 
 * Will be a better version of MeshBase
 * 
 * @author Boris
 *
 */
public class Mesh {
	
	public class VBO {
		private int location;
		private int bufferId;
		private int coordSize;
		public VBO(int location, int bufferId, int coordSize) {
			this.location = location;
			this.bufferId = bufferId;
			this.coordSize = coordSize;
		}
		
		public int getLocation() {
			return location;
		}
		
		public int getBufferId() {
			return bufferId;
		}
		
		public int getCoordSize() {
			return coordSize;
		}
	}
	
	private int vao;
	private int ibo;
	private ArrayList<VBO> vbos;
	
	private int indicesCount;
	private int verticesCount;
	
	protected Mesh() {
		vbos = new ArrayList<VBO>();
	}
	
	protected int createVBO(int attributeLocation, int coordSize, FloatBuffer data) {
		verticesCount = data.capacity();
		int bufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_READ);
        glVertexAttribPointer(attributeLocation, coordSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        int index = vbos.size();
        vbos.add(new VBO(attributeLocation, bufferId, coordSize));
        return index;
	}
	
	protected int createIBO(IntBuffer data) {
		indicesCount = data.capacity();
        int indicesBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_READ);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        ibo = indicesBufferId;
        return indicesBufferId;
    }
	
	protected int createVAO() {
        vao = glGenVertexArrays();
        return vao;
    }
	
	public void delete() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(ibo);
		for(VBO buff: vbos) {
			glDeleteBuffers(buff.bufferId);
		}
	}
	
	public void bind() {
		bindVAO();
		enableVBOs();
		bindIBO();
	}
	
	public void unbind() {
		unbindIBO();
		disableVBOs();
		unbindVAO();
	}
	
	public void bindVAO() {
		glBindVertexArray(vao);
	}
	
	public void unbindVAO() {
		glBindVertexArray(0);
	}
	
	public void enableVBOs() {
		for(VBO vbo : vbos)
			glEnableVertexAttribArray(vbo.location);
	}
	
	public void disableVBOs() {
		for(VBO vbo: vbos)
			glDisableVertexAttribArray(vbo.location);
	}
	
	public void bindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
	}
	
	public void unbindIBO() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public int getVerticesCount() {
		return verticesCount;
	}
}
