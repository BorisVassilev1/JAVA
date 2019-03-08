package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Bullet {
	
	private Vector2f pos;
	private Vector2f dir;
	private float movementSpeed = 300;
	private float directionRad = 0;
	private float size;
	private Vector3f color;
	
	public Bullet(Vector2f position, float directionRad) {
		this.pos = position;
		this.dir = new Vector2f((float)Math.cos(directionRad),(float)Math.sin(directionRad));
		this.directionRad = directionRad;
		this.size = 10;
		this.color = new Vector3f(1, 0, 0);
	}
	
	public void draw()
	{
		Vector2f v1 = new Vector2f(-size/2f, -size/2f);
		Vector2f v2 = new Vector2f(+size/2f, -size/2f);
		Vector2f v3 = new Vector2f(+size/2f, +size/2f);
		Vector2f v4 = new Vector2f(-size/2f, +size/2f);
		
		v1 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v1), directionRad));// ротиране на вурховете на фигурата
		v2 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v2), directionRad));
		v3 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v3), directionRad));
		v4 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v4), directionRad));
		v1.add(pos);// добавяне на позицията
		v2.add(pos);
		v3.add(pos);
		v4.add(pos);
		glBegin(GL_QUADS);// рисуване
		{
			glColor3f(color.x, color.y, color.z);
			Utils.glVertexv2f(Utils.PixelsToScreen(v1));
			Utils.glVertexv2f(Utils.PixelsToScreen(v2));
			Utils.glVertexv2f(Utils.PixelsToScreen(v3));
			Utils.glVertexv2f(Utils.PixelsToScreen(v4));
		}
		glEnd();
	}
	
	public void update()
	{
		Vector2f b = (Vector2f) dir.clone();
		b.scale((float) (movementSpeed * Time.deltaTime)); 
		this.pos.add(b);
	}
}
