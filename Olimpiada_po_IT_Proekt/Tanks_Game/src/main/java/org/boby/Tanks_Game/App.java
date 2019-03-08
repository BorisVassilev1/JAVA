package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.boby.Tanks_Game.Utils.HappeningContainer;
import org.boby.Tanks_Game.launcher.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;
import org.lwjgl.opengl.DisplayMode;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Hello world!
 *
 */
public class App implements Runnable
{
	public float zoom = 1;
	public Player[] players;
	public ArrayList<Bullet> bullets;
	public int myID = 0;
	//public Socket socket;
	
	@Override
	public void run() {//run the game
		//Main.gameSocket.connect();
		System.out.println("starting the game window...");
		initDisplay();
		System.out.println("inited display");
    	gameLoop();
    	cleanUp();
	}
	
	public void initSockets()
    {
        //System.out.println( "Hello World!" );
		bullets = new ArrayList<Bullet>();
		Main.gameSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
    		public void call(Object... args) {
    		System.out.println("connection: " + Main.gameSocket.id());
    		System.out.println("sending my launcherID");
    		Main.gameSocket.emit("LauncherID", Main.socket.id(), new Ack() {
				public void call(Object... args) {
					System.out.println("recieving the initializing packet!");
					JSONObject a = (JSONObject)args[0];
					JSONArray array = null;
					try {
						myID = a.getInt("ID");
						System.out.println("my ID is: " + myID);
						array  = a.getJSONArray("players");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					players = new Player[array.length()];
					for(int i = 0; i < array.length(); i ++)
					{
						try {
							players[i] = new Player(array.getJSONObject(i).getJSONObject("pos").getInt("x"), 
									array.getJSONObject(i).getJSONObject("pos").getInt("y"),30f, (byte) array.getJSONObject(i).getInt("team"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					for(int i = 0; i < players.length; i ++)
					{
						if(i == myID) {
							players[i].color.set(0,1,0);
						}
						else if(players[i].team == players[myID].team)
						{
							players[i].color.set(0.5f, 0.5f, 1);
						}
						else
						{
							players[i].color.set(1, 0,0);
						}
					}
					Main.startGame();
			}});
    	}});
    	
    	Main.gameSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
    		public void call(Object... args) {
    			System.out.println("disconnected!");
    	}});
		
    	Main.gameSocket.on("move", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				//System.out.println("a player has moved");
				JSONObject pos = (JSONObject)args[0];
				JSONObject dest = (JSONObject)args[1];
    			int id = (Integer) args[2];
    			//System.out.println(pos);
    			try {
					players[id].setPos((float)pos.getDouble("x"), (float)pos.getDouble("y"));
					players[id].setDestination((float)dest.getDouble("x"), (float)dest.getDouble("y"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}});
    	
    	Main.gameSocket.on("shoot", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				try {
					//System.out.println("a player is shooting");
					JSONObject bull = (JSONObject)args[0];
					JSONObject pos = bull.getJSONObject("pos");
					JSONObject dir = bull.getJSONObject("direction");
					int playerid = (Integer) args[1];
					final Bullet bullet = new Bullet( new Vector2f((float)pos.getDouble("x"), (float)pos.getDouble("y")), (float)Math.atan2(dir.getDouble("y"), dir.getDouble("x")));
					new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {
					            	int ind = Main.app.bullets.indexOf(bullet);
					            	if(ind != -1)
					            	Main.app.bullets.remove(ind);
					            }
					        }, 
					        5000 
					);
					bullets.add(bullet);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	
    	Main.gameSocket.on("damage", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				int playerID = (Integer)args[0];
				int bulletID = (Integer)args[1];
				int amt = (Integer)args[2];
				System.out.println(playerID + "has taken damage");
				players[playerID].dealDamage(amt);
				bullets.remove(bulletID);
			}
		});
    	
    	Main.gameSocket.on("player disconnected", new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				int id = (Integer)args[0];
				System.out.println("player with id: " + id + " has disconnected!");
				//players[id] = null;
				
		}});
    }
	
	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Game");
			Display.setResizable(false);
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

	public void gameLoop() {
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
		//glTranslated(width/2f, height/2f, 0);
		
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
				//glTranslated(width/2f, height/2f, 0);
			}
			//glTranslatef(width/2, height/2, 0);
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
			
			for(int i = 0; i < bullets.size(); i ++) {
				if(bullets.get(i) != null)
				{
					bullets.get(i).update();
					bullets.get(i).draw();
				}
			}
			
			Display.update();
			
			Display.sync(120);
		}
	}

	public void cleanUp() {
		Display.destroy();
		Main.gameSocket.disconnect();
	}

	

}
