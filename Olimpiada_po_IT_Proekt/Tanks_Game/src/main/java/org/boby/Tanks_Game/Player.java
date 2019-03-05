package org.boby.Tanks_Game;

import static org.lwjgl.opengl.GL11.*;

import javax.print.attribute.standard.Destination;
import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.boby.Tanks_Game.launcher.Main;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;

public class Player {
	Vector2f pos;
	float size;
	String UUID;
	Vector3f color;
	
	Vector2f direction;
	float directionRad = (float) (Math.PI / 4);
	
	float movementSpeed = 200;
	Vector2f currentDestination;
	float currentDestinatonRad = 0;
	boolean isMoving = false;
	
	public Player(float x, float y, float size, String UUID) {
		this.pos = new Vector2f(x,y);
		this.size = size;
		this.UUID = UUID;
		direction = new Vector2f();
		color = new Vector3f(0,1 ,0);
		currentDestination = (Vector2f) pos.clone();
	}
	
	public Player(Vector2f pos, float size, String UUID) {
		this.pos = pos;
		this.size = size;
		this.UUID = UUID;
		color = new Vector3f(0,1 ,0);
	}
	
	public void draw()// рисуване на играча
	{
		Vector2f v1 = new Vector2f(-size/2f, -size/2f);
		Vector2f v2 = new Vector2f(+size/2f, -size/2f);
		Vector2f v3 = new Vector2f(+size/2f, +size/2f);
		Vector2f v4 = new Vector2f(-size/2f, +size/2f);
		
		v1 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v1), directionRad));// ротиране на вурховете на фигурата
		v2 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v2), directionRad));
		v3 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v3), directionRad));
		v4 = Utils.vec3fto2f(Matrices.rotateVec2f(Utils.vec2fto3f(v4), directionRad));
		v1.add(pos);// добавяне на позицията
		v2.add(pos);
		v3.add(pos);
		v4.add(pos);
		glBegin(GL_QUADS);// рисуване
		{
			glColor3f(color.x, color.y, color.z);
			Utils.glVertexv2f(Utils.PixelsToScreen(v1));
			Utils.glVertexv2f(Utils.PixelsToScreen(v2));
			Utils.glVertexv2f(Utils.PixelsToScreen(v3));
			Utils.glVertexv2f(Utils.PixelsToScreen(v4));
		}
		glEnd();
	}
	
	public void Input()
	{
		boolean willmove = false;
		while(Mouse.next())//разглеждане на всички ивенти на мишката между предишното и текущото извикване на Input()
		{
			if(Mouse.isInsideWindow() && Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == true)// ляв бутон кликнат върху прозореца
			{
				float x = Mouse.getX();
				float y = Mouse.getY();
				setDestination(x, y);
				willmove = true;
			}
		}
		
		if(willmove)// ако има промяна в състоянието
		{
			JSONObject obj = new JSONObject();
			try {
				obj.put("X", pos.x);//информация за играча
				obj.put("Y", pos.y);
				//obj.put("dirX", direction.x);
				//obj.put("dirY", direction.y);
				obj.put("ID", Main.app.myID);
				obj.put("isMoving", isMoving);
				obj.put("destX", currentDestination.x);
				obj.put("destY", currentDestination.y);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.gameSocket.emit("move", obj);//изпращане на пакет със информацията
		}
	}
	
	public void update()// това, което прави играчът всеки кадър от играта
	{
		if(isMoving)
		{
			Vector2f a = (Vector2f) pos.clone();
			a.sub(currentDestination);
			if(a.lengthSquared() > Time.deltaTime * this.movementSpeed)// ако разстоянието до целта е повече от това, което иргачът ще измине за един кадър
			{
				Vector2f b = (Vector2f) (direction.clone());// мести се
				b.scale((float) Time.deltaTime * this.movementSpeed);
				pos.add(b);
			}
			else
			{
				Vector2f b = (Vector2f) (direction.clone());// в противен случай, се мести се до целта
				b.scale(a.lengthSquared());
				pos.add(b);
				this.isMoving = false;
			}
		}
	}
	
	public void setPos(float x, float y)
	{
		this.pos.set(x,y);
	}
	public void setDir(float x, float y) {
		this.direction.set(x, y);
	}
	
	public void setMoving(boolean isMoving)
	{
		this.isMoving = isMoving;
	}
	
	public void setDestination(float x, float y)
	{
		this.setDir(x - pos.x, y - pos.y);
		direction.normalize();
		currentDestination.set(x,y);
		isMoving = true;
	}
	
	@Override
	public String toString() {
		return "{\"X\":" + this.pos.x + ", \"Y\":" + this.pos.y + ", \"ID\"" + this.UUID + "}";
	}
}
