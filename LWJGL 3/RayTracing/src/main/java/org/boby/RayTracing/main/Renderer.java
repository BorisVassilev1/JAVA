package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.objects.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

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
    
    public static void draw(Object3d obj, Quad renderingQuad)
	{
    	if ( Main.window.isResized() ) {
            glViewport(0, 0, Main.window.getWidth(), Main.window.getHeight());
            Main.window.setResized(false);
        }
    	renderingQuad.getPosition().set(0,0,(float) ( - 1/Math.tan(FOV / 2)));
    	
    	renderingQuad.getMaterial().getShader().bind();
		
		Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Main.window.getWidth(),  Main.window.getHeight(), Z_NEAR, Z_FAR);
		renderingQuad.getMaterial().getShader().setUniform("projectionMatrix", projectionMatrix);
		
		
		Matrix4f worldMatrix =
	            transform.getWorldMatrix(
	                renderingQuad.getPosition(),
	                renderingQuad.getRotation(),
	                renderingQuad.getScale());
		renderingQuad.getMaterial().getShader().setUniform("worldMatrix", worldMatrix);
		
		//obj.getMaterial().getShader().setUniform("texture_sampler", 0);
		//GL20.glActiveTexture(GL20.GL_TEXTURE0);
		//glBindTexture(GL_TEXTURE_2D, Main.tex.getID());
		glBindVertexArray(obj.getMesh().getVAOID());
		glBindBuffer(GL_ARRAY_BUFFER, obj.getMesh().getVertexBufferID());
		FloatBuffer vertexBuffer =  obj.getMesh().getVertexBuffer();
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		FloatBuffer translatedVertexBuffer = BufferUtils.createFloatBuffer(vertexBuffer.limit());
		for(int i = 0; i < vertexBuffer.limit(); i ++)
		{
			float num = vertexBuffer.get(i);
			if(i % 3 == 2)
			{
				translatedVertexBuffer.put(num - 10);
			}
			else
			{
				translatedVertexBuffer.put(num);
			}
		}
		
//		for(int i = 0; i < translatedVertexBuffer.limit(); i ++)
//		{
//			System.out.print(translatedVertexBuffer.get(i) + " ");
//		}
//		System.out.println();
		renderingQuad.getMaterial().getShader().setUniform("vertexArray",translatedVertexBuffer);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, obj.getMesh().getIndicesBufferID());
		IntBuffer indicesBuffer = obj.getMesh().getIndicesBuffer();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		renderingQuad.getMaterial().getShader().setUniform("indicesArray", indicesBuffer);
		
		UpdateViewPlane(Main.window.getWidth()/Main.window.getHeight(), FOV);
		
		renderingQuad.getMaterial().getShader().setUniform("ray00", projPlane[0]);
		renderingQuad.getMaterial().getShader().setUniform("ray10", projPlane[1]);
		renderingQuad.getMaterial().getShader().setUniform("ray01", projPlane[2]);
		renderingQuad.getMaterial().getShader().setUniform("ray11", projPlane[3]);
		
		
		//render the object
		glBindVertexArray(renderingQuad.getMesh().getVAOID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glDrawElements(GL_TRIANGLES, renderingQuad.getMesh().getIndicesCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        renderingQuad.getMaterial().getShader().unbind();
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
