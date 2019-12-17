package org.boby.RayTracing.main;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

import org.boby.RayTracing.objects.Object3d;
import org.boby.RayTracing.objects.Transformation;
import org.boby.RayTracing.utils.Texture;
import org.boby.RayTracing.utils.Time;
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
    
    public static Vector3f camPos = new Vector3f(0.0f,1.0f,0.0f);
    public static Vector3f camRot = new Vector3f(0.0f,0.0f,0.0f);
    
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
		
		//int bufferWithSpheres;
		//bufferWithSpheres = glGenBuffers();
		//glBindBuffer(GL_SHADER_STORAGE_BUFFER, bufferWithSpheres);
		//glBufferData(GL_SHADER_STORAGE_BUFFER,new float[] {1.0f}, GL_DYNAMIC_DRAW);
		//glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, bufferWithSpheres);
		
		//glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
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
    
    
    public static void Compute(ComputeShader shader, Texture tex) {
    	shader.bind();
    	
    	
    	glBindTexture(GL_TEXTURE_2D, tex.getID());
    	
    	if(shader.hasUniform("resolution")) {
    		shader.setUniform("resolution", new Vector2f(Main.window.getWidth(), Main.window.getHeight()));
    	}
    	
    	if(shader.hasUniform("cameraMatrix")) {
    		Matrix4f mat = transform.getWorldMatrix(camPos, camRot , 1.0f);
    		shader.setUniform("cameraMatrix", mat);
    	}
    	
    	if(shader.hasUniform("fov")) {
    		shader.setUniform("fov", FOV);
    	}
    	
    	/*if(shader.hasUniform("deltaTime")) {
    		shader.setUniform("deltaTime", (int)Time.deltaTimeI);
    		System.out.println((int)Time.deltaTime);
    	}*/
    	
//    	System.out.println(Time.timeNow / 1000000000.0);
    	Vector3f col = new Vector3f((float)Math.sin(Time.timeNow / 1000000000.0) / 2 + 0.5f,(float) Math.cos(Time.timeNow / 1000000000.0) / 2 + 0.5f, 0.0f);
//    	System.out.println(col);
    	FloatBuffer buff = BufferUtils.createFloatBuffer(36);
		buff.put(new float[] {
				 0.5f, 1.3f, 1.5f, 0.0f, col.x, col.y, col.z, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f,
				 1.5f, col.x + 0.5f, 2.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f,
				-0.6f, col.y + 0.5f, 1.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f
				});
		buff.flip();
		shader.setSSBO("shader_data", buff, GL_DYNAMIC_DRAW);
    	
    	glDispatchCompute(tex.getWidth(), tex.getHeight(), 1);
    	GL46.glMemoryBarrier(GL46.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
    	
    	shader.unbind();
    }
}
