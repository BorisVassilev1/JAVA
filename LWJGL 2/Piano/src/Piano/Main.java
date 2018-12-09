package Piano;

import static org.lwjgl.opengl.GL11.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.LineUnavailableException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {
	
	public static void mainx(String[] args) {
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
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public static void gameLoop() {
		
		long deltatime;

		long timeNow = System.nanoTime();
		long timePrev = System.nanoTime();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();

			timeNow = System.nanoTime();
			deltatime = timeNow - timePrev;
			timePrev = System.nanoTime();

			//System.out.println(1000000000 / deltatime);
			
			for(int i = 0; i < 256; i ++)
			{
				if(Keyboard.isKeyDown(i))
				{
					System.out.println(i);
					try {
						SoundUtils.tone((int)(Math.pow(2, (i-49) / 12f) * 440) , 200, 0.1);
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
	}
}
