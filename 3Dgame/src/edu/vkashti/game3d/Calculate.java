package edu.vkashti.game3d;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("unused")
public class Calculate extends Main {

	static Vector3 position = new Vector3(0, 0, 100);
	
	static Vector3 rotation = new Vector3(0,0,0);
	
	static Vector3 camPosition = new Vector3(0,0,0);
	
	static float scale = 200;
	
	static Color fillColor = new Color(0,0,0,20);
	static Color wireColor = new Color(0,0,0,50);
	
	static Sphere3d Sphere = new Sphere3d(100,20);
	static Cube3d cube = new Cube3d(position, scale, fillColor, wireColor);
	
	public static void Start()
	{
		Sphere.init();
		Sphere.setFillColor(fillColor);
		Sphere.setWireColor(wireColor);
	}
	
	public static void Update() {
		HandleInput();
		Sphere.setPosition(position);
		Sphere.randomKoef = 0;
		Sphere.update();
		Point3D.setCamPos(camPosition);
		Point3D.setCamRot(rotation);
		
		
		
	}

	public static void Draw(Graphics g) {
		Sphere.view(g);
		

	}

	static void HandleInput() {
		if (IsKeyPressed[40]) {
			rotation.x -= 0.05;
		}
		if (IsKeyPressed[37]) {
			rotation.y -= 0.05;
		}
		if (IsKeyPressed[38]) {
			rotation.x += 0.05;
		}
		if (IsKeyPressed[39]) {
			rotation.y += 0.05;
		}
		if(IsKeyPressed[87])
		{
			camPosition.z += 3;
		}
		if(IsKeyPressed[83])
		{
			camPosition.z -= 3;
		}
		if(IsKeyPressed[16])
		{
			camPosition.y +=3;
		}
		if(IsKeyPressed[32])
		{
			camPosition.y -=3;
		}
		if(IsKeyPressed[68])
		{
			camPosition.x +=3;
		}
		if(IsKeyPressed[65])
		{
			camPosition.x -=3;
		}
	}
}
