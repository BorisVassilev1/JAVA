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

public class TextureCubeMap implements Texture {
	
	private static final String[] faces = new String[] {
		"right",
		"left",
		"top",
		"bottom",
		"front",
		"back"
	};
	
	int id;
	
	public TextureCubeMap(String path, String format) {
		ByteBuffer buff = null;
		
		this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);
        
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			for(int i = 0; i < faces.length; i ++) {
				String wholePath = path + "/" + faces[i] + format;
				File file = new File(wholePath);
				String filePath = file.getAbsolutePath();
				stbi_set_flip_vertically_on_load(false);
				buff = stbi_load(filePath, w, h, channels, 4);
				if(buff == null) {
					throw new RuntimeException("Image file [" + wholePath  + "] failed to load: " + stbi_failure_reason());
				}
				int width = w.get(0);
				int height = h.get(0);
				
				System.out.println(width + " " + height + " " + i);
				System.out.println(buff.capacity());
				
				glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buff);
				
				stbi_image_free(buff);
			}
		}
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        
//        ByteBuffer b2 = BufferUtils.createByteBuffer(buff.capacity() * 24);
//        glGetTextureImage(id, 0, GL_RGBA, GL_FLOAT, b2);
//        for(int i = 0; b2.hasRemaining() && i < 100; i ++)
//        	System.out.println(b2.asFloatBuffer().get(i));
        
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}
	
	@Override
	public void save(String fileName) {
		// TODO: make write this
	}

	@Override
	public void bind(int textureUnit) {
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
	}

	@Override
	public void unbind(int textureUnit) {
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}

	@Override
	public void bindImage(int textureUnit) {
		glBindImageTexture(textureUnit, id, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);
	}

	@Override
	public void unbindImage(int textureUnit) {
		glBindImageTexture(textureUnit, 0, 0, false, 0, GL_READ_WRITE, GL_RGBA32F);
	}
	
	@Override
	public int getID() {
		return id;
	}

	@Override
	public void delete() {
		glDeleteTextures(id);
	}
}
