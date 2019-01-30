package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.input.Keyboard;

public class Player {
	Vector2f pos;
	float size;
	String UUID;
	Vector3f color;
	
	public Player(float x, float y, float size, String UUID) {
		this.pos = new Vector2f(x,y);
		this.size = size;
		this.UUID = UUID;
		color = new Vector3f(0,1 ,0);
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
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			pos.y += 1000 * Time.deltaTime;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			pos.y -= 1000 * Time.deltaTime;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			pos.x -= 1000 * Time.deltaTime;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			pos.x += 1000 * Time.deltaTime;
		}
	}
}
