package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.objects.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

import shaders.ComputeShader;

public class Renderer {
	private static final float FOV = (float) Math.toRadians(70f);
	
    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;
    
    private static Transformation transform;
    
    private static Vector3f projPlane[];
    
    public static void init()
    {
    	transform = new Transformation();
    	projPlane = new Vector3f[4];
    }
    
    public static void draw(Object3d obj)
	{
    	if ( Main.window.isResized() ) {
            glViewport(0, 0, Main.window.getWidth(), Main.window.getHeight());
            Main.window.setResized(false);
        }
    	obj.getPosition().set(0,0,(float) ( - 1/Math.tan(FOV / 2)));
    	
    	obj.getMaterial().getShader().bind();
		if(obj.getMaterial().getShader().hasUniform("projectionMatrix")) {
			Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Main.window.getWidth(),  Main.window.getHeight(), Z_NEAR, Z_FAR);
			obj.getMaterial().getShader().setUniform("projectionMatrix", projectionMatrix);
		}
		
		if(obj.getMaterial().getShader().hasUniform("worldMatrix")) {
			Matrix4f worldMatrix =
		            transform.getWorldMatrix(
		                obj.getPosition(),
		                obj.getRotation(),
		                obj.getScale());
			obj.getMaterial().getShader().setUniform("worldMatrix", worldMatrix);
		}
		
		if(obj.getMaterial().getShader().hasUniform("texture_sampler")) {
			obj.getMaterial().getShader().setUniform("texture_sampler", 0);
		}
		//GL46.glActiveTexture(GL46.GL_TEXTURE0);
		//glBindTexture(GL_TEXTURE_2D, Main.tex.getID());
		
		
		//render the object
		glBindVertexArray(obj.getMesh().getVAOID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glDrawElements(GL_TRIANGLES, obj.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        obj.getMaterial().getShader().unbind();
	}
    
    
    public static void Compute(ComputeShader shader, int tex_output, int tex_w, int tex_h) {
    	shader.bind();
    	
    	
    	glBindTexture(GL_TEXTURE_2D, tex_output);
    	//glBindImageTexture(0, tex_output, 0, false, 0, GL_WRITE_ONLY, GL_RGBA32F);
    	
    	if(shader.hasUniform("resolution")) {
    		shader.setUniform("resolution", new Vector2f(Main.window.getWidth(), Main.window.getHeight()));
    	}
    	
    	glDispatchCompute(tex_w,tex_h, 1);
    	GL46.glMemoryBarrier(GL46.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
    	
    	shader.unbind();
    }
    
    
    
	public static void UpdateViewPlane(float imageRatio, float fovRad)
	{
		
		//float fieldOfView = 70;
		//fovDeg *= Math.PI / 180;
		float planeOffset = (float) (1/Math.tan(fovRad/2));
		Vector3f camrot2 = new Vector3f(0,180,0);
		projPlane[0] = (Vector3f) new Vector3f(- 1 * imageRatio, - 1, planeOffset).normalize();
		projPlane[1] = (Vector3f) new Vector3f(+ 1 * imageRatio, - 1, planeOffset).normalize();
		projPlane[2] = (Vector3f) new Vector3f(- 1 * imageRatio, + 1, planeOffset).normalize();
		projPlane[3] = (Vector3f) new Vector3f(+ 1 * imageRatio, + 1, planeOffset).normalize();
		//Matrix4f rotMat = transform.getWorldMatrix(new Vector3f(), new Vector3f(camrot2).mul((float) (Math.PI / 180)), 1);
		projPlane[0] = Matrices.rotateVector(projPlane[0], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[1] = Matrices.rotateVector(projPlane[1], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[2] = Matrices.rotateVector(projPlane[2], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[3] = Matrices.rotateVector(projPlane[3], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
	}
}
