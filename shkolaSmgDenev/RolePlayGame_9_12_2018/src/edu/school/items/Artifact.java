package edu.school.items;

public class Artifact extends Item{
	
	private EnumItemSlot itemSlot;
	private int attack;
	private int defence;
	private int minLevel; 
	
	public Artifact(String name, EnumItemSlot itemSlot, float weight, int attack, int defence, int minLevel) {
		super(-1,name, weight, 1);
		this.itemSlot = itemSlot;
		this.attack = attack;
		this.defence = defence;
		this.minLevel = minLevel;
	}

	public EnumItemSlot getItemSlot() {
		return itemSlot;
	}

	public void setItemSlot(EnumItemSlot itemSlot) {
		this.itemSlot = itemSlot;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int min_level) {
		this.minLevel = min_level;
	}
	
	@Override
	public String toString() {
		return "{" + "Name: " + this.name + ", Attack: " + this.attack + ", Defence: " + this.defence + ", Weight: " + this.weight +
				", MinLevel"  + this.minLevel + ", ItemSlot: " + this.itemSlot.toString() + ", StackSize: " + this.stackSize + "}";
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
