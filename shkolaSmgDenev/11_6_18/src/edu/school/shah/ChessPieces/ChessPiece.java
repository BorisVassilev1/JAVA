package edu.school.shah.ChessPieces;

import edu.school.helpers.Vector2i;

public abstract class ChessPiece {
	private Vector2i pos;
	private boolean color;
	private boolean selected;
	private boolean hasMoved;
	private String description;
	
	
	public ChessPiece( Vector2i position, boolean color, String desctiption)
	{
		this.pos = position;
		this.color = color;
		this.hasMoved = false;
		this.selected = false;
		this.description = desctiption;
	}
	
	
	public abstract boolean moveTo(Vector2i newPos, ChessPiece[][] table);
	
	public boolean isWhite()
	{
		return color;
	}
	public boolean isBlack()
	{
		return !color;
	}
	
	public void setRow(int val)
	{
		if(val <= 7 && val >= 0)
		pos.y = val;
	}
	public void setColumn(int val)
	{
		if(val <= 7 && val >= 0)
		pos.x = val;
	}
	public int getRow()
	{
		return pos.y;
	}
	public int getColumn()
	{
		return pos.x;
	}
	public Vector2i getPos()
	{
		return pos;
	}
	public void setPos(Vector2i val)
	{
		setRow(val.y);
		setColumn(val.x);
	}
	public void select()
	{
		selected = true;
	}
	public void deselect()
	{
		selected = false;
	}
	public boolean isSelected()
	{
		return selected;
	}
	public void setHasMoved(boolean val)
	{
		hasMoved = val;
	}
	public boolean getHasMoved()
	{
		return hasMoved;
	}
	public String toString()
	{
		return description;
	}
}
