package rayTracing;

import java.awt.Color;
import java.awt.Graphics;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Triangle {

	public Vector3f vertices[] = new Vector3f[3];
	public Vector2f verticesP[] = new Vector2f[3];
	
	Color fillColor = new Color(255,255,255,255);
	Color wireColor = new Color(0  ,0  ,0  ,255);
	
	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3) {
		vertices[0] = v1;
		vertices[1] = v2;
		vertices[2] = v3;
	}

	public void draw() {
		verticesP[0] = Main.cam.CalcPoint(vertices[0]);
		verticesP[1] = Main.cam.CalcPoint(vertices[1]);
		verticesP[2] = Main.cam.CalcPoint(vertices[2]);
		
		if(verticesP[0] != null && verticesP[1] != null && verticesP[2] != null)
		{
			verticesP[0].x /= Main.width / 2;
			verticesP[0].y /= Main.height / 2;
			
			verticesP[1].x /= Main.width / 2;
			verticesP[1].y /= Main.height / 2;
			
			verticesP[2].x /= Main.width / 2;
			verticesP[2].y /= Main.height / 2;
			
			glColor4f(wireColor.getRed(),wireColor.getGreen() / 255f,wireColor.getBlue() / 255f,wireColor.getAlpha() / 255f);
			glBegin(GL_LINE_LOOP);
			glVertex2f(verticesP[0].x, verticesP[0].y);
			glVertex2f(verticesP[1].x, verticesP[1].y);
			glVertex2f(verticesP[2].x, verticesP[2].y);
			glEnd();
			
			glColor4f(fillColor.getRed(), fillColor.getGreen() / 255f, fillColor.getBlue() / 255f, fillColor.getAlpha() / 255f);
			glBegin(GL_TRIANGLES);
			glVertex2f(verticesP[0].x, verticesP[0].y);
			glVertex2f(verticesP[1].x, verticesP[1].y);
			glVertex2f(verticesP[2].x, verticesP[2].y);
			glEnd();
		}
	}
	
	public void setFillColor(Color c)
	{
		fillColor = c;
	}
	
	public void setWireColor(Color c)
	{
		wireColor = c;
	}
	
	public void set(Vector3f v1, Vector3f v2, Vector3f v3)
	{
		vertices[0] = v1;
		vertices[1] = v2;
		vertices[2] = v3;
	}
}
