package edu.school.helpers;

import edu.school.shah.ChessPieces.ChessPiece;

public class Vector2i {
	public int x;
	public int y;
	
	public Vector2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public Vector2i()
	{
		x = 0;
		y = 0;
	}
	
	public static Vector2i sub(Vector2i a,Vector2i b)
	{
		a.set(a.x - b.x, a.y - b.y);
		return a;
	}
	
	public static Vector2i add(Vector2i a, Vector2i b)
	{
		a.set(a.x + b.x, a.y + b.y);
		return a;
	}
	
	public Vector2i addX(int val)
	{
		x += val;
		return this;
	}
	public Vector2i addY(int val)
	{
		y += val;
		return this;
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public void set(Vector2i val)
	{
		set(val.x, val.y);
	}
	
	public Vector2i copy()
	{
		return new Vector2i(x,y);
	}
	
	public static ChessPiece getElement(ChessPiece[][] table , Vector2i pos)
	{
		return table[pos.x][pos.y];
	}
}
