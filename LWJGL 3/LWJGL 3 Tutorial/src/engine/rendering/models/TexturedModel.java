package engine.rendering.models;

import org.lwjgl.opengl.GL30;

import engine.rendering.Material;

import org.lwjgl.opengl.GL15;

public class TexturedModel extends Model {
    private int vertexArrayID, vertexBufferID, textureCoordsBufferID, indicesBufferID, vertexCount;
    private Material material;
    
    public TexturedModel(float[] vertices, float[] textureCoords, int[] indices, String file) {
    	vertexArrayID = super.createVertexArray();
    	indicesBufferID = super.bindIndicesBuffer(indices);
        vertexBufferID = super.storeData(0, 3, vertices);
        textureCoordsBufferID = super.storeData(1, 2, textureCoords);
		vertexCount = indices.length;
        GL30.glBindVertexArray(0);
        material = new Material(file);
    }
    
    public void remove() {
    	GL30.glDeleteVertexArrays(vertexArrayID);
    	GL15.glDeleteBuffers(vertexBufferID);
    	GL15.glDeleteBuffers(textureCoordsBufferID);
    	GL15.glDeleteBuffers(indicesBufferID);
    	material.remove();
    }
 
    public int getVertexArrayID() {
        return vertexArrayID;
    }
 
    public int getVertexCount() {
        return vertexCount;
    }

	public Material getMaterial() {
		return material;
	}
}