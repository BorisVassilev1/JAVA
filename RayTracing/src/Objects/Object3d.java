package Objects;

import org.lwjgl.util.vector.Vector3f;

import Mesh.Mesh;

import static org.lwjgl.opengl.GL11.*;

public class Object3d {
	
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	public Mesh mesh;
	
	public Object3d(Mesh mesh)
	{
		this.mesh = mesh;
		position = new Vector3f(0,0,0);
		rotation = new Vector3f(0,0,0);
		scale = new Vector3f(1,1,1);
	}
	
	public void draw()
	{
		glPushMatrix();
		{
			glTranslatef(position.x,position.y,position.z);
			glScalef(scale.x, scale.y, scale.z);
			glRotatef(rotation.x,1,0,0);
			glRotatef(rotation.y,0,1,0);
			glRotatef(rotation.z,0,0,1);
			glBegin(GL_TRIANGLES);
			{
				for(int i = 0 ; i < mesh.triangles.length; i +=3)
				{
					glColor3f(mesh.colors[i/3].x,mesh.colors[i/3].y,mesh.colors[i/3].z);
					glVertex3f(mesh.vertices[mesh.triangles[i]].x,mesh.vertices[mesh.triangles[i]].y,mesh.vertices[mesh.triangles[i]].z);
					glVertex3f(mesh.vertices[mesh.triangles[i + 1]].x,mesh.vertices[mesh.triangles[i + 1]].y,mesh.vertices[mesh.triangles[i + 1]].z);
					glVertex3f(mesh.vertices[mesh.triangles[i + 2]].x,mesh.vertices[mesh.triangles[i + 2]].y,mesh.vertices[mesh.triangles[i + 2]].z);
				}
			}
			glEnd();
		}
		glPopMatrix();
	}
}
