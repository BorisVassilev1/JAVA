package engine.maths;

public class Vector3f {
	private float x, y, z;
	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float dot(Vector3f vector) {
		return x * vector.getX() + y * vector.getY() + z * vector.getZ();
	}
	
	public Vector3f normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		return this;
	}
	
	public Vector3f rotate(Vector3f vector) {
		return null;
	}
	
	public Vector3f cross(Vector3f vector) {
		float newX = y * vector.getZ() - z * vector.getY();
		float newY = z * vector.getX() - x * vector.getZ();
		float newZ = x * vector.getY() - y * vector.getX();
		
		return new Vector3f(newX, newY, newZ);
	}
	
	public Vector3f add(Vector3f vector) {
		return new Vector3f(x + vector.getX(), y + vector.getY(), z + vector.getZ());
	}
	
	public Vector3f sub(Vector3f vector) {
		return new Vector3f(x - vector.getX(), y - vector.getY(), z - vector.getZ());
	}
	
	public Vector3f mul(Vector3f vector) {
		return new Vector3f(x * vector.getX(), y * vector.getY(), z * vector.getZ());
	}
	
	public Vector3f div(Vector3f vector) {
		return new Vector3f(x / vector.getX(), y / vector.getY(), z / vector.getZ());
	}
	
	public Vector3f add(float value) {
		return new Vector3f(x + value, y + value, z + value);
	}
	
	public Vector3f sub(float value) {
		return new Vector3f(x - value, y - value, z - value);
	}
	
	public Vector3f mul(float value) {
		return new Vector3f(x * value, y * value, z * value);
	}
	
	public Vector3f div(float value) {
		return new Vector3f(x / value, y / value, z / value);
	}
	
	public String toString() {
		return x + ", " + y + ", " + z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}