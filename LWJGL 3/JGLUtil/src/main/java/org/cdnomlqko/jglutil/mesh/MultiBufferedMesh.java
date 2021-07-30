package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy;

import static org.lwjgl.opengl.GL46.*;

/**
 * A base class for 3d geometry. Creates and maintains a single VAO and multiple VBO-s. 
 * This class does not have in-depth JavaDoc. The concepts it implements are best described in the OpenGL Wiki.
 * @author CDnoMlqko
 *
 */
public class MultiBufferedMesh extends Mesh{
	
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

	protected ArrayList<VBO> vbos;
	
	/**
	 * initializes the mesh.
	 * @param drawMode - how the mesh will display. This must be one of GL_POINTS, GL_LINES, GL_LINE_STRIP, GL_TRIANGLES, etc...
	 */
	protected MultiBufferedMesh(int drawMode) {
		vbos = new ArrayList<VBO>();
		this.setDrawMode(drawMode);
	}
	
	protected int createVBO(int attributeLocation, int coordSize, FloatBuffer data) {
		verticesCount = data.limit();
		int bufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_READ);
        glVertexAttribPointer(attributeLocation, coordSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        int index = vbos.size();
        vbos.add(new VBO(attributeLocation, bufferId, coordSize));
        return index;
	}
	
	@Override
	public void delete() {
		deleteVAOandVBO();
		for(VBO buff: vbos) {
			glDeleteBuffers(buff.bufferId);
		}
	}
	
	@Override
	public void enableVBOs() {
		for(VBO vbo : vbos)
			glEnableVertexAttribArray(vbo.location);
	}
	
	@Override
	public void disableVBOs() {
		for(VBO vbo: vbos)
			glDisableVertexAttribArray(vbo.location);
	}
}
