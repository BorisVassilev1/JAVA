package org.boby.RayTracing.rendering;

import static org.lwjgl.opengl.GL46.*;

import org.boby.RayTracing.data.Scene;
import org.boby.RayTracing.data.gameobject.GameObject;
import org.boby.RayTracing.data.gameobject.MeshedGameObject;
import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.shaders.ComputeShader;
import org.boby.RayTracing.shaders.VFShader;


public class Renderer {
	
	/**
	 * Draws an object on the screen, using the object's own shader.
	 * @param obj
	 */
	public static void draw(Object3d obj) {
		VFShader sh = obj.getShader();
		sh.bind();
		
		if(sh.hasUniform("worldMatrix")) {
			sh.setUniform("worldMatrix", obj.transform.getWorldMatrix());
		}
		
		// Will use the texture bound to GL_TEXTURE0
		if(sh.hasUniform("texture_sampler"))
			sh.setUniform("texture_sampler", 0);

		// bind VAO
		obj.getMesh().bind();
		
		glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0);
		
		obj.getMesh().unbind();
		
		sh.unbind();
	}
	
	public static void draw(MeshedGameObject obj, VFShader sh) { 
		sh.bind();
		
		// Set uniforms
		if(sh.hasUniform("worldMatrix")) {
			sh.setUniform("worldMatrix", obj.transform.getWorldMatrix());
		}
		
		if(sh.hasUniform("material_index")) {
			sh.setUniform("material_index", obj.getMaterialId());
		}
		
		if(sh.hasUniform("texture_sampler"))
			sh.setUniform("texture_sampler", 0);
		
		obj.mesh.bind();
		
		glDrawElements(GL_TRIANGLES, obj.mesh.getIndicesCount(), GL_UNSIGNED_INT, 0); 
		
		obj.mesh.unbind();
		
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
