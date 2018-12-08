package rayTracing;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Main {
	public static Texture tex;
	public static Camera cam = new Camera(new Vector3f(0,0,0),new Vector3f(0,0,0), new Vector3f(0,0,1000));;
	public static boolean isEsc = false;
	public static float i = Float.MAX_VALUE - 1000;
	
	public static int width;
	public static int height;
	
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
			//Display.setInitialBackground(1,1,1);
			Display.create();
			width = Display.getWidth();
			height = Display.getHeight();
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
		
		i = 0;
		
		tex.setTextureFilter(GL_NEAREST);

		long deltatime;

		long timeNow = System.nanoTime();
		long timePrev = System.nanoTime();
		
		glEnable(GL_BLEND);
		
		Calculate.Start();
		//System.out.println(Display.getWidth());
		while (!Display.isCloseRequested()) {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();

			glBegin(GL_QUADS);
			glColor3f(1,1,1);
			glVertex2f(-1, -1);
			glVertex2f(1, -1);
			glVertex2f(1, 1);
			glVertex2f(-1, 1);
			glEnd();
			
			Input.handleInput();
			//Input.handleMouse(isEsc);

			timeNow = System.nanoTime();
			deltatime = timeNow - timePrev;
			timePrev = System.nanoTime();

			System.out.println(1000000000 / deltatime);
			
			Calculate.Update();
			Calculate.Draw();
			
			glDisable(GL_BLEND);
			
			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
	}
}
