package RayTracing;

import org.lwjgl.util.vector.Vector3f;

import Objects.Triangle;

public class Rays {
	public static boolean rayToTriangle(Vector3f rayOrigin, Vector3f rayVector, Triangle inTriangle, Vector3f outIntersectionPoint)
	{
		final float EPSILON = 0.00000000001f;
		Vector3f vertex0 = inTriangle.v0;
	    Vector3f vertex1 = inTriangle.v1;  
	    Vector3f vertex2 = inTriangle.v2;
	    Vector3f edge1, edge2, h, s, q;
	    float a,f,u,v;
	    edge1 = Vector3f.sub(vertex1, vertex0, null);
	    edge2 = Vector3f.sub(vertex2, vertex0, null);
	    h = Vector3f.cross(rayVector, edge2, null);
	    a = Vector3f.dot(edge1,h);
	    if(a > -EPSILON && a < EPSILON)
	    	return false;
	    f = 1/a;
	    s = Vector3f.sub(rayOrigin, vertex0, null);
	    u = f * Vector3f.dot(s, h);
	    if (u < 0.0 || u > 1.0)
	        return false;
	    q = Vector3f.cross(s, edge1, null);
	    v = f * Vector3f.dot(rayVector, q);
	    if (v < 0.0 || u + v > 1.0)
	        return false;
	    float t = f * Vector3f.dot(edge2, q);
	    if(t > EPSILON)
	    {
	    	outIntersectionPoint.set(Vector3f.add(rayOrigin, (Vector3f) rayVector.scale(t), null));
	    	//System.out.println( outIntersectionPoint);
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
}
