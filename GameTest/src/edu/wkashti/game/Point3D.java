package edu.wkashti.game;

public class Point3D {
	private Vector3 CamPos = new Vector3(0, 0, 0);
	private Vector3 CamRot = new Vector3(0, 0, 0);
	private Vector3 e = new Vector3(0, 0, 1);

	Point3D(Vector3 CamPos, Vector3 CamRot, Vector3 e) {
		this.CamPos = CamPos;
		this.CamRot = CamRot;
		this.e = e;
	}

	Vector2 CalcPoint(Vector3 a) {
		Vector2 b = new Vector2(0, 0);
		Vector3 d = new Vector3(a.x - CamPos.x, a.y - CamPos.y, a.z - CamPos.z);

		// float pointoff = (float) Math.sqrt((a.x - CamPos.x) * (a.x - CamPos.x) + (a.y
		// - CamPos.y) * (a.y - CamPos.y));
		// pointoff = (float) Math.sqrt(pointoff * pointoff + (a.z - CamPos.z) * (a.z -
		// CamPos.z));
		//
		// d.x += pointoff * Math.cos(d.y) * Math.cos(d.x);
		// d.y += pointoff * Math.sin(d.y) * Math.cos(d.x);
		// d.z += pointoff * Math.sin(d.x);

		d.x = (float) (Math.cos(CamRot.x)
				* (Math.sin(CamRot.z) * (a.y - CamPos.y) + Math.cos(CamRot.z) * (a.x - CamPos.x))
				- Math.sin(CamRot.y) * (a.z - CamPos.z));
		d.y = (float) (Math.sin(CamRot.x)
				* (Math.cos(CamRot.y) * (a.z - CamPos.z) + Math.sin(CamRot.y)
				* (Math.sin(CamRot.z) * (a.y - CamPos.y) + Math.cos(CamRot.z) * (a.x - CamPos.x)))
				+ Math.cos(CamRot.x) * (Math.cos(CamRot.z) * (a.y - CamPos.y) - Math.sin(CamRot.z) * (a.x - CamPos.x)));
		d.z = (float) (Math.cos(CamRot.x)
				* (Math.cos(CamRot.y) * (a.z - CamPos.z) + Math.sin(CamRot.y)
				* (Math.sin(CamRot.z) * (a.y - CamPos.y) + Math.cos(CamRot.z) * (a.x - CamPos.x)))
				- Math.sin(CamRot.x) * (Math.cos(CamRot.z) * (a.y - CamPos.y) - Math.sin(CamRot.z) * (a.x - CamPos.x)));

		b.x = e.z / d.z * d.x - e.x;
		b.y = e.z / d.z * d.y - e.y;

		return b;
	}
}
