package Objects;

import org.lwjgl.util.vector.Vector3f;

public class Mesh {
	
	public Vector3f[] vertices;
	public int[] triangles;
	public Triangle[] tris;
	public Vector3f[] colors;
	
	public Mesh(Vector3f[] vertices, int[] triangles, Vector3f[] colors)
	{
		this.vertices = vertices;
		this.triangles = triangles;
		this.colors = colors;
		tris = new Triangle[triangles.length / 3];
		
		for(int i = 0; i < triangles.length / 3; i ++)
		{
			tris[i] = new Triangle(vertices[triangles[3 * i]],vertices[triangles[3 * i + 1]],vertices[triangles[3 * i + 2]]); 
		}
	}
	public Mesh()
	{
		
	}
}
