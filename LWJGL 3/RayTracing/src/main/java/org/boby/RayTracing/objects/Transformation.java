package org.boby.RayTracing.objects;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * a Factory for Matrix4f
 * @author Boby
 *
 */
public class Transformation {
	private final Matrix4f projectionMatrix;

    private final Matrix4f worldMatrix;

    public Transformation() {
        worldMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public final Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
    	worldMatrix.translation(offset).
                rotateX((float)Math.toRadians(rotation.x)).
                rotateY((float)Math.toRadians(rotation.y)).
                rotateZ((float)Math.toRadians(rotation.z)).
                scale(scale);
        return worldMatrix;
    }
}
