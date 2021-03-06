package rayTracing;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {
	public static void handleInput()
	{
		float moveSpeed = 0.01f;
		float rotateSpeed = 0.005f;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			moveSpeed += 0.1;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			Main.cam.move(moveSpeed * 5, 1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			Main.cam.move(-moveSpeed * 5, 1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			Main.cam.move(-moveSpeed * 5, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			Main.cam.move(moveSpeed * 5, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			Main.cam.rotateY(-rotateSpeed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			Main.cam.rotateY(rotateSpeed);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			Main.cam.moveY(moveSpeed * 5);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			Main.cam.moveY(-moveSpeed * 5);
		}
		
		if(Mouse.getDWheel() != 0)
		{
			Main.isEsc = !Main.isEsc;
		}
	}
	
	public static void handleMouse(boolean isEsc)
	{
		if(!isEsc)
		{
			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			
			Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
			
			int newMouseX = Mouse.getX();
			int newMouseY = Mouse.getY();
			
			int genDX = mouseX - newMouseX;
			int genDY = mouseY - newMouseY;
			
			Main.cam.rotateY( (Mouse.getDX() + genDX) /20f);
			Main.cam.rotateX( -(Mouse.getDY() + genDY) /20f);
			
			Mouse.updateCursor();
		}
	}
}
