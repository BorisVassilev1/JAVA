package engine.maths;

public class Vector2f {
	private float x, y;
	public Vector2f() {
		x = 0;
		y = 0;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2f vector) {
		return x * vector.getX() + y * vector.getY();
	}
	
	public Vector2f normalize() {
		float length = length();
		x /= length;
		y /= length;
		return this;
	}	
	
	public Vector2f rotate(float angle) {
		float radians = (float) Math.toRadians(angle);
		return new Vector2f((float) (x * Math.cos(radians)) - (float) (y * Math.sin(radians)), (float) (x * Math.sin(radians) + (float) y * Math.cos(radians)));
	}
	
	public Vector2f add(Vector2f vector) {
		return new Vector2f(x + vector.getX(), y + vector.getY());
	}
	
	public Vector2f sub(Vector2f vector) {
		return new Vector2f(x - vector.getX(), y - vector.getY());
	}
	
	public Vector2f mul(Vector2f vector) {
		return new Vector2f(x * vector.getX(), y * vector.getY());
	}
	
	public Vector2f div(Vector2f vector) {
		return new Vector2f(x / vector.getX(), y / vector.getY());
	}
	
	public Vector2f add(float value) {
		return new Vector2f(x + value, y + value);
	}
	
	public Vector2f sub(float value) {
		return new Vector2f(x - value, y - value);
	}
	
	public Vector2f mul(float value) {
		return new Vector2f(x * value, y * value);
	}
	
	public Vector2f div(float value) {
		return new Vector2f(x / value, y / value);
	}
	
	public String toString() {
		return x + ", " + y;
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
}