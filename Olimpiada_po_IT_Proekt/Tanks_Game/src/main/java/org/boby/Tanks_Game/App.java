package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;
import org.lwjgl.opengl.DisplayMode;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws URISyntaxException
    {
        //System.out.println( "Hello World!" );
    	final Socket socket = IO.socket("http://localhost:3000");
    	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

    	  public void call(Object... args) {
    	    socket.emit("foo", "hi");
    	    System.out.println("connection: " + socket.id());
    	    //socket.disconnect();
    	  }

    	}).on("event", new Emitter.Listener() {
    	  public void call(Object... args) {
    		  System.out.println("recieved a message from the server: ");
    		  for (Object object : args) {
				System.out.println(object);
			}
    	  }
    	  

    	}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

    	  public void call(Object... args) {
    		  System.out.println("disconnected: " + socket.id());
    	  }

    	});
    	socket.connect();
    	initDisplay();
    	gameLoop();
    	cleanUp();
    }
	
	public static void initDisplay() {
		try {
			 Display.setDisplayMode(new DisplayMode(1000, 800));
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

			//System.out.println(1000000000 / deltatime);
			
			glBegin(GL_QUADS);
			{
				glColor3f(1, 1, 1);
				glVertex2f(-0.5f, -0.5f);
				glVertex2f(-0.5f, 0.5f);
				glVertex2f(0.5f,  0.5f);
				glVertex2f(0.5f, -0.5f);
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
