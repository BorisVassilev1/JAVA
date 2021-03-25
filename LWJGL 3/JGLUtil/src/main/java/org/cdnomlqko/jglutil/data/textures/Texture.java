package org.cdnomlqko.jglutil.data.textures;

import static org.lwjgl.opengl.GL46.*;

public interface Texture {
	
	public void save(String fileName);
	
	/**
	 * Binds this texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	public void bind(int textureUnit);
	
	/**
	 * Binds the texture to {@link org.lwjgl.opengl.GL13#GL_TEXTURE0}
	 */
	public default void bind() {bind(GL_TEXTURE0);}
	
	/**
	 * binds a null texture
	 * @param textureUnit - parameter to glActiveTexture()
	 */
	public void unbind(int textureUnit);
	
	/**
	 * Binds a null texture to {@link org.lwjgl.opengl.GL13#GL_TEXTURE0}
	 */
	public default void unbind() {unbind(GL_TEXTURE0);}
	
	public void bindImage(int unit);
	public void unbindImage(int unit);
	
	public int getID();
	
	/**
	 * deletes the texture. deallocates the memory.
	 */
	public void delete();
}
