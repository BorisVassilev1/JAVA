package RayTracing;

import org.lwjgl.util.vector.Vector3f;

public class Utils {
	public static Vector3f lerp(Vector3f a, Vector3f b, Vector3f c, Vector3f d, float x, float y)
	{
		Vector3f vecAB = lerp(a, b, x);
		Vector3f vecCD = lerp(c, d, x);
		return lerp(vecAB, vecCD, y);
	}
	public static float lerp(float a , float b, float k)
	{
		return (a * k + b * (1 - k));
	}
	public static Vector3f lerp(Vector3f a, Vector3f b, float k)
	{
		return new Vector3f(lerp(a.x, b.x, k), lerp(a.y, b.y, k), lerp(a.z, b.z, k));
	}
}
