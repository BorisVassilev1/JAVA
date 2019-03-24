package engine.rendering;

import java.io.FileInputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;

public class Material {
	private int textureID;
	
	public Material(String file) {
		try {
			textureID = TextureLoader.getTexture("png", new FileInputStream("res/textures/" + file)).getTextureID();
		} catch (IOException e) {
			System.err.println("Error: Couldn't load texture");
			System.exit(-1);
		}
	}
	
	public void remove() {
		GL11.glDeleteTextures(textureID);
	}

	public int getTextureID() {
		return textureID;
	}
}