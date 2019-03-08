package org.boby.Tanks_Game;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

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
}
