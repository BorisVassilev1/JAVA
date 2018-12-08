package edu.vkashti.waves;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Main {
	public static Texture tex;
	public static Camera cam;
	public static boolean isEsc = false;
	public static float i = Float.MAX_VALUE - 1000;
	
	public static void main(String[] args) {
		initDisplay();

		gameLoop();
		cleanUp();
	}

	public static void initDisplay() {
		try {
			// Display.setDisplayMode(new DisplayMode(1000, 800));
			Display.setTitle("Game");
			Display.setResizable(true);
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
		tex = loadTexture("grass_top");
		cam = new Camera(70f, (float) Display.getWidth() / (float) Display.getHeight(), 0.3f, 1000f);
		i = 0;
		
		int sizeX = 40;
		int sizeZ = 40;
		float maxHeight = 20;
		
		
		float[][] array = new float[sizeX][sizeZ];
		
		tex.setTextureFilter(GL_NEAREST);

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

			System.out.println(1000000000 / deltatime);
			
			i += 0.01;
			
			float locI = Main.i ;
			locI = locI % 360;
			for (int x = 0; x < sizeX; x++) {
				for (int z = 0; z < sizeZ; z++) {
					
					float y = (float) (Math.sin( Math.sqrt( ( (x-sizeX/2) * (x-sizeX/2) + (z-sizeZ/2) * (z-sizeZ/2))) /2  - locI ) * maxHeight/8 + maxHeight/4 ) ;
					
					for (int i = 0; i < y; i++) {
						array[x][z] = y;
					}
				}
			}
			
			for (int x = 0; x < sizeX; x++) {
				for (int z = 0; z < sizeZ; z++) {
					drawCube(x, z, tex, array[x][z]);
				}
			}
			

			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
	}
	
	public static void drawCube(float x, float z, Texture tex, float height)
	{
		glPushMatrix();
		{
			glTranslatef(x, 0, z);
			tex.bind();
			glScalef(1, height, 1);
			glBegin(GL_QUADS);
			{
				glColor3f(0, 1, 1);

				glVertex3f(-1, -1, 1);
				glVertex3f(-1, 1, 1);
				glVertex3f(1, 1, 1);
				glVertex3f(1, -1, 1);

				glColor3f(1, 0, 0);

				glVertex3f(-1, -1, -1);
				glVertex3f(-1, 1, -1);
				glVertex3f(1, 1, -1);
				glVertex3f(1, -1, -1);

				glColor3f(0, 1, 0);

				glVertex3f(-1, -1, -1);
				glVertex3f(-1, -1, 1);
				glVertex3f(-1, 1, 1);
				glVertex3f(-1, 1, -1);

				glColor3f(1, 0, 1);

				glVertex3f(1, -1, -1);
				glVertex3f(1, -1, 1);
				glVertex3f(1, 1, 1);
				glVertex3f(1, 1, -1);

				glColor3f(0, 0, 1);

				glVertex3f(-1, -1, -1);
				glVertex3f(1, -1, -1);
				glVertex3f(1, -1, 1);
				glVertex3f(-1, -1, 1);

				glColor3f(1, 1, 0);

				glVertex3f(-1, 1, -1);
				glVertex3f(1, 1, -1);
				glVertex3f(1, 1, 1);
				glVertex3f(-1, 1, 1);

			}
			glEnd();
		}
		glPopMatrix();
	}
}
