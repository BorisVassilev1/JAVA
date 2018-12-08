package rayTracing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Sphere3d {
	Vector3f position = new Vector3f(0, 0, 0);

	Vector3f rotation = new Vector3f(0, 0, 0);

	float r = 100;
	int resolution = 100;
	
	public float randomKoef = 0;
	
	Color fillColor = new Color(0,0,0,50);
	Color wireColor = new Color(0,0,0,100);
	
	static ArrayList<Triangle> Triangles = new ArrayList<>();

	Vector3f[][] globe = new Vector3f[resolution + 1][resolution + 1];

	Random rand = new Random();
	
	public Sphere3d(float radius, int resolution) {
		this.r = radius;
		this.resolution = resolution;
	}

	public void calculateVertices() {
		for (int i = 0; i < resolution + 1; i++) {
			float lat = map(i, 0, resolution, (float) -Math.PI / 2, (float) Math.PI / 2);
			for (int j = 0; j < resolution + 1; j++) {
				float lon = map(j, 0, resolution, (float) -Math.PI, (float) Math.PI);
				
				float newR = r + rand.nextFloat() * randomKoef;
				
				float x = (float) (newR * Math.cos(lon) * Math.cos(lat));
				float z = (float) (newR * Math.sin(lon) * Math.cos(lat));
				float y = (float) (newR * Math.sin(lat));

				x += position.x;
				y += position.y;
				z += position.z;

				// x = rotate(new Vector2(x,y),new Vector2(position.x,
				// position.y),rotation.z).x;
				// y = rotate(new Vector2(x,y),new Vector2(position.x,
				// position.y),rotation.z).y;
				//
				// y = rotate(new Vector2(y,z),new Vector2(position.y,
				// position.z),rotation.x).x;
				// z = rotate(new Vector2(y,z),new Vector2(position.y,
				// position.z),rotation.x).y;
				//
				// x = rotate(new Vector2(x,z),new Vector2(position.z,
				// position.x),rotation.y).x;
				// z = rotate(new Vector2(x,z),new Vector2(position.z,
				// position.x),rotation.y).y;

				globe[i][j] = new Vector3f(x, y, z);
			}
		}
	}

	public void view() {
		for (Triangle triangle : Triangles) {
//			triangle.setFillColor(fillColor);
//			triangle.setWireColor(wireColor);
			triangle.draw();
		}
	}

	public void createTriangles() {
		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				Vector3f v1 = globe[i][j];
				Vector3f v2 = globe[i][j + 1];
				Vector3f v3 = globe[i + 1][j];
				Vector3f v4 = globe[i + 1][j + 1];

				Triangles.add(new Triangle(v1, v2, v3));
				Triangles.add(new Triangle(v2, v3, v4));
				
				Triangles.get(Triangles.size() - 1).setFillColor(fillColor);
				Triangles.get(Triangles.size() - 1).setWireColor(wireColor);
				
				Triangles.get(Triangles.size() - 2).setFillColor(fillColor);
				Triangles.get(Triangles.size() - 2).setWireColor(wireColor);
			}
		}
	}

	public void refreshTriangles() {
		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				Vector3f v1 = globe[i][j];
				Vector3f v2 = globe[i][j + 1];
				Vector3f v3 = globe[i + 1][j];
				Vector3f v4 = globe[i + 1][j + 1];

				// Triangles.add(new Triangle(v1, v2, v3));
				// Triangles.add(new Triangle(v2, v3, v4));

//				Triangles.set((i * resolution + j) * 2, new Triangle(v1, v2, v3));
//				Triangles.set((i * resolution + j) * 2 + 1, new Triangle(v2, v3, v4));
				
				Triangles.get((i * resolution + j) * 2).set(v1,v2,v3);
				Triangles.get((i * resolution + j) * 2 + 1).set(v2,v3,v4);
			}
		}
	}

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}

	public void setRotation(Vector3f rot) {
		this.rotation = rot;
	}
	
	public void setFillColor(Color c)
	{
		fillColor = c;
	}
	
	public void setWireColor(Color c)
	{
		wireColor = c;
	}
	
	public void init()
	{
		this.calculateVertices();
		this.createTriangles();
	}
	
	public void update()
	{
		this.calculateVertices();
		this.refreshTriangles();
	}
	
	private Vector2f rotate(Vector2f xy, Vector2f pivotxy, float radians) {
		// Vector2 newxy = new Vector2(
		// (float) Math.sin(radians + Math.atan((xy.x - pivotxy.x)/(xy.y - pivotxy.y)))
		// * ((xy.x - pivotxy.x) * (xy.x - pivotxy.x) + (xy.y - pivotxy.y) * (xy.y -
		// pivotxy.y)),
		// (float) Math.cos(radians + Math.atan((xy.x - pivotxy.x)/(xy.y - pivotxy.y)))
		// * ((xy.x - pivotxy.x) * (xy.x - pivotxy.x) + (xy.y - pivotxy.y) * (xy.y -
		// pivotxy.y)));

		// Vector2 newxy = new Vector2(
		// (float) (Math.cos(radians) * (xy.x - pivotxy.x) - Math.sin(radians) * (xy.y -
		// pivotxy.y) + pivotxy.x),
		// (float) (Math.sin(radians) * (xy.x - pivotxy.x) + Math.cos(radians) * (xy.y -
		// pivotxy.y) + pivotxy.y)
		// );

		Vector2f relxy = new Vector2f(xy.x - pivotxy.x, xy.y - pivotxy.y);
		Vector2f newxy = new Vector2f(
				(float) (Math.sin(radians + Math.atan(relxy.x / relxy.y))
						* Math.sqrt((relxy.x) * (relxy.x) + (relxy.y) * (relxy.y))),
				(float) (Math.cos(radians + Math.atan(relxy.x / relxy.y))
						* Math.sqrt((relxy.x) * (relxy.x) + (relxy.y) * (relxy.y))));
		if (xy.y > pivotxy.y) {
			newxy.y = pivotxy.y - (newxy.y - pivotxy.y);
			newxy.x = pivotxy.x - (newxy.x - pivotxy.x);
		}
		// if(radians>Math.PI)
		// {
		// newxy = rotate(newxy,pivotxy,(float)(radians-Math.PI));
		// }

		return newxy;
	}

	private float map(int num, float minval1, float maxval1, float minval2, float maxval2) {
		float a = (minval1 - num) * (maxval2 - minval2) / (maxval1 - minval1) + minval2;
		return a;
	}
}
