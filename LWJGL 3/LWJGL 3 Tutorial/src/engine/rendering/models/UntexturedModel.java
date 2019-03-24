package engine.rendering.models;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL15;

public class UntexturedModel extends Model {
    private int vertexArrayID, vertexBufferID, indicesBufferID, vertexCount;
    
    public UntexturedModel(float[] vertices, int[] indices) {
    	vertexArrayID = super.createVertexArray();
    	indicesBufferID = super.bindIndicesBuffer(indices);
        vertexBufferID = super.storeData(0, 3, vertices);
		vertexCount = indices.length;
        GL30.glBindVertexArray(0);
    }
    
    public void remove() {
    	GL30.glDeleteVertexArrays(vertexArrayID);
    	GL15.glDeleteBuffers(vertexBufferID);
    	GL15.glDeleteBuffers(indicesBufferID);
    }
 
    public int getVertexArrayID() {
        return vertexArrayID;
    }
 
    public int getVertexCount() {
        return vertexCount;
    }
}