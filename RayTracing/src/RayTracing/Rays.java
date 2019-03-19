package RayTracing;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;

import Objects.IntersectionPoint;
import Objects.Object3d;
import Objects.Triangle;

public class Rays {
	
	static Vector3f[] projPlane = new Vector3f[4];
	static Vector3f intPoint;
	static Vector3f rayDir;
	
	public static void initProjection()
	{
		intPoint = new Vector3f();
	}
	
	public static void UpdateViewPlane(float imageRatio, float fovDeg)
	{
		
		//float fieldOfView = 70;
		fovDeg *= Math.PI / 180;
		float planeOffset = (float) (1/Math.tan(fovDeg/2));
		Vector3f camrot2 = new Vector3f(Main.cam.rot.x, Main.cam.rot.y + 180, Main.cam.rot.z);
		projPlane[0] = (Vector3f) new Vector3f(- 1 * imageRatio, - 1, planeOffset).normalise();
		projPlane[1] = (Vector3f) new Vector3f(+ 1 * imageRatio, - 1, planeOffset).normalise();
		projPlane[2] = (Vector3f) new Vector3f(- 1 * imageRatio, + 1, planeOffset).normalise();
		projPlane[3] = (Vector3f) new Vector3f(+ 1 * imageRatio, + 1, planeOffset).normalise();
		projPlane[0] = Matrices.rotateVector(projPlane[0], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
		projPlane[1] = Matrices.rotateVector(projPlane[1], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
		projPlane[2] = Matrices.rotateVector(projPlane[2], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
		projPlane[3] = Matrices.rotateVector(projPlane[3], (Vector3f) new Vector3f(camrot2).scale((float) (Math.PI / 180)));
	}
	
	public static Color traceScreenPoint(float x, float y)
	{
		
		rayDir = Utils.lerp(projPlane[0], projPlane[1], projPlane[2], projPlane[3], x , y);
		
		IntersectionPoint closest = null;
		for (Object3d obj : Main.scene) {
			for(int i = 0; i < obj.mesh.tris.length; i ++)
			{
				if(Rays.rayToTriangle(Main.cam.pos, rayDir, obj.mesh.tris[i], intPoint)) {
					
					if(closest == null)
					{
						closest = new IntersectionPoint(intPoint, obj, i);
					}
					else if(Vector3f.sub(Main.cam.pos, closest.point, null).lengthSquared() > Vector3f.sub(Main.cam.pos, intPoint, null).lengthSquared())
					{
						closest.set(intPoint, obj, i);
					}
					
//					g.setColor(new Color(obj.mesh.colors[i].x,obj.mesh.colors[i].y,obj.mesh.colors[i].z, 0.1f));
//					g.drawRect(x , y, 1, 1);
				}
			}
		}
		if(closest != null)
		{
			Color baseCol = new Color(closest.object.mesh.colors[closest.triangleIndex].x,closest.object.mesh.colors[closest.triangleIndex].y,closest.object.mesh.colors[closest.triangleIndex].z);
			
			
			return baseCol;
		}
		return null;
		
	}
	
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
