package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy;
import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy.AABB;
import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy.Node;
import org.cdnomlqko.jglutil.data.Triangle;
import org.lwjgl.opengl.GL46;
import org.omg.CORBA.portable.ValueOutputStream;

/**
 * Contains the geometry of a 3d object. 
 * Contains the following mesh data:
 *  - vertex positions
 *  - indices
 *  - vertex normals
 *  - vertex colors
 *  - texture coordinates
 * 
 * @author CDnoMlqko
 *
 */
public class BasicMesh extends Mesh {
	
	/**
	 * Constructor
	 * @param vertices - vertex positions
	 * @param normals - vertex normals
	 * @param indices - indices
	 * @param texCoords - texture coordinates
	 * @param colors - vertex colors
	 */
	public BasicMesh(FloatBuffer vertices, FloatBuffer normals, IntBuffer indices, FloatBuffer texCoords, FloatBuffer colors) {
		super(GL46.GL_TRIANGLES);
		super.createVAO();
		super.bindVAO();
		super.createIBO(indices);
		super.createVBO(0, 3, vertices);
		super.createVBO(1, 3, normals);
		super.createVBO(2, 2, texCoords);
		super.createVBO(3, 4, colors);
		super.unbindVAO();
	}
	
	public VBO getVertices() {
		return vbos.get(0);
	}
	
	public VBO getNormals() {
		return vbos.get(1);
	}
	
	public VBO getTexCoords() {
		return vbos.get(2);
	}
	
	public VBO getColors() {
		return vbos.get(3);
	}
}
