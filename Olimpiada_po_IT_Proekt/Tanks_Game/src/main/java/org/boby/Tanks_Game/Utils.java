package org.boby.Tanks_Game;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {
	
	public static Vector2f PixelsToScreen(Vector2f pixels)
	{
		 pixels.set(pixels.x / Display.getWidth() * 2.0f - 1.0f, pixels.y  / Display.getHeight() * 2.0f- 1.0f);
		 return pixels;
	}
	public static void glVertexv2f(Vector2f vec)
	{
		glVertex2f(vec.x, vec.y);
	}
	
	public static Vector3f vec2fto3f(Vector2f a)
	{
		return new Vector3f(a.x, a.y, 0);
	}
	public static Vector2f vec3fto2f(Vector3f a)
	{
		return new Vector2f(a.x, a.y);
	}
	
	public static class HappeningContainer{
		
		private boolean hasHappened;
		public HappeningContainer()
		{
			hasHappened = false;
		}
		public void happen()
		{
			hasHappened = true;
		}
		public boolean hasHappened()
		{
			return hasHappened;
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
}
