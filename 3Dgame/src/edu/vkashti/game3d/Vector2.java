package edu.vkashti.game3d;

public class Vector2 {
	float x;
	float y;
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 multiply(Vector2 a)
	{
		return new Vector2(this.x*a.x , this.y * a.y);
	}
	
	public Vector2 multiply(float a)
	{
		return new Vector2(this.x * a, this.y * a);
	}
	
}
