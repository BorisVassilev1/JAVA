package edu.vkashti.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle {

	public Vector3 v1;
	public Vector3 v2;
	public Vector3 v3;
	
	Color fillColor = new Color(255,255,255,255);
	Color wireColor = new Color(0  ,0  ,0  ,255);
	
	public Triangle(Vector3 v1, Vector3 v2, Vector3 v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public void draw(Graphics g) {
		Vector2 v1p = Point3D.CalcPoint(v1);
		Vector2 v2p = Point3D.CalcPoint(v2);
		Vector2 v3p = Point3D.CalcPoint(v3);
		
		int w = Main.pnlCanvas.getWidth()/2;
		int h = Main.pnlCanvas.getHeight()/2;
		
//		if(v1p != null && v2p != null) {
//		g.drawLine((int) (v1p.x+w), (int) (v1p.y+h), (int) (v2.x+w), (int) (v2.y+h));
//		}
//		if(v2p != null && v3p != null) {
//		g.drawLine((int) (v2p.x+w), (int) (v2p.y+h), (int) (v3.x+w), (int) (v3.y+h));
//		}
//		if(v3p != null && v1p != null) {
//		g.drawLine((int) (v3p.x+w), (int) (v3p.y+h), (int) (v1.x+w), (int) (v1.y+h));
//		}
		if(v1p != null && v2p != null && v3p != null)
		{
			int xPoints[] = new int[] { (int) v1p.x + w, (int) v2p.x + w, (int) v3p.x + w };
			int yPoints[] = new int[] { (int) v1p.y + h, (int) v2p.y + h, (int) v3p.y + h };
			g.setColor(wireColor);
			g.drawPolygon(xPoints, yPoints, 3);
			g.setColor(fillColor);
			g.fillPolygon(xPoints, yPoints, 3);
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
	
}
