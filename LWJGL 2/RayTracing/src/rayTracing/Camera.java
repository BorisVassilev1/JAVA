package rayTracing;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private static Vector3f pos = new Vector3f(0, 0, 0);
	private static Vector3f rot = new Vector3f(0, 0, 0);
	private static Vector3f e = new Vector3f(0, 0, 100);

	Camera(Vector3f CamPos, Vector3f CamRot, Vector3f e) {
		setCamPos(CamPos);
		setCamRot(CamRot);
		this.e = e;
	}

	Vector2f CalcPoint(Vector3f a) {
		Vector2f b = new Vector2f(0, 0);
		Vector3f d = new Vector3f(a.x - pos.x, a.y - pos.y, a.z - pos.z);

		d.x = (float) (Math.cos(getCamRot().x)
				* (Math.sin(getCamRot().z) * (a.y - pos.y) + Math.cos(getCamRot().z) * (a.x - pos.x))
				- Math.sin(getCamRot().y) * (a.z - pos.z));
		d.y = (float) (Math.sin(getCamRot().x)
				* (Math.cos(getCamRot().y) * (a.z - pos.z) + Math.sin(getCamRot().y)
						* (Math.sin(getCamRot().z) * (a.y - pos.y) + Math.cos(getCamRot().z) * (a.x - pos.x)))
				+ Math.cos(getCamRot().x)
						* (Math.cos(getCamRot().z) * (a.y - pos.y) - Math.sin(getCamRot().z) * (a.x - pos.x)));
		d.z = (float) (Math.cos(getCamRot().x)
				* (Math.cos(getCamRot().y) * (a.z - pos.z) + Math.sin(getCamRot().y)
						* (Math.sin(getCamRot().z) * (a.y - pos.y) + Math.cos(getCamRot().z) * (a.x - pos.x)))
				- Math.sin(getCamRot().x)
						* (Math.cos(getCamRot().z) * (a.y - pos.y) - Math.sin(getCamRot().z) * (a.x - pos.x)));

		if (d.z >= 0.0001) {
			b.x = e.z / d.z * d.x - e.x;
			b.y = e.z / d.z * d.y - e.y;

			return b;
		} else {
			return null;
		}
	}

	Vector3f add(Vector3f a, Vector3f b) {
		return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	static Vector3f getCamRot() {
		return rot;
	}

	public void setCamRot(Vector3f camRot) {
		rot = camRot;
	}

	public void setCamPos(Vector3f camPosition) {
		pos = camPosition;
	}
	public void move(float speed, int dir)
	{
		pos.z += speed * Math.sin(Math.toRadians(rot.y + 90 * dir));
		pos.x += speed * Math.cos(Math.toRadians(rot.y + 90 * dir));
	}
	
	public void moveY(float speed)
	{
		pos.y += speed;
	}
	
	public void rotateY(float amt)
	{
		rot.y += amt;
	}
	
	public void rotateX(float amt)
	{
		rot.x += amt;
	}

}
