package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
	 * @param texCoords - textute coordinates
	 * @param colors - vertex colors
	 */
	public BasicMesh(FloatBuffer vertices, FloatBuffer normals, IntBuffer indices, FloatBuffer texCoords, FloatBuffer colors) {
		super();
		super.createVAO();
		super.bindVAO();
		super.createIBO(indices);
		super.createVBO(0, 3, vertices);
		super.createVBO(1, 3, normals);
		super.createVBO(2, 2, texCoords);
		super.createVBO(3, 4, colors);
		super.unbindVAO();
	}
}
