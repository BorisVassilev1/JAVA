package org.boby.RayTracing.utils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
	private int id;
	private int width;
	private int height;
	/**
	 * loads a texture from a file.
	 * @param filename - path to the file
	 */
	public Texture(String filename) {
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File(filename));
			width = bi.getWidth();
			height = bi.getHeight();
			
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			for(int y = 0; y < height; y ++)
			{
				for(int x = 0; x < width; x ++)
				{
					int pixel = pixels_raw[y * width + x];
					
					pixels.put((byte) ((pixel >> 16) & 0xFF));//red
					pixels.put((byte) ((pixel >> 8) & 0xFF));//green
					pixels.put((byte) (pixel  & 0xFF));//blue
					pixels.put((byte) ((pixel >> 24) & 0xFF));//alpha
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			glBindTexture(GL_TEXTURE_2D, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * binds this texture
	 */
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
	/**
	 * binds a null texture so any texture that has been bound will not be drawn
	 */
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	
}
