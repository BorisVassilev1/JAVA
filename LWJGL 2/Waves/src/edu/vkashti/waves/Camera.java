package edu.vkashti.waves;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera {
	private float x;
	

	private float y;
	private float z;
	private float rx;
	private float ry;
	private float rz;
	
	private float fov;
	private float aspect;
	private float near;
	private float far;
	
	public Camera(float fov,float aspect,float near,float far) {
		x=0;
		y=0;
		z=0;
		rx=0;
		ry=0;
		rz=0;
		
		this.fov=fov;
		this.aspect=aspect;
		this.near=near;
		this.far=far;
		
		initProjection();
	}
	
	private void initProjection()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov,aspect,near,far);
		//glOrtho(100, -100, 100, -100, 0.3, 10000);
		glMatrixMode(GL_MODELVIEW);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
	}
	
	public void useView()
	{
		glRotatef(rx,1,0,0);
		glRotatef(ry,0,1,0);
		glRotatef(rz,0,0,1);
		glTranslatef(x, y, z);
	}
	
	public void move(float speed, int dir)
	{
		z += speed * Math.sin(Math.toRadians(ry + 90 * dir));
		x += speed * Math.cos(Math.toRadians(ry + 90 * dir));
	}
	
	public void moveY(float speed)
	{
		y += speed;
	}
	
	public void rotateY(float amt)
	{
		ry += amt;
	}
	
	public void rotateX(float amt)
	{
		rx += amt;
	}
	
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getRX() {
		return rx;
	}

	public void setRX(float rx) {
		this.rx = rx;
	}

	public float getRY() {
		return ry;
	}

	public void setRY(float ry) {
		this.ry = ry;
	}

	public float getRZ() {
		return rz;
	}

	public void setRZ(float rz) {
		this.rz = rz;
	}
}
