package RayTracing;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Objects.Object3d;

public class Camera {
	
	public float fov;
	private float aspect;
	private float near;
	private float far;
	
	public Vector3f pos;
	public Vector3f rot;
	
	public Camera(float fov,float aspect,float near,float far) {
		
		pos = new Vector3f();
		rot = new Vector3f();
		
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
		glRotatef(rot.x,1,0,0);
		glRotatef(rot.y,0,1,0);
		glRotatef(rot.z,0,0,1);
		glTranslatef(-pos.x, -pos.y, -pos.z);
		//System.out.println(rot);
	}
	
	public Vector3f Projection(Vector3f point, Object3d obj)
	{
		Vector3f positioned;
		positioned = Matrices.rotateVector(point, Matrices.degToRad(obj.rotation));
		positioned.set(Vector3f.add(positioned, obj.position, null));
		positioned.set(Vector3f.sub(positioned, pos, null));
		positioned.set(Matrices.rotateVector(positioned, Matrices.degToRad(rot)));
		
		if(positioned.z > 0)
		{
		Vector3f projected;
		float[][] projection = new float[][] {
			{1.4f / positioned.z,0               ,0},
			{0               ,1.4f / positioned.z,0},
		};
		projected = Matrices.matrixToVec(Matrices.matMul(projection, positioned));
		return projected;
		}
		else return null;
	}
	
	public void move(float speed, int dir)
	{
		pos.z -= speed * Math.sin(Math.toRadians(rot.y + 90 * dir));
		pos.x -= speed * Math.cos(Math.toRadians(rot.y + 90 * dir));
	}
	
	public void moveY(float speed)
	{
		pos.y += speed;
	}
	
	public void rotateY(float amt)
	{
		rot.y += amt;
	}
	
	public void rotateX(float amt)
	{
		rot.x += amt;
	}
	
	
	public float getX() {
		return pos.x;
	}

	public void setX(float x) {
		this.pos.x = x;
	}

	public float getY() {
		return pos.y;
	}

	public void setY(float y) {
		this.pos.y = y;
	}

	public float getZ() {
		return pos.z;
	}

	public void setZ(float z) {
		this.pos.z = z;
	}

	public float getRX() {
		return rot.x;
	}

	public void setRX(float rx) {
		this.rot.x = rx;
	}

	public float getRY() {
		return rot.y;
	}

	public void setRY(float ry) {
		this.rot.y = ry;
	}

	public float getRZ() {
		return rot.z;
	}

	public void setRZ(float rz) {
		this.rot.z = rz;
	}
}
