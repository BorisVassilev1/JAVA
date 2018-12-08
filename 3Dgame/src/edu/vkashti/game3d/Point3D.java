package edu.vkashti.game3d;

public class Point3D {
	private static Vector3 CamPos = new Vector3(0, 0, 0);
	private static Vector3 CamRot = new Vector3(0, 0, 0);
	private static Vector3 e = new Vector3(0, 0, 100);

	Point3D(Vector3 CamPos, Vector3 CamRot, Vector3 e) {
		this.setCamPos(CamPos);
		this.setCamRot(CamRot);
		this.e = e;
	}

	static Vector2 CalcPoint(Vector3 a) {
		Vector2 b = new Vector2(0, 0);
		Vector3 d = new Vector3(a.x - CamPos.x, a.y - CamPos.y, a.z - CamPos.z);

		d.x = (float) (Math.cos(getCamRot().x)
				* (Math.sin(getCamRot().z) * (a.y - CamPos.y) + Math.cos(getCamRot().z) * (a.x - CamPos.x))
				- Math.sin(getCamRot().y) * (a.z - CamPos.z));
		d.y = (float) (Math.sin(getCamRot().x)
				* (Math.cos(getCamRot().y) * (a.z - CamPos.z) + Math.sin(getCamRot().y)
						* (Math.sin(getCamRot().z) * (a.y - CamPos.y) + Math.cos(getCamRot().z) * (a.x - CamPos.x)))
				+ Math.cos(getCamRot().x)
						* (Math.cos(getCamRot().z) * (a.y - CamPos.y) - Math.sin(getCamRot().z) * (a.x - CamPos.x)));
		d.z = (float) (Math.cos(getCamRot().x)
				* (Math.cos(getCamRot().y) * (a.z - CamPos.z) + Math.sin(getCamRot().y)
						* (Math.sin(getCamRot().z) * (a.y - CamPos.y) + Math.cos(getCamRot().z) * (a.x - CamPos.x)))
				- Math.sin(getCamRot().x)
						* (Math.cos(getCamRot().z) * (a.y - CamPos.y) - Math.sin(getCamRot().z) * (a.x - CamPos.x)));

		if (d.z >= 0.0001) {
			b.x = e.z / d.z * d.x - e.x;
			b.y = e.z / d.z * d.y - e.y;

			return b;
		} else {
			return null;
		}
	}

	static Vector3 add(Vector3 a, Vector3 b) {
		return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Vector3 getCamRot() {
		return CamRot;
	}

	public static void setCamRot(Vector3 camRot) {
		CamRot = camRot;
	}

	public static void setCamPos(Vector3 camPosition) {
		CamPos = camPosition;

	}
}
