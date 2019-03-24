package engine.maths;

public class Matrix4f {
	private float[][] matrix;
	
	public Matrix4f() {
		matrix = new float[4][4];
	}
	
	public Matrix4f identity() {
		matrix[0][0] = 1;	matrix[0][1] = 0;	matrix[0][2] = 0;	matrix[0][3] = 0;
		matrix[1][0] = 0;	matrix[1][1] = 1;	matrix[1][2] = 0;	matrix[1][3] = 0;
		matrix[2][0] = 0;	matrix[2][1] = 0;	matrix[2][2] = 1;	matrix[2][3] = 0;
		matrix[3][0] = 0;	matrix[3][1] = 0;	matrix[3][2] = 0;	matrix[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f translate(Vector3f vector) {
		matrix[0][0] = 1;	matrix[0][1] = 0;	matrix[0][2] = 0;	matrix[0][3] = vector.getX();
		matrix[1][0] = 0;	matrix[1][1] = 1;	matrix[1][2] = 0;	matrix[1][3] = vector.getY();
		matrix[2][0] = 0;	matrix[2][1] = 0;	matrix[2][2] = 1;	matrix[2][3] = vector.getZ();
		matrix[3][0] = 0;	matrix[3][1] = 0;	matrix[3][2] = 0;	matrix[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f rotate(Vector3f vector) {
		Matrix4f rotateX = new Matrix4f();
		Matrix4f rotateY = new Matrix4f();
		Matrix4f rotateZ = new Matrix4f();
		
		Vector3f rotatedVector = new Vector3f((float) Math.toRadians(vector.getX()), (float) Math.toRadians(vector.getY()), (float) Math.toRadians(vector.getZ()));
		
		rotateZ.matrix[0][0] = (float)Math.cos(rotatedVector.getZ()); rotateZ.matrix[0][1] = -(float)Math.sin(rotatedVector.getZ()); rotateZ.matrix[0][2] = 0; rotateZ.matrix[0][3] = 0;
		rotateZ.matrix[1][0] = (float)Math.sin(rotatedVector.getZ()); rotateZ.matrix[1][1] =  (float)Math.cos(rotatedVector.getZ()); rotateZ.matrix[1][2] = 0; rotateZ.matrix[1][3] = 0;
		rotateZ.matrix[2][0] = 0;				  					  rotateZ.matrix[2][1] = 0;									     rotateZ.matrix[2][2] = 1; rotateZ.matrix[2][3] = 0;
		rotateZ.matrix[3][0] = 0;				 	 				  rotateZ.matrix[3][1] = 0;									     rotateZ.matrix[3][2] = 0; rotateZ.matrix[3][3] = 1;
		
		rotateX.matrix[0][0] = 1; rotateX.matrix[0][1] = 0;										rotateX.matrix[0][2] = 0;									   rotateX.matrix[0][3] = 0;
		rotateX.matrix[1][0] = 0; rotateX.matrix[1][1] = (float)Math.cos(rotatedVector.getX()); rotateX.matrix[1][2] = -(float)Math.sin(rotatedVector.getX()); rotateX.matrix[1][3] = 0;
		rotateX.matrix[2][0] = 0; rotateX.matrix[2][1] = (float)Math.sin(rotatedVector.getX()); rotateX.matrix[2][2] = (float)Math.cos(rotatedVector.getX());  rotateX.matrix[2][3] = 0;
		rotateX.matrix[3][0] = 0; rotateX.matrix[3][1] = 0;										rotateX.matrix[3][2] = 0;									   rotateX.matrix[3][3] = 1;
		
		rotateY.matrix[0][0] = (float)Math.cos(rotatedVector.getY()); rotateY.matrix[0][1] = 0; rotateY.matrix[0][2] = -(float)Math.sin(rotatedVector.getY()); rotateY.matrix[0][3] = 0;
		rotateY.matrix[1][0] = 0;									  rotateY.matrix[1][1] = 1; rotateY.matrix[1][2] = 0;									   rotateY.matrix[1][3] = 0;
		rotateY.matrix[2][0] = (float)Math.sin(rotatedVector.getY()); rotateY.matrix[2][1] = 0; rotateY.matrix[2][2] = (float)Math.cos(rotatedVector.getY());  rotateY.matrix[2][3] = 0;
		rotateY.matrix[3][0] = 0;									  rotateY.matrix[3][1] = 0; rotateY.matrix[3][2] = 0;									   rotateY.matrix[3][3] = 1;
		
		matrix = rotateZ.mul(rotateY.mul(rotateX)).getMatrix();
		
		return this;
	}
	
	public Matrix4f scale(Vector3f vector) {
		matrix[0][0] = vector.getX(); matrix[0][1] = 0;				matrix[0][2] = 0;			  matrix[0][3] = 0;
		matrix[1][0] = 0;			  matrix[1][1] = vector.getY(); matrix[1][2] = 0;			  matrix[1][3] = 0;
		matrix[2][0] = 0;			  matrix[2][1] = 0;				matrix[2][2] = vector.getZ(); matrix[2][3] = 0;
		matrix[3][0] = 0;			  matrix[3][1] = 0;				matrix[3][2] = 0;			  matrix[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f projection(float fov, float aspectRatio, float zNear, float zFar) {
		float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2));
		float zRange = zNear - zFar;
		
		matrix[0][0] = 1.0f / (tanHalfFOV * aspectRatio); matrix[0][1] = 0;					matrix[0][2] = 0;						 matrix[0][3] = 0;
		matrix[1][0] = 0;								  matrix[1][1] = 1.0f / tanHalfFOV;	matrix[1][2] = 0;						 matrix[1][3] = 0;
		matrix[2][0] = 0;								  matrix[2][1] = 0;					matrix[2][2] = (-zNear - zFar) / zRange; matrix[2][3] = 2 * zFar * zNear / zRange;
		matrix[3][0] = 0;								  matrix[3][1] = 0;					matrix[3][2] = 1;						 matrix[3][3] = 0;
			
		return this;
	}
	
	public Matrix4f mul(Matrix4f m) {
		Matrix4f result = new Matrix4f();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.set(i, j, matrix[i][0] * m.get(0, j) +
								 matrix[i][1] * m.get(1, j) +
								 matrix[i][2] * m.get(2, j) +
								 matrix[i][3] * m.get(3, j));
			}
		}
		
		return result;
	}

	public float[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(float[][] matrix) {
		this.matrix = matrix;
	}
	
	public float get(int x, int y) {
		return matrix[x][y];
	}
	
	public void set(int x, int y, float value) {
		matrix[x][y] = value;
	}
	
	public String toString() {
		String matrix = "";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matrix += (Float.toString(get(i, j)) + ", ");
			}
			matrix += "\n";
		}
		return matrix + "";
	}
}
