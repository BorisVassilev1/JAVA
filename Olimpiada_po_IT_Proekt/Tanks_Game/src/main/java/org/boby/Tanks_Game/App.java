package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.net.URISyntaxException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Vector2f;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
	public static Player[] players;
	public static int myID = 0;
	public static Socket socket;
	public static void main( String[] args ) throws URISyntaxException
    {
		NativeLoader.loadNatives("lib/natives-win");
        //System.out.println( "Hello World!" );
    	socket = IO.socket("http://localhost:3000");
    	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
    		public void call(Object... args) {
    		//socket.emit("foo", "hi");
    		System.out.println("connection: " + socket.id());
    		//socket.disconnect();
    	  }});
    	socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
    		public void call(Object... args) {
    			System.out.println("disconnected: " + socket.id());
    	}});
    	socket.on("init", new Emitter.Listener() {
			public void call(Object... args) {
				System.out.println("recieving the initializing packet!");
				JSONObject a = (JSONObject)args[0];
				try {
					myID = a.getInt("ID");
					System.out.println(myID);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println(a);
				try {
					myID = a.getInt("ID");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONArray array = null;
				try {
					array  = a.getJSONArray("players");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(array.toString());
				players = new Player[array.length()];
				for(int i = 0; i< array.length(); i ++)
				{
					try {
						players[i] = new Player(array.getJSONObject(i).getInt("X"), 
								array.getJSONObject(i).getInt("Y"),30f, "sadiu");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}});
    	socket.on("new player", new Emitter.Listener() {
    		@Override
    		public void call(Object... args) {
    			System.out.println("recieved a packet with a new Player!");
    			JSONObject a = (JSONObject)args[0];
    			int id = 0;
    			try {
					id = a.getInt("ID");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			if(id > players.length - 1)
    			{
    				System.out.println("Resizing the players array!");
    				Player[] newPlayers = new Player[id + 1];
    				for(int i = 0; i < players.length; i ++)
    				{
    					newPlayers[i] = players[i];
    				}
    				players = newPlayers;
    			}
    			try {
					players[id] = new Player(a.getJSONObject("newPlayer").getInt("X"), 
							a.getJSONObject("newPlayer").getInt("Y"),30f, "sadiu");
					players[id].setDestination(a.getJSONObject("newPlayer").getInt("destX"), 
							a.getJSONObject("newPlayer").getInt("destY"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
    	}});
    	socket.on("move", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject a = (JSONObject)args[0];
    			int id = 0;
    			try {
					id = a.getInt("ID");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					//players[id].setDir((float) a.getDouble("dirX"), (float) a.getDouble("dirY"));
					players[id].setPos((float) a.getDouble("X"), (float) a.getDouble("Y"));
					players[id].setMoving(a.getBoolean("isMoving"));
					players[id].setDestination((float) a.getDouble("destX"), (float) a.getDouble("destY"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}});
    	socket.on("player disconnected", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				int id = (Integer)args[0];
				players[id] = null;
				
		}});
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
		
		try {
			Mouse.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = Display.getWidth();
		int height = Display.getHeight();
		
		glViewport(0, 0, width, height); 
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, 0, height, -1, 1);
		glTranslated(width/2f, height/2f, 0);
		
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glLoadIdentity();
			
			Time.updateTime();
			if(Display.wasResized())
			{
				width = Display.getWidth();
				height = Display.getHeight();
				glViewport(0, 0, width, height); 
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, width, 0, height, -1, 1);
				glTranslated(width/2f, height/2f, 0);
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
			
			players[myID].Input();
			for(int i = 0; i < players.length; i ++)
			{
				if(players[i] != null)
				{
					players[i].update();
					players[i].draw();
				}
			}
			
			Display.update();
			
			Display.sync(120);
		}
	}

	public static void cleanUp() {
		Display.destroy();
		System.exit(1);
	}

}
