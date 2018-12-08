package rayTracing;

import java.awt.Color;
import java.awt.Graphics;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Cube3d {
	
	Vector3f position = new Vector3f(0, 0, 0);

	Vector3f[] vertex = new Vector3f[] {
	new Vector3f(-0.5f, -0.5f, 1),
	new Vector3f(-0.5f, 0.5f, 1),
	new Vector3f(0.5f, 0.5f, 1),
	new Vector3f(0.5f, -0.5f, 1),
	new Vector3f(-0.5f, -0.5f, 1.5f),
	new Vector3f(-0.5f, 0.5f, 1.5f),
	new Vector3f(0.5f, 0.5f, 1.5f),
	new Vector3f(0.5f, -0.5f, 1.5f)
	};
	Vector2f[] projection = new Vector2f[] {
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0),
	new Vector2f(0, 0)
	};
	
	float scale = 300;
	
	private int[] xFront;
	private int[] yFront;
	private int[] xBack;
	private int[] yBack;
	private int[] xLeft;
	private int[] yLeft;
	private int[] xRight;
	private int[] yRight;
	private int[] xDown;
	private int[] yDown;
	private int[] xUp;
	private int[] yUp;
	
	private Color fillColor = new Color(0,0,0,20);
	private Color wireColor = new Color(0,0,0);
	
	public Cube3d(Vector3f position,float scale,Color fillColor, Color wireColor) {
		this.position = position;
		this.scale = scale;
		this.fillColor = fillColor;
		this.wireColor = wireColor;
	}
	public void calculateVertices()
	{
		projection[0] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[0], position)).scale(scale);
		projection[1] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[1], position)).scale(scale);
		projection[2] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[2], position)).scale(scale);
		projection[3] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[3], position)).scale(scale);
		projection[4] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[4], position)).scale(scale);
		projection[5] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[5], position)).scale(scale);
		projection[6] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[6], position)).scale(scale);
		projection[7] = (Vector2f) Main.cam.CalcPoint(Main.cam.add(vertex[7], position)).scale(scale);
	}
	public void calculatePolygons()
	{
		xFront = new int[] { 
				(int) (projection[0].x+ Main.width/2), 
				(int) (projection[1].x+ Main.width/2), 
				(int) (projection[2].x+ Main.width/2),
				(int) (projection[3].x+ Main.width/2) 
				};
		yFront = new int[] { 
				(int) (projection[0].y+ Main.height/2), 
				(int) (projection[1].y+ Main.height/2), 
				(int) (projection[2].y+ Main.height/2),
				(int) (projection[3].y+ Main.height/2)
				};
		
		xBack = new int[] { 
				(int) (projection[4].x+ Main.width/2), 
				(int) (projection[5].x+ Main.width/2), 
				(int) (projection[6].x+ Main.width/2),
				(int) (projection[7].x+ Main.width/2) 
				};
		yBack = new int[] { 
				(int) (projection[4].y+ Main.height/2), 
				(int) (projection[5].y+ Main.height/2), 
				(int) (projection[6].y+ Main.height/2),
				(int) (projection[7].y+ Main.height/2)
				};
		
		xLeft = new int[] { 
				(int) (projection[0].x+ Main.width/2), 
				(int) (projection[4].x+ Main.width/2), 
				(int) (projection[5].x+ Main.width/2),
				(int) (projection[1].x+ Main.width/2) 
				};
		yLeft = new int[] { 
				(int) (projection[0].y+ Main.height/2), 
				(int) (projection[4].y+ Main.height/2), 
				(int) (projection[5].y+ Main.height/2),
				(int) (projection[1].y+ Main.height/2)
				};
		
		xRight = new int[] { 
				(int) (projection[2].x+ Main.width/2), 
				(int) (projection[6].x+ Main.width/2), 
				(int) (projection[7].x+ Main.width/2),
				(int) (projection[3].x+ Main.width/2) 
				};
		yRight = new int[] { 
				(int) (projection[2].y+ Main.height/2), 
				(int) (projection[6].y+ Main.height/2), 
				(int) (projection[7].y+ Main.height/2),
				(int) (projection[3].y+ Main.height/2)
				};
		
		xDown = new int[] { 
				(int) (projection[1].x+ Main.width/2), 
				(int) (projection[5].x+ Main.width/2), 
				(int) (projection[6].x+ Main.width/2),
				(int) (projection[2].x+ Main.width/2) 
				};
		yDown = new int[] { 
				(int) (projection[1].y+ Main.height/2), 
				(int) (projection[5].y+ Main.height/2), 
				(int) (projection[6].y+ Main.height/2),
				(int) (projection[2].y+ Main.height/2)
				};
		
		xUp = new int[] { 
				(int) (projection[0].x+ Main.width/2), 
				(int) (projection[4].x+ Main.width/2), 
				(int) (projection[7].x+ Main.width/2),
				(int) (projection[3].x+ Main.width/2) 
				};
		yUp = new int[] { 
				(int) (projection[0].y+ Main.height/2), 
				(int) (projection[4].y+ Main.height/2), 
				(int) (projection[7].y+ Main.height/2),
				(int) (projection[3].y+ Main.height/2)
				};
	}
	public void view(Graphics g)
	{
		g.setColor(wireColor);
		
		g.drawPolygon(xFront, yFront, 4);
		g.drawPolygon(xBack, yBack, 4);
		g.drawPolygon(xLeft, yLeft, 4);
		g.drawPolygon(xRight, yRight, 4);
		g.drawPolygon(xDown, yDown, 4);
		g.drawPolygon(xUp, yUp, 4);
		
		g.setColor(fillColor);
		g.fillPolygon(xFront, yFront, 4);
		g.fillPolygon(xBack, yBack, 4);
		g.fillPolygon(xLeft, yLeft, 4);
		g.fillPolygon(xRight, yRight, 4);
		g.fillPolygon(xDown, yDown, 4);
		g.fillPolygon(xUp, yUp, 4);
	}
	
	void setPosition(Vector3f position1) {
		this.position = position1;
	}
	
	void setScale(float scale1)
	{
		this.scale = scale1;
	}
}
