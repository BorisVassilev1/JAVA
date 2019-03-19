package Objects;

import org.lwjgl.util.vector.Vector3f;

public class IntersectionPoint {
	
	public Vector3f point;
	public Object3d object;
	public int triangleIndex;
	
	
	public IntersectionPoint(Vector3f point, Object3d object, int triangleIndex)
	{
		this.point = new Vector3f(point);
		this.object = object;
		this.triangleIndex = triangleIndex;
	}
	
	public void set(Vector3f point, Object3d object, int triangleIndex)
	{
		this.point.set(point);
		this.object = object;
		this.triangleIndex = triangleIndex;
	}
}
