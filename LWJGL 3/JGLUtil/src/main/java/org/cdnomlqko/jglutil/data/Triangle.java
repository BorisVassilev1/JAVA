package org.cdnomlqko.jglutil.data;

import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy.AABB;
import org.cdnomlqko.jglutil.data.BoundingVolumeHierarchy.Boundable;
import org.joml.Vector3f;

public class Triangle implements Boundable{
	public Vector3f v0;
	public Vector3f v1;
	public Vector3f v2;
	
	public Triangle(Vector3f v0, Vector3f v1, Vector3f v2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Triangle(float v00, float v01, float v02, float v10, float v11, float v12, float v20, float v21, float v22) {
		this.v0 = new Vector3f(v00, v01, v02);
		this.v1 = new Vector3f(v10, v11, v12);
		this.v2 = new Vector3f(v20, v21, v22);
	}

	@Override
	public AABB getBoundingBox() {
		Vector3f min = new Vector3f(v0), max = new Vector3f(v0);
		min.min(v1).min(v2);
		max.max(v1).max(v2);
		return new AABB(min, max);
	}
}
