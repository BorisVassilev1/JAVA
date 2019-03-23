package objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import mesh.Mesh;

public class Object3d {
	
	private Mesh mesh;
	//private Material material
	//TODO:position, rotation, etc
	
	public Object3d() {
		mesh = new Mesh(new float[]{
				-0.5f, +0.5f, 0,
				+0.5f, +0.5f, 0,
				-0.5f, -0.5f, 0,
				+0.5f, -0.5f, 0
		}, new int[] {
				0, 1, 2,
				1, 2, 3
		});
		mesh.create();
	}
	
	public void draw()
	{
		GL30.glBindVertexArray(mesh.getVAOId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void destroy()
	{
		mesh.remove();
	}
}
