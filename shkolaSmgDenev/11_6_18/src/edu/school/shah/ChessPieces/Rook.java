package edu.school.shah.ChessPieces;

import edu.school.helpers.Vector2i;

public class Rook extends ChessPiece{

	public Rook(Vector2i position, boolean color) {
		super(position, color, "rook");
	}

	@Override
	public boolean moveTo(Vector2i newPos, ChessPiece[][] table) {
		if(newPos.x < 0 || newPos.x > 7 || newPos.y < 0 || newPos.y > 7)
		{
			return false;
		}
		Vector2i delta = Vector2i.sub(newPos,getPos());
		Vector2i temp = new Vector2i();
		if(delta.x == 0)
		{
			if(delta.y != 0)
			{
				for(int i = getPos().y; i != newPos.y; i += Integer.signum(delta.y))
				{
					temp.set(getPos());
					if(Vector2i.getElement(table, temp.addY(i)) != null)
					{
						return false;
					}
				}
				setPos(newPos);
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(delta.y == 0)
		{
			if(delta.x != 0)
			{
				for(int i = getPos().x; i != newPos.x; i += Integer.signum(delta.x))
				{
					temp.set(getPos());
					if(Vector2i.getElement(table, temp.addX(i)) != null)
					{
						return false;
					}
				}
				setPos(newPos);
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

}
