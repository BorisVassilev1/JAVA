package edu.tutorial.application1;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Main {

	public static Texture tex;
	public static Camera cam;
	public static boolean isEsc = false;
	public static float i;
	
	public static void main(String[] args) {
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

		int terrainX = 128;
		int terrainY = 20;
		int terrainZ = 128;

		tex.setTextureFilter(GL_NEAREST);

		int[][][] terrain = Terrain.generateTerrainArray(terrainX, terrainZ, terrainY, 0.03f,
				(float) (Math.random() * 100.0));
		ArrayList<Quad> quads = Terrain.calculateTerrainQuads(terrain, terrainX, terrainY, terrainZ);

		System.out.println(quads.size());

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
			
//			for (int x = 0; x < 3; x++) {
//				for (int y = 0; y < 3; y++) {
//					for (int z = 0; z < 3; z++) {
//						if(x - Math.round(cam.getX()) -1 >= 0 &&
//								y - Math.round(cam.getY()) -1 >= 0 &&
//								z - Math.round(cam.getZ()) -1 >= 0 &&
//								x - Math.round(cam.getX()) -1 <terrainX &&
//								y - Math.round(cam.getY()) -1 < terrainY &&
//								z - Math.round(cam.getZ()) -1 < terrainZ){
//							terrain[x - Math.round(cam.getX()) -1]
//								   [y - Math.round(cam.getY()) -1]
//								   [z - Math.round(cam.getZ()) -1] = 0;
//						}
//					}
//				}
//			}
			terrain = Terrain.generateTerrainArray(terrainX, terrainZ, terrainY, 0.03, i/5);
			quads= Terrain.calculateTerrainQuads(terrain, terrainX, terrainY, terrainZ);

			Terrain.draw(quads);
			//i += 0.03;
			//System.out.printf("%f, %f, %f, %f",cam.getX(),cam.getY(),cam.getZ(),i);
			
			Display.update();
		}
	}

	public static void cleanUp() {
		Display.destroy();
	}
}
