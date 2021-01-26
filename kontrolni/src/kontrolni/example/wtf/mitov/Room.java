package kontrolni.example.wtf.mitov;

import java.util.ArrayList;

public class Room {
	private int x, y;
	private double width;
	private double height;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public ArrayList<Furniture> getFurniture() {
		return furniture;
	}

	public void setFurniture(ArrayList<Furniture> furniture) {
		this.furniture = furniture;
	}

	private void remove() {
		// TODO
	}
	
	ArrayList<Furniture> furniture;
	
	
	@Override
	protected Object clone() {
		return new Room(x, y, width, height);
	}

	public Room(int x, int y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
