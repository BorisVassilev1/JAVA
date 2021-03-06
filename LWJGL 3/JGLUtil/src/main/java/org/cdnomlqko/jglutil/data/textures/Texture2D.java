package org.cdnomlqko.jglutil.data.textures;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
/**
 * RGBA 2D texture
 * @author Boby
 *
 */
public class Texture2D implements Texture {
	private int id;
	private int width;
	private int height;
	
	/**
	 * Loads a texture from a file.
	 * @param fileName - path to the file
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
	        stbi_set_flip_vertically_on_load(false);
	        buf = stbi_load(filePath, w, h, channels, 4);
	        if (buf == null) {
	            throw new RuntimeException("Image file [" + filePath  + "] failed to load: " + stbi_failure_reason());
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
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
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
    	glGenerateMipmap(GL_TEXTURE_2D);
    	//glBindImageTexture(0, id, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);
    	glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Saves the data from the texure in a png
	 * @param fileName - path to the file without extension
	 */
	@Override
	public void save(String fileName) {
		FloatBuffer buff;
		buff = BufferUtils.createFloatBuffer(width * height * 4);
		glBindTexture(GL_TEXTURE_2D, id);
		glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_FLOAT, buff);
		glBindTexture(GL_TEXTURE_2D, 0);
				
		BufferedImage img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		
		int[] pixels = new int[width * height];
		for(int i = 0; i < width * height; ++ i) {
			int R = (int)(buff.get(i * 4 + 0) * 255);
			int G = (int)(buff.get(i * 4 + 1) * 255);
			int B = (int)(buff.get(i * 4 + 2) * 255);
			int A = (int)(buff.get(i * 4 + 3) * 255);
			try {
				pixels[i] = new Color(R, G, B, A).getRGB();
				img.setRGB(i % width, i / width, pixels[i]);
			} catch(Exception e) {
				System.out.println("Error on pixel: " +  (i % width ) + " " + (i / width) + ": " + R + " " + G + " " + B + " " + A);
			}
		}
		
		try {
			File of = new File(fileName);
			ImageIO.write(img, "png", of);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Binds this texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	@Override
	public void bind(int textureUnit)
	{
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	/**
	 * binds a null texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	@Override
	public void unbind(int textureUnit)
	{
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	@Override
	public void bindImage(int unit) {
		glBindImageTexture(unit, id, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);
	}
	
	@Override
	public void unbindImage(int unit) {
		glBindImageTexture(unit, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);
	}
	
	/**
	 * deletes the texture. deallocates the memory.
	 */
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
