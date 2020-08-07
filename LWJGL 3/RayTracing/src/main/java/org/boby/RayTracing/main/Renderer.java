package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.util.function.Function;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Transformation;
import org.boby.RayTracing.shaders.ComputeShader;
import org.boby.RayTracing.shaders.Shader;
import org.boby.RayTracing.utils.Texture2D;
import org.boby.RayTracing.utils.Time;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Renderer {
	public static final float FOV = (float) Math.toRadians(70f);

	public static final float Z_NEAR = 0.01f;

	public static final float Z_FAR = 1000.f;

	public static Transformation transform;

	public static Vector3f camPos = new Vector3f(0.0f, 1.0f, 0.0f);
	public static Vector3f camRot = new Vector3f(0.0f, 0.0f, 0.0f);

	public static void init() {
		transform = new Transformation();
	}

	public static void draw(Object3d obj) {
		Shader sh = obj.getShader();

		// Check if the window has been resized and adjust the viewport
		if (Main.window.isResized()) {
			glViewport(0, 0, Main.window.getWidth(), Main.window.getHeight());
			Main.window.setResized(false);
		}

		sh.bind();
		
		// Set uniforms
		
		Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Main.window.getWidth(), Main.window.getHeight(), Z_NEAR, Z_FAR);
		sh.setUniform("projectionMatrix", projectionMatrix);

		Matrix4f worldMatrix = transform.getWorldMatrix(obj.getPosition(), obj.getRotation(), obj.getScale());
		sh.setUniform("worldMatrix", worldMatrix);

		// Will use the texture bound to GL_TEXTURE0
		sh.setUniform("texture_sampler", 0);

		// bind VAO
		glBindVertexArray(obj.getMesh().getVAOID());
		
		// Enable VBOs: vertices, colors, texCoords
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		
		sh.unbind();
	}

	public static void Compute(ComputeShader shader, int num_groups_x, int num_groups_y, int num_groups_z) {
		shader.bind();

		glDispatchCompute(num_groups_x, num_groups_y, num_groups_z);
		glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

		shader.unbind();
	}
}
