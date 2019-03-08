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

import io.socket.client.Ack;

public class Player {
	Vector2f pos;
	float size;
	Vector3f color;
	
	Vector2f direction;
	float directionRad = (float) (Math.PI / 4);
	
	float movementSpeed = 200;
	Vector2f currentDestination;
	float currentDestinatonRad = 0;
	boolean isMoving = false;
	byte team;
	
	boolean isActive;
	
	int maxHealth = 100;
	int currentHealth;
	
	boolean canShoot = true;
	
	public Player(float x, float y, float size, byte team) {
		this.pos = new Vector2f(x,y);
		this.size = size;
		this.team = team;
		this.isActive = true;
		direction = new Vector2f();
		color = new Vector3f(1,1 ,1);
		currentHealth = maxHealth;
		currentDestination = (Vector2f) pos.clone();
	}
	
	public Player(Vector2f pos, float size, byte team) {
		this.pos = pos;
		this.size = size;
		this.team = team;
		color = new Vector3f(0,1 ,0);
	}
	
	public void draw()// рисуване на играча
	{
		if(isActive)
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
	}
	
	public void Input()
	{
		if(isActive)
		{
			boolean willmove = false;
			boolean willShoot = false;
			float x = 0, y = 0;
			while(Mouse.next())//разглеждане на всички ивенти на мишката между предишното и текущото извикване на Input()
			{
				if(Mouse.isInsideWindow() && Mouse.getEventButton() == 1 && Mouse.getEventButtonState() == true)// ляв бутон кликнат върху прозореца
				{
					x = Mouse.getX();
					y = Mouse.getY();
					willmove = true;
				}
				if(Mouse.isInsideWindow() && Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == true)// десен -----||---------
				{
					x = Mouse.getX();
					y = Mouse.getY();
					willShoot = true;
				}
			}
			
			if(willmove)// ако има промяна в състоянието
			{
				final JSONObject obj = new JSONObject();
				try {//информация за играча
					obj.put("x", x);
					obj.put("y", y);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("emitting \"move\" packet ");
				Main.gameSocket.emit("move", obj, new Ack() {
					
					@Override
					public void call(Object... args) {
						//System.out.println("\"move\" packet returned");
						JSONObject pos = (JSONObject) args[0];
						JSONObject dest = (JSONObject) args[1];
	//					System.out.println("prev pos: " + Player.this.pos);
	//					System.out.println("prev dest: " + currentDestination);
	//					System.out.println("sent dest: " + obj);
	//					System.out.println("recieved pos: " + pos);
	//					System.out.println("recieved dest: " + dest);
						try {
							setPos((float) pos.getDouble("x"), (float) pos.getDouble("y"));
							setDestination((float) dest.getDouble("x"), (float) dest.getDouble("y"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});//изпращане на пакет със информацията
			}
			
			if(willShoot && canShoot)
			{
				canShoot = false;
				new java.util.Timer().schedule( 
				        new java.util.TimerTask() {
				            @Override
				            public void run() {
				            	canShoot = true;
				            }
				        }, 
				        1000 
				);
				JSONObject mousexy = new JSONObject();
				try {
					mousexy.put("x", x);
					mousexy.put("y", y);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Main.gameSocket.emit("shoot", mousexy, new Ack() {
					
					@Override
					public void call(Object... args) {
						try {
							JSONObject bull = (JSONObject)args[0];
							JSONObject pos = bull.getJSONObject("pos");
							JSONObject dir = bull.getJSONObject("direction");
							final Bullet bullet = new Bullet( new Vector2f((float)pos.getDouble("x"), (float)pos.getDouble("y")), (float)Math.atan2(dir.getDouble("y"), dir.getDouble("x")));
							new java.util.Timer().schedule( 
							        new java.util.TimerTask() {
							            @Override
							            public void run() {
							            	Main.app.bullets.remove(Main.app.bullets.indexOf(bullet));
							            }
							        }, 
							        5000 
							);
							Main.app.bullets.add(bullet);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
			}
		}
	}
	
	public void update()// това, което прави играчът всеки кадър от играта
	{
		if(isMoving && isActive)
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
	
	public void setMoving(boolean flag)
	{
		this.isMoving = flag;
	}
	
	public void setDestination(float x, float y)
	{
		this.setDir(x - pos.x, y - pos.y);
		if(direction.lengthSquared() != 0)//the vecmath library makes the vector with coordinates NaN when normalizing a zero vector 
		direction.normalize();
		currentDestination.set(x,y);
		isMoving = true;
	}
	
	public void dealDamage(int amt)
	{
		this.currentHealth -= amt;
		if(currentHealth <= 0)
		{
			die();
		}
	}
	
	public void setActive(boolean flag)
	{
		this.isActive = flag;
	}
	
	public void die()
	{
		System.out.println("im dead");
		this.setActive(false);
	}
	
	@Override
	public String toString() {// not needed
		return "{\"X\":" + this.pos.x + ", \"Y\":" + this.pos.y + "}";
	}
}
