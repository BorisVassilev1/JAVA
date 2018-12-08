package edu.tutorial.application1;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Color;

import edu.tutorial.vectors.Vector3;

public class Quad {
	private Vector3 v00;
	private Vector3 v01;
	private Vector3 v11;
	private Vector3 v10;
	
	Vector3 col = new Vector3(0, 0, 0);
	
	public Quad(Vector3 v00, Vector3 v01,Vector3 v11, Vector3 v10, Vector3 color)
	{
		this.v00 = v00;
		this.v01 = v01;
		this.v11 = v11;
		this.v10 = v10;
		this.col = color;
	}
	
	public void draw()
	{
		Main.tex.bind();
		//glBegin(GL_QUADS);
		glColor3f(col.x, col.y, col.z);
		glTexCoord2f(0, 0); glVertex3f(v00.x,v00.y,v00.z);
		glTexCoord2f(0, 1); glVertex3f(v01.x,v01.y,v01.z);
		glTexCoord2f(1, 1); glVertex3f(v11.x,v11.y,v11.z);
		glTexCoord2f(1, 0); glVertex3f(v10.x,v10.y,v10.z);
		
		//glEnd();
	}
}
