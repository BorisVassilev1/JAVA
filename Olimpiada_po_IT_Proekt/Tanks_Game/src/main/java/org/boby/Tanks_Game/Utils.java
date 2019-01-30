package org.boby.Tanks_Game;

import javax.vecmath.Vector2f;

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
}
