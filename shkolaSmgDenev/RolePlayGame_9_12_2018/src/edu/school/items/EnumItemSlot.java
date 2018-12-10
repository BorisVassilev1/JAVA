package edu.school.items;

public enum EnumItemSlot {
	L_HAND(0),
	R_HAND(1),
	HEAD(2),
	CHEST(3),
	LEGS(4),
	FEET(5);
	
	private int id;
	
	EnumItemSlot(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
