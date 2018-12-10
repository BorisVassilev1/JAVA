package edu.school.items;

public class Item {
	
	private String name;
	private float weight;
	private int stackSize;
	private int id;
	
	public Item(int id, String name, float weight, int stackSize) {
		this.name = name;
		this.weight = weight;
		this.stackSize = stackSize;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "{" + "Id: " + this.id + ", Name: " + this.name + ", Weight: " + this.weight + ", StackSize: " + this.stackSize + "}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
}
