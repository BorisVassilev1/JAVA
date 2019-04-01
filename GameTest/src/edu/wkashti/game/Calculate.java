package edu.wkashti.game;

import java.awt.Color;
import java.awt.Graphics;

public class Calculate extends Main {
	void drawSpyral(Graphics g, int count, int w, int h) {
		int prevdeg = 0;
		int prevrad = 0;
		int rad = 0;
		int deg = 0;
		for (int i = 0; i < count; i++) {
			rad += 1;
			deg = prevdeg + 10;
			g.drawLine((int) (Math.sin(prevdeg * Math.PI / 180) * prevrad + w / 2),
					(int) (Math.cos(prevdeg * Math.PI / 180) * prevrad + h / 2),
					(int) (Math.sin(deg * Math.PI / 180) * rad + w / 2),
					(int) (Math.cos(deg * Math.PI / 180) * rad + h / 2));
			prevdeg += 10;
			prevrad = rad;
		}
	}

	void drawNAngle(Graphics g, int Angles, double radius, int w, int h) {

		int prevang = 0;
		for (int i = 0; i < Angles; i++) {
			int ang = prevang + 360 / Angles;
			g.drawLine((int) (Math.sin(prevang * Math.PI / 180) * radius + w / 2),
					(int) (Math.cos(prevang * Math.PI / 180) * radius + h / 2),
					(int) (Math.sin(ang * Math.PI / 180) * radius + w / 2),
					(int) (Math.cos(ang * Math.PI / 180) * radius + h / 2));
			prevang = ang;
		}
	}

	static int startx = 10;
	static int starty = 50;
	static int width = 800;
	static int height = 600;
	static int iterations = 200;
	static int zoom = 300;
	
	
	static double cx = -0.8f;
	static double cy = 0.156f;
	
	static float angle = 0;
	public static void Update() {
		if(IsKeyPressed[87]) {
			cy -= 0.01;
		}
		if(IsKeyPressed[65]) {
			cx -= 0.01;
		}
		if(IsKeyPressed[83]) {
			cy += 0.01;
		}
		if(IsKeyPressed[68]) {
			cx += 0.01;
		}
		if(IsKeyPressed[38]) {
			zoom += 100;
		}
		if(IsKeyPressed[40]) {
			zoom -= 100;
		}
		if(IsKeyPressed[39]) 
		{
			startx += 20;
		}
		if(IsKeyPressed[37])
		{
			startx -= 20;
		}
		if(IsKeyPressed[70]) 
		{
			starty += 20;
		}
		if(IsKeyPressed[82])
		{
			starty -= 20;
		}
		angle += 0.01;
	}

	public static int calculatepoint(double x, double y) {
		double cx = x;
		double cy = y;
		int i = 0;

		for (; i < iterations; i++) {
			double nx = x * x - y * y + cx;
			double ny = 2 * x * y + cy;
			x = nx;
			y = ny;

			if (x * x + y * y > 4)
				break;

		}
		if (i == iterations) {
			return 0;
		} else {
			return (int) (i*255.0/iterations);
		}
	}

	public static void Draw(Graphics g, int w, int h) {
		g.fillRect(0, 0, width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixel = calculatepoint((x - width / 2f + startx) / zoom, (y - height / 2f + starty) / zoom);
				Color c = new Color(pixel, pixel, pixel);
				g.setColor(c);
				g.drawRect(x, y, 1, 1);
			}
		}

	}
}
