package org.cdnomlqko.jglutil.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

/**
 * This class contains static methods for {@link Mesh} creation.
 * 
 * @author CDnoMlqko
 */
public class MeshUtils {
	
	private static BasicMesh cubeMesh;
	private static BasicMesh quadMesh;
	
	public static void init() {
		setCubeMesh(makeCube(1, 1, 1));
		setQuadMesh(makeQuad(1,1));
	}
	
	public static void delete() {
		cubeMesh.delete();
		quadMesh.delete();
	}
	
	/**
	 * Creates a Quad mesh with the described size on the XY plane.
	 * 
	 * @param sizex - size along the X axis
	 * @param sizey - size along the Y axis
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(float sizex, float sizey) {
		float sx = sizex, sy = sizey;
		return new BasicMesh(
				(FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[] {
					-sx, +sy, 0,
					+sx, +sy, 0,
					+sx, -sy, 0,
					-sx, -sy, 0
				}).flip(),
				(FloatBuffer) BufferUtils.createFloatBuffer(12).put(new float[] {
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f 
				})
						.flip(),
				(IntBuffer) BufferUtils.createIntBuffer(6).put(new int[] {
					0, 1, 2,
					0, 2, 3 
				}).flip(),
				(FloatBuffer) BufferUtils.createFloatBuffer(8).put(new float[] {
					0, 0,
					1, 0,
					1, 1,
					0, 1 
				}).flip(),
				(FloatBuffer) BufferUtils.createFloatBuffer(16).put(new float[] {
					1, 0, 0, 1,
					0, 1, 0, 1,
					0, 0, 1, 1,
					1, 1, 0, 1 })
				.flip());
	}

	/**
	 * {@link #makeQuad(float, float)}, but with a vector for size
	 * 
	 * @param size
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(float size) {
		return makeQuad(size, size);
	}

	/**
	 * {@link #makeQuad(float, float)}, but for squares
	 * 
	 * @param size
	 * @return the generated mesh
	 */
	public static BasicMesh makeQuad(Vector2f size) {
		return makeQuad(size.x, size.y);
	}

	public static BasicMesh makeCube(float size_x, float size_y, float size_z) {
		float sx = size_x / 2, sy = size_y / 2, sz = size_z / 2;
		BasicMesh toReturn = new BasicMesh((FloatBuffer) BufferUtils.createFloatBuffer(24*3).put(new float[] {
				 sx, -sy,  sz,
				-sx, -sy,  sz,
				-sx, -sy, -sz,
				-sx,  sy, -sz,
				-sx,  sy,  sz,
				 sx,  sy,  sz,
				 sx,  sy, -sz,
				 sx,  sy,  sz,
				 sx, -sy,  sz,
				 sx,  sy,  sz,
				-sx,  sy,  sz,
				-sx, -sy,  sz,
				-sx, -sy,  sz,
				-sx,  sy,  sz,
				-sx,  sy, -sz,
				 sx, -sy, -sz,
				-sx, -sy, -sz,
				-sx,  sy, -sz,
				 sx, -sy, -sz,
				 sx,  sy, -sz,
				 sx, -sy, -sz,
				 sx, -sy,  sz,
				-sx, -sy, -sz,
				 sx,  sy, -sz
		}).flip(),(FloatBuffer) BufferUtils.createFloatBuffer(24*3).put(new float[] {
				 0.0f, -1.0f,  0.0f,
				 0.0f, -1.0f,  0.0f,
				 0.0f, -1.0f,  0.0f,
				 0.0f,  1.0f,  0.0f,
				 0.0f,  1.0f,  0.0f,
				 0.0f,  1.0f,  0.0f,
				 1.0f,  0.0f,  0.0f,
				 1.0f,  0.0f,  0.0f,
				 1.0f,  0.0f,  0.0f,
				-0.0f,  0.0f,  1.0f,
				-0.0f,  0.0f,  1.0f,
				-0.0f,  0.0f,  1.0f,
				-1.0f, -0.0f, -0.0f,
				-1.0f, -0.0f, -0.0f,
				-1.0f, -0.0f, -0.0f,
				 0.0f,  0.0f, -1.0f,
				 0.0f,  0.0f, -1.0f,
				 0.0f,  0.0f, -1.0f,
				 0.0f, -1.0f,  0.0f,
				 0.0f,  1.0f,  0.0f,
				 1.0f,  0.0f,  0.0f,
				-0.0f,  0.0f,  1.0f,
				-1.0f, -0.0f, -0.0f,
				 0.0f,  0.0f, -1.0f
		}).flip(),(IntBuffer) BufferUtils.createIntBuffer(12*3).put(new int[] {
				 0 , 1 , 2 ,
				 3 , 4 , 5 ,
				 6 , 7 , 8 ,
				 9 , 10, 11,
				 12, 13, 14,
				 15, 16, 17,
				 18, 0 , 2 ,
				 19, 3 , 5 ,
				 20, 6 , 8 ,
				 21, 9 , 11,
				 22, 12, 14,
				 23, 15, 17 
		}).flip(), (FloatBuffer) BufferUtils.createFloatBuffer(24*2).put(new float[] {
				1.0f     , 0.333333f,
				1.0f     , 0.666667f,
				0.666667f, 0.666667f,
				1.0f     , 0.333333f,
				0.666667f, 0.333333f,
				0.666667f, 0.0f     ,
				0.0f     , 0.333333f,
				0.0f     , 0.0f     ,
				0.333333f, 0.0f     ,
				0.333333f, 0.0f     ,
				0.666667f, 0.0f     ,
				0.666667f, 0.333333f,
				0.333333f, 1.0f     ,
				0.0f     , 1.0f     ,
				0.0f     , 0.666667f,
				0.333333f, 0.333333f,
				0.333333f, 0.666667f,
				0.0f     , 0.666667f,
				0.666667f, 0.333333f,
				1.0f     , 0.0f     ,
				0.333333f, 0.333333f,
				0.333333f, 0.333333f,
				0.333333f, 0.666667f,
				0.0f     , 0.333333f
		}).flip(), (FloatBuffer) BufferUtils.createFloatBuffer(24*4).put(new float[] {
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f
		}).flip());
		
		return toReturn;
	}
	
	public static BasicMesh makeCube(float size) {
		return makeCube(size, size, size);
	}
	
	public static BasicMesh makeCube(Vector3f size) {
		return makeCube(size.x, size.y, size.z);
	}
	
	public static LineMesh makeLineCube(float size_x, float size_y, float size_z) {
		float sx = size_x/2, sy = size_y/2, sz = size_z/2;
		LineMesh toReturn = new LineMesh((FloatBuffer) BufferUtils.createFloatBuffer(8*3).put(new float[] {
				+sx, +sy, +sz,
				+sx, +sy, -sz,
				+sx, -sy, +sz,
				+sx, -sy, -sz,
				-sx, +sy, +sz,
				-sx, +sy, -sz,
				-sx, -sy, +sz,
				-sx, -sy, -sz
		}).flip(), (IntBuffer) BufferUtils.createIntBuffer(12*2).put(new int[] {
				0, 1,
				1, 3,
				2, 3,
				0, 2,
				4, 5,
				5, 7,
				6, 7,
				4, 6,
				0, 4,
				1, 5,
				2, 6,
				3, 7
		}).flip(), (FloatBuffer) BufferUtils.createFloatBuffer(8*4).put(new float[] {
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
		}).flip());
		
		return toReturn;
	}
	
	public static LineMesh makeArrow() {
		LineMesh toReturn = new LineMesh((FloatBuffer) BufferUtils.createFloatBuffer(6*3).put(new float[] {
				 0.0f ,  0.0f , -0.5f,
				 0.0f ,  0.0f ,  0.5f,
				 0.25f,  0.0f ,  0.25f,
				-0.25f,  0.0f ,  0.25f,
				 0.0f ,  0.25f,  0.25f,
				 0.0f , -0.25f,  0.25f
		}).flip(), (IntBuffer) BufferUtils.createIntBuffer(6*2).put(new int[] {
				0, 1,
				1, 2,
				1, 3,
				1, 4,
				1, 5
		}).flip(), (FloatBuffer) BufferUtils.createFloatBuffer(6*4).put(new float[] {
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
		}).flip());
		return toReturn;
	}
	
	public static LineMesh makeLineOctahedron() {
		LineMesh toReturn = new LineMesh((FloatBuffer) BufferUtils.createFloatBuffer(6*3).put(new float[] {
				 0.5f ,  0.0f ,  0.0f ,
				-0.5f ,  0.0f ,  0.0f ,
				 0.0f ,  0.5f ,  0.0f,
				 0.0f , -0.5f ,  0.0f,
				 0.0f ,  0.0f ,  0.5f,
				 0.0f ,  0.0f , -0.5f
		}).flip(), (IntBuffer) BufferUtils.createIntBuffer(15*2).put(new int[] {
				0, 2,
				0, 3,
				0, 4,
				0, 5,
				1, 2,
				1, 3,
				1, 4,
				1, 5,
				2, 4,
				2, 5,
				3, 4,
				3, 5,
				0, 1,
				2, 3,
				4, 5
		}).flip(), (FloatBuffer) BufferUtils.createFloatBuffer(6*4).put(new float[] {
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
		}).flip());
		
		return toReturn;
	}
	
	public static BasicMesh getCubeMesh() {
		return cubeMesh;
	}

	public static void setCubeMesh(BasicMesh cubeMesh) {
		MeshUtils.cubeMesh = cubeMesh;
	}

	public static BasicMesh getQuadMesh() {
		return quadMesh;
	}

	public static void setQuadMesh(BasicMesh quadMesh) {
		MeshUtils.quadMesh = quadMesh;
	}
	
}
