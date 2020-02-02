package org.boby.RayTracing.utils;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import static java.awt.image.BufferedImage.*;
/**
 * RGBA 2D texture
 * @author Boby
 *
 */
public class Texture2D {
	private int id;
	private int width;
	private int height;
	/**
	 * Loads a texture from a file.
	 * @param filename - path to the file
	 * @throws Exception 
	 */
	public Texture2D(String fileName){
		int width;
	    int height;
	    ByteBuffer buf;
	    // Load Texture file
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        IntBuffer w = stack.mallocInt(1);
	        IntBuffer h = stack.mallocInt(1);
	        IntBuffer channels = stack.mallocInt(1);
	        
	        File file = new File(fileName);
	        String filePath = file.getAbsolutePath();
	        buf = stbi_load(filePath, w, h, channels, 4);
	        if (buf == null) {
	            throw new RuntimeException("Image file [" + filePath  + "] not loaded: " + stbi_failure_reason());
	        }

	        // Get width and height of image
	        width = w.get();
	        height = h.get();
	        this.width = width;
	        this.height = height;
	    }
	    
	    id = glGenTextures();
	    // Bind the texture
	    glBindTexture(GL_TEXTURE_2D, id);
	    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width,
	    	    height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    glGenerateMipmap(GL_TEXTURE_2D);
	    stbi_image_free(buf);
	    glBindTexture(GL_TEXTURE_2D, 0);
	}
	/**
	 * Creates a blank texture with the given width and height
	 * @param width
	 * @param height
	 */
	public Texture2D(int width, int height) {
		this.width = width;
		this.height = height;
		this.id = glGenTextures();
    	glActiveTexture(GL_TEXTURE0);
    	glBindTexture(GL_TEXTURE_2D, id);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,(ByteBuffer) null);
    	glBindImageTexture(0, id, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
    	glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Saves the data from the texure in a png
	 * @param fileName - path to the file without extension
	 */
	public void save(String fileName) {
		FloatBuffer buff;
		buff = BufferUtils.createFloatBuffer(width * height * 4);
		glBindTexture(GL_TEXTURE_2D, id);
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_FLOAT, buff);
		glBindTexture(GL_TEXTURE_2D, 0);
		System.out.println("asdf");
		
		BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		
		int[] pixels = new int[width * height];
		for(int i = 0; i < width * height; ++ i) {
			int R = (int)(buff.get(i * 4 + 0) * 255);
			int G = (int)(buff.get(i * 4 + 1) * 255);
			int B = (int)(buff.get(i * 4 + 2) * 255);
			int A = (int)(buff.get(i * 4 + 3) * 255);
			try {
				pixels[i] = new Color(R,G,B,A).getRGB();
				img.setRGB(i % width, i / width, pixels[i]);
			} catch(Exception e) {
				System.out.println((i % width ) + " " + (i / width) + ": " + R + " " + G + " " + B + " " + A);
			}
		}
		
		try {
			File of = new File(fileName + ".png");
			ImageIO.write(img, "png", of);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Binds this texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	public void bind(int textureUnit)
	{
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	/*
	 * Binds the texture
	 */
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * binds a null texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	public void unbind(int textureUnit)
	{
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	/**
	 * Binds a null texture
	 */
	public void unbind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void delete() {
		glDeleteTextures(this.id);
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
