package Objects;

import org.lwjgl.util.vector.Vector3f;

public class Triangle {
	
	public Vector3f v0;
	public Vector3f v1;
	public Vector3f v2;
	
	public Vector3f normal;
	
	public Triangle(Vector3f v0, Vector3f v1, Vector3f v2)
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.normal = Vector3f.cross(Vector3f.sub(v0,  v1, null), Vector3f.sub(v1,  v2, null), null);
	}
}
