package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

import javax.print.attribute.standard.Destination;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;

public class Player {
	Vector2f pos;
	float size;
	String UUID;
	Vector3f color;
	
	Vector2f direction;
	
	float movementSpeed = 200;
	Vector2f currentDestination;
	boolean isMoving = false;
	
	public Player(float x, float y, float size, String UUID) {
		this.pos = new Vector2f(x,y);
		this.size = size;
		this.UUID = UUID;
		direction = new Vector2f();
		color = new Vector3f(0,1 ,0);
		currentDestination = (Vector2f) pos.clone();
	}
	
	public Player(Vector2f pos, float size, String UUID) {
		this.pos = pos;
		this.size = size;
		this.UUID = UUID;
		color = new Vector3f(0,1 ,0);
	}
	
	public void draw()
	{
		glBegin(GL_QUADS);
		{
			glColor3f(color.x, color.y, color.z);
			Utils.glVertexv2f(Utils.PixelsToScreen(new Vector2f(pos.x - size / 2, pos.y - size / 2)));
			Utils.glVertexv2f(Utils.PixelsToScreen(new Vector2f(pos.x + size / 2, pos.y - size / 2)));
			Utils.glVertexv2f(Utils.PixelsToScreen(new Vector2f(pos.x + size / 2, pos.y + size / 2)));
			Utils.glVertexv2f(Utils.PixelsToScreen(new Vector2f(pos.x - size / 2, pos.y + size / 2)));
		}
		glEnd();
	}
	
	public void Input()
	{
		boolean hasmoved = false;
		while(Mouse.next())
		{
			if(Mouse.isInsideWindow() && Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == true)
			{
				float x = Mouse.getX();
				float y = Mouse.getY();
				setDestination(x, y);
				hasmoved = true;
			}
		}
		
		if(hasmoved)
		{
			JSONObject obj = new JSONObject();
			try {
				obj.put("X", pos.x);
				obj.put("Y", pos.y);
				//obj.put("dirX", direction.x);
				//obj.put("dirY", direction.y);
				obj.put("ID", App.myID);
				obj.put("isMoving", isMoving);
				obj.put("destX", currentDestination.x);
				obj.put("destY", currentDestination.y);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			App.socket.emit("move", obj);
		}
	}
	
	public void update()
	{
		if(isMoving)
		{
			Vector2f a = (Vector2f) pos.clone();
			a.sub(currentDestination);
			if(a.lengthSquared() > Time.deltaTime * this.movementSpeed)
			{
				Vector2f b = (Vector2f) (direction.clone());
				b.scale((float) Time.deltaTime * this.movementSpeed);
				pos.add(b);
			}
			else
			{
				Vector2f b = (Vector2f) (direction.clone());
				b.scale(a.lengthSquared());
				pos.add(b);
				this.isMoving = false;
			}
		}
	}
	
	public void setPos(float x, float y)
	{
		this.pos.set(x,y);
	}
	public void setDir(float x, float y) {
		this.direction.set(x, y);
	}
	
	public void setMoving(boolean isMoving)
	{
		this.isMoving = isMoving;
	}
	
	public void setDestination(float x, float y)
	{
		this.setDir(x - pos.x, y - pos.y);
		direction.normalize();
		currentDestination.set(x,y);
		isMoving = true;
	}
	
	@Override
	public String toString() {
		return "{\"X\":" + this.pos.x + ", \"Y\":" + this.pos.y + ", \"ID\"" + this.UUID + "}";
	}
}
