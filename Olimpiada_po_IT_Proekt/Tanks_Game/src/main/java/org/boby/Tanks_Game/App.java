package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Vector2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
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
	public static float zoom = 1;
	public static void main( String[] args ) throws URISyntaxException
    {
		NativeLoader.loadNatives("lib/natives-win");
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

		Time.initTime();
		
		glEnable(GL_TEXTURE_2D);
		
		Player player = new Player(100f,100f, 30f, "saudiskd");
		
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();

			int width = Display.getWidth();
			int height = Display.getHeight();
			
			Time.updateTime();
			if(Display.wasResized())
			{
				glViewport(0, 0, width, height); 
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, width, 0, height, -1, 1);
			}
			if(Time.deltaTimeI > 1000000000 / 60)
			{
				//System.out.println("FPS: " + 1000000000 / Time.deltaTimeI);
			}
			int Dwheel = Mouse.getDWheel();
			if(Dwheel < 0)
			{
				zoom *= 1.25f ;
			}
			else if(Dwheel > 0)
			{
				zoom *= 0.8f ;
			}
			glScalef(zoom, zoom, 1);
			
			player.Input();
			player.draw();
			
			Display.update();
			
			Display.sync(120);
		}
	}

	public static void cleanUp() {
		Display.destroy();
		System.exit(1);
	}

}
