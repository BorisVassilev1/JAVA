package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
	private static final float FOV = (float) Math.toRadians(70f);
	
    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;
    
    private static Transformation transform;
    
    public static void init()
    {
    	transform = new Transformation();
    }
    
    public static void draw(Object3d obj)
	{
    	if ( Main.window.isResized() ) {
            GL11.glViewport(0, 0, Main.window.getWidth(), Main.window.getHeight());
            Main.window.setResized(false);
        }
    	
    	obj.getMaterial().getShader().bind();
		
		//Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Main.window.getWidth(),  Main.window.getHeight(), Z_NEAR, Z_FAR);
    	Matrix4f projectionMatrix = new Matrix4f().perspective(FOV, Main.window.getWidth()/Main.window.getHeight(), Z_NEAR, Z_FAR);
		obj.getMaterial().getShader().setUniform("projectionMatrix", projectionMatrix);
		
		
		Matrix4f worldMatrix =
	            transform.getWorldMatrix(
	                obj.getPosition(),
	                obj.getRotation(),
	                obj.getScale());
		obj.getMaterial().getShader().setUniform("worldMatrix", worldMatrix);
		
		obj.getMaterial().getShader().setUniform("texture_sampler", 0);
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Main.tex.getID());
		
		//render the object
		GL30.glBindVertexArray(obj.getMesh().getVAOID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);       
        GL20.glEnableVertexAttribArray(2); 
        GL11.glDrawElements(GL11.GL_TRIANGLES, obj.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        obj.getMaterial().getShader().unbind();
	}
}
