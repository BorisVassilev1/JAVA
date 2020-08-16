package org.boby.RayTracing.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46.*;

/*
 * Contains base functonality of the Mesh class. Just a black box for now.
 */
public class MeshBase {
	
	protected int createVertexArray() {
        int vertexArrayID = glGenVertexArrays();
        glBindVertexArray(vertexArrayID);
        return vertexArrayID;
    }
     
    protected int storeData(int attributeNumber, int coordSize, float[] data) {
    	FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        int bufferID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_READ);
        glVertexAttribPointer(attributeNumber, coordSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return bufferID;
    }
     
    protected int bindIndicesBuffer(int[] indices) {
    	IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        int indicesBufferID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesBufferID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_READ);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return indicesBufferID;
    }
}
