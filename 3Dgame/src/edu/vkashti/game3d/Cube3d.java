package edu.vkashti.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class Cube3d {
	
	Vector3 position = new Vector3(0, 0, 0);

	Vector3[] vertex = new Vector3[] {
	new Vector3(-0.5f, -0.5f, 1),
	new Vector3(-0.5f, 0.5f, 1),
	new Vector3(0.5f, 0.5f, 1),
	new Vector3(0.5f, -0.5f, 1),
	new Vector3(-0.5f, -0.5f, 1.5f),
	new Vector3(-0.5f, 0.5f, 1.5f),
	new Vector3(0.5f, 0.5f, 1.5f),
	new Vector3(0.5f, -0.5f, 1.5f)
	};
	Vector2[] projection = new Vector2[] {
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0),
	new Vector2(0, 0)
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
	
	public Cube3d(Vector3 position,float scale,Color fillColor, Color wireColor) {
		this.position = position;
		this.scale = scale;
		this.fillColor = fillColor;
		this.wireColor = wireColor;
	}
	public void calculateVertices()
	{
		projection[0] = Point3D.CalcPoint(Point3D.add(vertex[0], position)).multiply(scale);
		projection[1] = Point3D.CalcPoint(Point3D.add(vertex[1], position)).multiply(scale);
		projection[2] = Point3D.CalcPoint(Point3D.add(vertex[2], position)).multiply(scale);
		projection[3] = Point3D.CalcPoint(Point3D.add(vertex[3], position)).multiply(scale);
		projection[4] = Point3D.CalcPoint(Point3D.add(vertex[4], position)).multiply(scale);
		projection[5] = Point3D.CalcPoint(Point3D.add(vertex[5], position)).multiply(scale);
		projection[6] = Point3D.CalcPoint(Point3D.add(vertex[6], position)).multiply(scale);
		projection[7] = Point3D.CalcPoint(Point3D.add(vertex[7], position)).multiply(scale);
	}
	public void calculatePolygons()
	{
		xFront = new int[] { 
				(int) (projection[0].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[1].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[2].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[3].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yFront = new int[] { 
				(int) (projection[0].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[1].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[2].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[3].y+ Main.pnlCanvas.getHeight()/2)
				};
		
		xBack = new int[] { 
				(int) (projection[4].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[5].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[6].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[7].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yBack = new int[] { 
				(int) (projection[4].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[5].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[6].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[7].y+ Main.pnlCanvas.getHeight()/2)
				};
		
		xLeft = new int[] { 
				(int) (projection[0].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[4].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[5].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[1].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yLeft = new int[] { 
				(int) (projection[0].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[4].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[5].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[1].y+ Main.pnlCanvas.getHeight()/2)
				};
		
		xRight = new int[] { 
				(int) (projection[2].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[6].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[7].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[3].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yRight = new int[] { 
				(int) (projection[2].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[6].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[7].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[3].y+ Main.pnlCanvas.getHeight()/2)
				};
		
		xDown = new int[] { 
				(int) (projection[1].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[5].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[6].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[2].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yDown = new int[] { 
				(int) (projection[1].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[5].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[6].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[2].y+ Main.pnlCanvas.getHeight()/2)
				};
		
		xUp = new int[] { 
				(int) (projection[0].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[4].x+ Main.pnlCanvas.getWidth()/2), 
				(int) (projection[7].x+ Main.pnlCanvas.getWidth()/2),
				(int) (projection[3].x+ Main.pnlCanvas.getWidth()/2) 
				};
		yUp = new int[] { 
				(int) (projection[0].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[4].y+ Main.pnlCanvas.getHeight()/2), 
				(int) (projection[7].y+ Main.pnlCanvas.getHeight()/2),
				(int) (projection[3].y+ Main.pnlCanvas.getHeight()/2)
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
	
	void setPosition(Vector3 position1) {
		this.position = position1;
	}
	
	void setScale(float scale1)
	{
		this.scale = scale1;
	}
}
