package rayTracing;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Raycaster {
	
	public Raycaster() {
		
	}
	
	Vector3f isin(Triangle t, Vector2f point)
	{
	  if(t.verticesP[0] != null && t.verticesP[1] != null && t.verticesP[2] != null)
	  {
		int sign = 10;
		boolean isin = true;
		float dist[] = new float[3];
		Vector3f intersection = new Vector3f(0,0,0);
		for(int i = 0; i < 3; i ++)
		{
			Vector2f prev = (i > 0)? t.verticesP[i-1] : t.verticesP[3 - 1];
			float face = orientedFace(prev,t.verticesP[i],point);
			if(sign == 10)
			{
				sign = sign(face);
			}
			else if(sign(face) != sign)
			{
				isin = false;
			}
			if(isin == true)
			{
				dist[i] = dist(point,t.verticesP[i]);
			}
	  	}
		if(isin)
		{
			intersection.set(
					((dist[1] + dist[2]) * t.vertices[0].x + (dist[0] + dist[2]) * t.vertices[1].x + (dist[0] + dist[1]) * t.vertices[2].x) / (dist[0]+ dist[1] + dist[2]),
					((dist[1] + dist[2]) * t.vertices[0].y + (dist[0] + dist[2]) * t.vertices[1].y + (dist[0] + dist[1]) * t.vertices[2].y) / (dist[0]+ dist[1] + dist[2]),
					((dist[1] + dist[2]) * t.vertices[0].z + (dist[0] + dist[2]) * t.vertices[1].z + (dist[0] + dist[1]) * t.vertices[2].z) / (dist[0]+ dist[1] + dist[2])
					);
			
			
//			Vector3f ab = lerp(t.vertices[0], t.vertices[1],dist[0],dist[1]);
//			Vector3f bc = lerp(t.vertices[1], t.vertices[2],dist[1],dist[2]);
//			Vector3f ac = lerp(t.vertices[0], t.vertices[2],dist[0],dist[2]);
			
//			float sum = dist[0] + dist[1] + dist[2];
//			dist[0] /= sum;
//			dist[1] /= sum;
//			dist[2] /= sum;
//			
//			Vector3f ab = lerp(t.vertices[0], t.vertices[1],1 - dist[0],1 - dist[1]);
//			Vector3f bc = lerp(t.vertices[1], t.vertices[2],1 - dist[1],1 - dist[2]);
//			Vector3f ac = lerp(t.vertices[0], t.vertices[2],1 - dist[0],1 - dist[2]);
//			
//			ab.set(ab.x / Main.width, ab.y / Main.height, ab.z);
//			bc.set(bc.x / Main.width, bc.y / Main.height, bc.z);
//			ac.set(ac.x / Main.width, ac.y / Main.height, ac.z);
//			
//			Vector2f abp = Main.cam.CalcPoint(ab);
//			Vector2f bcp = Main.cam.CalcPoint(bc);
//			Vector2f acp = Main.cam.CalcPoint(ac);
//			
//			if(abp != null)
//			{
//				Calculate.drawRect(abp, 0.1f, new Color(255,0,0));
//			}
//			if(acp != null)
//			{
//				Calculate.drawRect(acp, 0.1f, new Color(255,0,0));
//			}
//			if(bcp != null)
//			{
//				Calculate.drawRect(bcp, 0.1f, new Color(255,0,0));
//			}
//			
//			intersection.set(
//					(ab.x + bc.x + ac.x) / 3,
//					(ab.y + bc.y + ac.y) / 3,
//					(ab.z + bc.z + ac.z) / 3
//					);
			
			return intersection;
		}
	  }
	  return null;
	}
	
	int sign(float a)
	{
	  return (a >= 0)? 1 : -1 ;
	}
	
	float orientedFace (Vector2f p1, Vector2f p2, Vector2f p3) {
	    return (p2.x - p1.x) * (p3.y - p2.y) - (p3.x - p2.x) * (p2.y - p1.y);
	}
	
	float dist(Vector2f a, Vector2f b)
	{
		return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
	}
	
	Vector3f lerp(Vector3f a, Vector3f b, float ar, float br)
	{
		return new Vector3f(
				(a.x * ar + b.x * br) / (ar + br),
				(a.y * ar + b.y * br) / (ar + br),
				(a.z * ar + b.z * br) / (ar + br)
				);
	}
	
	float max(float a,  float b, float c)
	{
		return Math.max(a, Math.max(b,c));
	}
}
