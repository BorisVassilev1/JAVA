package objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import mesh.Mesh;
import mesh.MeshBase;

public class Object3d {
	
	private Mesh mesh;
	//private Material material
	//TODO:position, rotation, etc
	
	public Object3d(Mesh mesh) {
		this.mesh = mesh;
		this.mesh.create();
	}
	
	public void draw()
	{
		GL30.glBindVertexArray(mesh.getVAOID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        //shader.loadTransformationMatrix(entity.getTransformationMatrix());
        //GL13.glActiveTexture(GL13.GL_TEXTURE0);
        //GL11.glBindTexture(GL11.GL_TEXTURE_2D, Main.);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
	}
	
	public void destroy()
	{
		mesh.remove();
	}
}
