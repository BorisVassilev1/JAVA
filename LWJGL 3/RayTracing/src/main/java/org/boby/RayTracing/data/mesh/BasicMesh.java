package org.boby.RayTracing.data.mesh;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * Contains the geometry of a 3d object. 
 * 
 * @author Boby
 *
 */
public class BasicMesh extends Mesh {
	public BasicMesh(FloatBuffer vertices, FloatBuffer normals, IntBuffer indices) {
		super();
		super.createVAO();
		super.bindVAO();
		super.createIBO(indices);
		super.createVBO(0, 3, vertices);
		super.createVBO(1, 3, normals);
		super.unbindVAO();
	}
}
