package myproject;

import static org.lwjgl.opengl.GL11.*;


import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws URISyntaxException
    {
    	initDisplay();
    	gameLoop();
    	cleanUp();
    }
	
	public static void initDisplay() {
		try {
			 Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Game");
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, e);
		}
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_ALPHA_TEST);
	}

//	public static Texture loadTexture(String key) {
//		try {
//			return TextureLoader.getTexture("png", new FileInputStream(new File("res/" + key + ".png")));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("problem loading the texture");
//		}
//		return null;
//	}

	public static void gameLoop() {

		long deltatime;

		long timeNow = System.nanoTime();
		long timePrev = System.nanoTime();
		
		glEnable(GL_TEXTURE_2D);
		
		
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();

			timeNow = System.nanoTime();
			deltatime = timeNow - timePrev;
			timePrev = System.nanoTime();

			if(deltatime < 1000000000 / 120)
			{
				try {
					TimeUnit.NANOSECONDS.sleep(1000000000 / 120 - deltatime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println(1000000000 / deltatime);
			}
			
			glBegin(GL_QUADS);
			{
				glVertex2f(-0.5f, -0.5f);
				glVertex2f(0.5f, -0.5f);
				glVertex2f(0.5f, 0.5f);
				glVertex2f(-0.5f, 0.5f);
			}
			glEnd();
			
			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
		System.exit(1);
	}

}
