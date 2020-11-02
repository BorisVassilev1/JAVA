package org.cdnomlqko.jglutil;

import static org.lwjgl.opengl.GL46.*;

import org.cdnomlqko.jglutil.data.Renderable;
import org.cdnomlqko.jglutil.mesh.Mesh;
import org.cdnomlqko.jglutil.shader.ComputeShader;
import org.cdnomlqko.jglutil.shader.VFShader;

import com.sun.org.apache.xpath.internal.functions.Function;

/**
 * A static class for rendering objects in the current OpenGL context.
 * @author CDnoMlqko
 *
 */
public class Renderer {
	
	/**
	 * Draws an object on the screen, using the object's own shader.
	 * @param obj
	 */
	public static void draw(Renderable obj) { 
		VFShader sh = obj.getShader();
		sh.bind();
		
		obj.prepareForRender();
		
		obj.getMesh().bind();
		
		glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0); 
		
		obj.getMesh().unbind();
		
		sh.unbind();
	}
	
	/**
	 * Draws a {@link Iterable} of {@link Renderable} objects on the screen using instanced rendering. Ignores the given objects' mesh and shaders. Only calls their {@link Renderable#prepareForRender()} before drawing. Draws the given mesh using the given shader
	 * @param objects
	 * @param mesh
	 * @param shader
	 */
	public static void drawInstanced(Iterable<Renderable> objects, Mesh mesh, VFShader shader) {
		mesh.bind();
		shader.bind();
		
		for(Renderable obj : objects) {
			obj.prepareForRender();
			glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0);
		}
		
		mesh.unbind();
		shader.unbind();
	}
	
	/**
	 * Executes a compute shader.
	 * @param shader - the shader to be used
	 * @param num_groups - dimensions of execution multithreading.
	 */
	public static void Compute(ComputeShader shader, int num_groups_x,  int num_groups_y,  int num_groups_z, Runnable callBeforeCompute) {
		shader.bind();
		
		callBeforeCompute.run();
		
		glDispatchCompute(num_groups_x, num_groups_y, num_groups_z);
		glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);

		shader.unbind();
	}
	
	/**
	 * {@link #Compute(ComputeShader, int, int, int, Runnable)}, but without a lambda.
	 * @param shader - the shader to be used
	 * @param num_groups - dimensions of execution multithreading.
	 */
	public static void Compute(ComputeShader shader, int num_groups_x,  int num_groups_y,  int num_groups_z) {
		Compute(shader, num_groups_x, num_groups_y, num_groups_z, () -> {});
	}
}
