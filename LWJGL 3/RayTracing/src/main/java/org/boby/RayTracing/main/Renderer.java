package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Quad;
import org.boby.RayTracing.objects.Transformation;
import org.boby.RayTracing.utils.Matrices;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class Renderer {
	private static final float FOV = (float) Math.toRadians(70f);
	
    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;
    
    private static Transformation transform;
    
    static Vector3f[] projPlane = new Vector3f[4];
    
    public static void init()
    {
    	transform = new Transformation();
    	UpdateViewPlane(Main.window.getWidth() / Main.window.getHeight(), FOV);
    }
    
    public static void draw(Object3d targetPlane, Object3d obj)
	{
    	//Main.renderQuad.getPosition().set(0.0f,0.0f,(float) (-1/Math.tan(FOV/2)));
    	if ( Main.window.isResized() ) {
            GL11.glViewport(0, 0, Main.window.getWidth(), Main.window.getHeight());
            Main.window.setResized(false);
        }
    	
    	targetPlane.getMaterial().getShader().bind();
		
		//Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Main.window.getWidth(),  Main.window.getHeight(), Z_NEAR, Z_FAR);
    	Matrix4f projectionMatrix = new Matrix4f().perspective(FOV, Main.window.getWidth()/Main.window.getHeight(), Z_NEAR, Z_FAR);
		targetPlane.getMaterial().getShader().setUniform("projectionMatrix", projectionMatrix);
		
		
		Matrix4f worldMatrix =
	            transform.getWorldMatrix(
	                targetPlane.getPosition(),
	                targetPlane.getRotation(),
	                targetPlane.getScale());
		targetPlane.getMaterial().getShader().setUniform("worldMatrix", worldMatrix);
		
		//obj.getMaterial().getShader().setUniform("texture_sampler", 0);
    	
    	UpdateViewPlane(Main.window.getWidth() / Main.window.getHeight(),FOV);
    	//System.out.println(projPlane[0] + " " + projPlane[1] + " " + projPlane[2] + " " + projPlane[3]);
    	//targetPlane.getMaterial().getShader().setUniform("ray00",projPlane[0]);
    	//targetPlane.getMaterial().getShader().setUniform("ray10",projPlane[1]);
    	//targetPlane.getMaterial().getShader().setUniform("ray01",projPlane[2]);
    	//targetPlane.getMaterial().getShader().setUniform("ray11",projPlane[3]);
    	
    	GL30.glBindVertexArray(targetPlane.getMesh().getVAOID());
    	
    	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, obj.getMesh().getVertexBufferID());
    	FloatBuffer verticesBuffer = obj.getMesh().getVerticesBuffer();
    	//targetPlane.getMaterial().getShader().setUniform("vertexArray",verticesBuffer);
    	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    	
    	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, obj.getMesh().getIndicesBufferID());
    	IntBuffer indicesBuffer = obj.getMesh().getIndicesBuffer();
    	//targetPlane.getMaterial().getShader().setUniform("indicesArray", indicesBuffer);
    	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    	
    	
		GL20.glActiveTexture(GL20.GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Main.tex.getID());
		
		//render the object
		
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);       
        GL20.glEnableVertexAttribArray(2); 
        GL11.glDrawElements(GL11.GL_TRIANGLES, targetPlane.getMesh().getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        targetPlane.getMaterial().getShader().unbind();
	}
    
    public static void UpdateViewPlane(float imageRatio, float fovDeg)
	{
		
		//float fieldOfView = 70;
		//fovDeg *= Math.PI / 180;
		float planeOffset = (float) (1/Math.tan(fovDeg/2));
		//System.out.println(planeOffset);
		//Vector3f camrot2 = new Vector3f(Main.cam.rot.x, Main.cam.rot.y + 180, Main.cam.rot.z);
		Vector3f camrot2 = new Vector3f(0,0,0);
		projPlane[0] = (Vector3f) new Vector3f(- 1 * imageRatio, - 1, planeOffset).normalize();
		projPlane[1] = (Vector3f) new Vector3f(+ 1 * imageRatio, - 1, planeOffset).normalize();
		projPlane[2] = (Vector3f) new Vector3f(- 1 * imageRatio, + 1, planeOffset).normalize();
		projPlane[3] = (Vector3f) new Vector3f(+ 1 * imageRatio, + 1, planeOffset).normalize();
		projPlane[0] = Matrices.rotateVector(projPlane[0], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[1] = Matrices.rotateVector(projPlane[1], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[2] = Matrices.rotateVector(projPlane[2], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
		projPlane[3] = Matrices.rotateVector(projPlane[3], (Vector3f) new Vector3f(camrot2).mul((float) (Math.PI / 180)));
	}
}
