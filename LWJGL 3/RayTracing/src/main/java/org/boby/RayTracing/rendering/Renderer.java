package org.boby.RayTracing.rendering;

import static org.lwjgl.opengl.GL46.*;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.shaders.ComputeShader;
import org.boby.RayTracing.shaders.VFShader;


public class Renderer {
	
	/**
	 * Draws an object on the screen, using the object's own shader.
	 * @param obj
	 */
	public static void draw(Object3d obj, Camera cam) {
		VFShader sh = obj.getShader();
		sh.bind();
		
		// Set uniforms
		if(sh.hasUniform("projectionMatrix")) {
			sh.setUniform("projectionMatrix", cam.getProjectionMatrix());
		}
		
		if(sh.hasUniform("worldMatrix")) {
			sh.setUniform("worldMatrix", obj.getWorldMatrix());
		}
		
		if(sh.hasUniform("viewMatrix")) {
			sh.setUniform("viewMatrix", cam.getViewMatrix());
		}

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
	
	/**
	 * Executes a compute shader.
	 * @param shader - the shader to be used
	 * @param num_groups - dimensions of execution multithreading.
	 */
	public static void Compute(ComputeShader shader, int num_groups_x,  int num_groups_y,  int num_groups_z) {
		shader.bind();

		glDispatchCompute(num_groups_x, num_groups_y, num_groups_z);
		glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

		shader.unbind();
	}
}
