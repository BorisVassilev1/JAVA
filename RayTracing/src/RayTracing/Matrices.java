package RayTracing;

import org.lwjgl.util.vector.Vector3f;

public class Matrices {

	public static Vector3f rotateVector(Vector3f vec, Vector3f rot) {
		float[][] rotationX = { { 1, 0, 0 }, { 0, (float) Math.cos(rot.x), (float) -Math.sin(rot.x) },
				{ 0, (float) Math.sin(rot.x), (float) Math.cos(rot.x) } };
		float[][] rotationY = { { (float) Math.cos(rot.y), 0, (float) -Math.sin(rot.y) }, { 0, 1, 0 },
				{ (float) Math.sin(rot.y), 0, (float) Math.cos(rot.y) } };

		float[][] rotationZ = { { (float) Math.cos(rot.z), (float) -Math.sin(rot.z), 0 },
				{ (float) Math.sin(rot.z), (float) Math.cos(rot.z), 0 }, { 0, 0, 1 } };
		vec = matrixToVec(matMul(rotationX, vec));
		vec = matrixToVec(matMul(rotationY, vec));
		vec = matrixToVec(matMul(rotationZ, vec));
		return vec;
	}

	public static float[][] vecToMatrix(Vector3f v) {
		float[][] m = { { v.x }, { v.y }, { v.z } };
		return m;
	}

	public static Vector3f matrixToVec(float[][] m) {
		Vector3f v = new Vector3f();
		v.x = m[0][0];
		v.y = m[1][0];
		if (m.length > 2) {
			v.z = m[2][0];
		}
		return v;
	}

	public static void logMatrix(float[][] m) {
		int cols = m[0].length;
		int rows = m.length;
		System.out.println(rows + "x" + cols);
		System.out.println("----------------");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static float[][] matMul(float[][] a, Vector3f b) {
		return matMul(a, vecToMatrix(b));
	}

	public static float[][] matMul(float[][] a, float[][] b) {
		int colsA = a[0].length;
		int rowsA = a.length;
		int colsB = b[0].length;
		int rowsB = b.length;

		if (colsA != rowsB) {
			return null;
		}

		float[][] result = new float[rowsA][colsB];

		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				float sum = 0;
				for (int k = 0; k < colsA; k++) {
					sum += a[i][k] * b[k][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}

	public static Vector3f degToRad(Vector3f deg) {
		Vector3f result = new Vector3f(deg.x, deg.y, deg.z);
		return (Vector3f) result.scale((float) (Math.PI / 180));
	}

}
