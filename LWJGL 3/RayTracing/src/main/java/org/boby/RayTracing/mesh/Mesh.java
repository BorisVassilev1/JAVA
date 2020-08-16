package org.boby.RayTracing.mesh;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL46.*;

/**
 * 
 * Will be a better version of MeshBase
 * 
 * @author Boris
 *
 */
public class Mesh {
	
	int vao;
	ArrayList<Integer> vbos;
	
	public Mesh() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		
		// TODO: finish this.
	}
	
	protected int createVertexArray() {
        int vertexArrayID = glGenVertexArrays();
        glBindVertexArray(vertexArrayID);
        return vertexArrayID;
    }
	
	public void bindVAO() {
		glBindVertexArray(vao);
	}
	public void enableVBOs() {
		for(int i : vbos) {
			glEnableVertexAttribArray(i);//TODO: this is not right
		}
	}
	
}
