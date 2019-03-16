package RayTracing;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Objects.Cube;
import Objects.Object3d;
import Objects.Triangle;

public class Main {
	public static Texture tex;
	public static Camera cam;
	public static boolean isEsc = false;
	public static float i = Float.MAX_VALUE - 1000;
	
	public static ArrayList<Object3d> scene = new ArrayList<Object3d>();
	
	public static void main(String[] args) {
		RenderWindow.create();
		initDisplay();
		gameLoop();
		cleanUp();
	}

	public static void initDisplay() {
		try {
			// Display.setDisplayMode(new DisplayMode(1000, 800));
			Display.setTitle("Game");
			Display.setResizable(false);
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public static Texture loadTexture(String key) {
		try {
			return TextureLoader.getTexture("png", new FileInputStream(new File("res/" + key + ".png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("problem loading the texture");
		}
		return null;
	}

	public static void gameLoop() {
		//tex = loadTexture("grass_top");
		//tex.setTextureFilter(GL_NEAREST);
		cam = new Camera(70f, (float) Display.getWidth() / (float) Display.getHeight(), 0.3f, 1000f);
		
		scene.add(new Cube());
		Cube c2 = new Cube();
		c2.scale.set(0.1f, 0.1f, 0.1f);
		scene.add(c2);
		
		long deltatime;

		long timeNow = System.nanoTime();
		long timePrev = System.nanoTime();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			cam.useView();

			Input.handleInput();
			Input.handleMouse(isEsc);

			timeNow = System.nanoTime();
			deltatime = timeNow - timePrev;
			timePrev = System.nanoTime();

			//System.out.println(1000000000 / deltatime);
			
			for (Object3d obj : scene) {
				obj.draw();
			}
			
			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
		System.exit(0);
	}
	
	public static void rayTrace() {
		Vector3f intPoint = new Vector3f();
		Vector3f camDir = new Vector3f(0,0,1);
		camDir = Matrices.rotateVector(camDir, (Vector3f) cam.rot.negate(null).scale((float) (Math.PI / 180)));
		//System.out.println(cam.rot);
		//System.out.println(camDir);
		for (Object3d obj : scene) {
			for(int i = 0; i < obj.mesh.tris.length; i ++)
			{
				if(Rays.rayToTriangle(cam.pos, camDir, obj.mesh.tris[i], intPoint)) {
					drawLine3f(cam.pos, Vector3f.add(cam.pos, intPoint, null));
					//System.out.println(intPoint);
					//System.out.println(intPoint);
					Cube c = new Cube();
					c.scale.set(0.1f,0.1f,0.1f);
					c.position = intPoint;
					c.draw();
				}
			}
		}
		//System.out.println("----------------");
	}
	
	public static void drawLine3f(Vector3f a, Vector3f b)
	{
		glLineWidth(100);
		glBegin(GL_LINES);
		{
			glColor3f(0.5f, 0.5f, 0.5f);
			glVertex3f(a.x, a.y, a.z);
			glVertex3f(b.x, b.y, b.z);
		}
		glEnd();
	}
	public static void drawLine2f(Vector3f proj0, Vector3f proj2)
	{
		glLineWidth(100);
		JPanel panel = RenderWindow.panel;
		Graphics g = panel .getGraphics();
		g.setColor(new Color(0,0,0));
		g.drawLine((int)(proj0.x * panel.getWidth()), (int)(proj0.y * panel.getHeight()), (int)(proj2.x * panel.getWidth()), (int)(proj2.y * panel.getHeight()));
		glEnd();
	}
	
}
