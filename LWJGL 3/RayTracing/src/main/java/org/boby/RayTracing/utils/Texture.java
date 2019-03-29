package org.boby.RayTracing.utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class Texture {
	private int id;
	private int width;
	private int height;
	/**
	 * loads a texture from a file.
	 * @param filename - path to the file
	 * @throws Exception 
	 */
	public Texture(String fileName) throws Exception {
		int width;
	    int height;
	    ByteBuffer buf;
	    // Load Texture file
	    try (MemoryStack stack = MemoryStack.stackPush()) {
	        IntBuffer w = stack.mallocInt(1);
	        IntBuffer h = stack.mallocInt(1);
	        IntBuffer channels = stack.mallocInt(1);

	        //URL url = Texture.class.getResource(fileName);
	        //File file = Paths.get(url.toURI()).toFile();
	        File file = new File(fileName);
	        String filePath = file.getAbsolutePath();
	        buf = stbi_load(filePath, w, h, channels, 4);
	        if (buf == null) {
	            throw new Exception("Image file [" + filePath  + "] not loaded: " + stbi_failure_reason());
	        }

	        /* Get width and height of image */
	        width = w.get();
	        height = h.get();
	    }
	    
	    id = glGenTextures();
	    // Bind the texture
	    glBindTexture(GL_TEXTURE_2D, id);
	    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width,
	    	    height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    GL30.glGenerateMipmap(GL_TEXTURE_2D);
	    stbi_image_free(buf);

	}
	/**
	 * binds this texture
	 */
	public void bind()
	{
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	/**
	 * binds a null texture so any texture that has been bound will not be drawn
	 */
	public void unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getID()
	{
		return id;
	}
}
