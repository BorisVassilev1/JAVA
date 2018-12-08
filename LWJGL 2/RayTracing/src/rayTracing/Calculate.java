package rayTracing;

import java.awt.Color;
import java.awt.Graphics;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public class Calculate extends Main {

	static Vector3f position = new Vector3f(0, 0, 100);
	
	static float scale = 200;
	
	static Color fillColor = new Color(0,0,0,20);
	static Color wireColor = new Color(0,0,0,50);
	
	static Sphere3d Sphere = new Sphere3d(100,10);
	static Cube3d cube = new Cube3d(position, scale, fillColor, wireColor);
	
	public static void Start()
	{
		Sphere.init();
		Sphere.setFillColor(fillColor);
		Sphere.setWireColor(wireColor);
	}
	
	public static void Update() {
		Sphere.setPosition(position);
		Sphere.randomKoef = 0;
		Sphere.update();
		

		Vector2f mouse = new Vector2f(
				(Mouse.getX() - width / 2) / (width / 2f),
				(Mouse.getY() - height / 2) / (height / 2f)
		);
		drawRect(mouse, 0.1f, new Color(0,1,0));
		if(Sphere.Triangles.get(0).verticesP[0] != null)
		{
			Raycaster rc = new Raycaster();
			for(int i = 0; i < Sphere.Triangles.size(); i ++)
			{
				Vector3f inter = rc.isin(Sphere.Triangles.get(i), mouse);
				if(inter != null)
				{
					Sphere.Triangles.get(i).setFillColor(new Color(255,0,0,100));
					Vector2f v = cam.CalcPoint(inter);
					if(v != null)
					{
						v.x /= width / 2;
						v.y /= height / 2;
						drawRect(v, 0.2f,new Color(0,0,1));
					}
					else
					{
//						System.out.println("null");
					}
				}
				else
				{
					Sphere.Triangles.get(i).setFillColor(new Color(0,0,0,20));
				}
			}
		}
	}

	public static void Draw() {
		Sphere.view();
	}
	
	static void drawRect(Vector2f pos, float rad, Color color)
	{
		float radx = rad / width * 100;
		float rady = rad / height * 100;
		glColor4f(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
		glBegin(GL_QUADS);
		glVertex2f(pos.x - radx, pos.y - rady);
		glVertex2f(pos.x + radx, pos.y - rady);
		glVertex2f(pos.x + radx, pos.y + rady);
		glVertex2f(pos.x - radx, pos.y + rady);
		glEnd();
	}
}
