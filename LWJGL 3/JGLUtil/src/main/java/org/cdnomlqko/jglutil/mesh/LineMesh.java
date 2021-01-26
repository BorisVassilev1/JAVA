package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL46;
/**
 * Mesh that is composed of lines. It has only vertex positions and vertex colors
 * @author CDnoMlqko
 *
 */
public class LineMesh extends Mesh {

	protected LineMesh(FloatBuffer vertices, IntBuffer indices, FloatBuffer colors) {
		super(GL46.GL_LINES);
		super.createVAO();
		super.bindVAO();
		super.createIBO(indices);
		super.createVBO(0,  3, vertices);
		super.createVBO(3, 4, colors);
		super.unbindVAO();
	}

}
