package edu.school.player;

import edu.school.items.Artifact;
import edu.school.items.EnumItemSlot;
import edu.school.items.ItemStack;

public class Player {

	private String name;
	private int level;
	private int xp;
	private Backpack backpack;

	private ItemStack[] atributes;

	private float baseAttack;
	private float baseDefence;
	private int maxHealth;
	private int health;

	private float attack;
	private float defence;

	public Player(String name, int baseAttack, int baseDefence, int maxHealth) {
		this.name = name;
		this.baseAttack = baseAttack;
		this.baseDefence = baseDefence;
		this.maxHealth = maxHealth;
		this.atributes = new ItemStack[6];
		this.backpack = new Backpack(10000, 10);
		this.health = maxHealth;
		recalculateStats();
	}

	private void recalculateStats() {
		this.attack = this.baseAttack;
		this.defence = this.baseDefence;
		for (ItemStack i : atributes) {
			if (i != null) {
				this.attack += ((Artifact) i.getItem()).getAttack();
				this.defence += ((Artifact) i.getItem()).getDefence();
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public Backpack getBackpack() {
		return backpack;
	}

	public void setBackpack(Backpack backpack) {
		this.backpack = backpack;
	}

	public ItemStack[] getAtributes() {
		return atributes;
	}

	public void setAtributes(ItemStack[] atributes) {
		this.atributes = atributes;
		recalculateStats();
	}

	public void setAtributes(Artifact head, Artifact l_hand, Artifact r_hand, Artifact chest, Artifact legs,
			Artifact feet) {
		if (head.getItemSlot().equals(EnumItemSlot.HEAD))
			this.atributes[0].setItem(head);
		if (l_hand.getItemSlot().equals(EnumItemSlot.L_HAND))
			this.atributes[1].setItem(l_hand);
		if (r_hand.getItemSlot().equals(EnumItemSlot.R_HAND))
			this.atributes[2].setItem(r_hand);
		if (chest.getItemSlot().equals(EnumItemSlot.CHEST))
			this.atributes[3].setItem(chest);
		if (legs.getItemSlot().equals(EnumItemSlot.LEGS))
			this.atributes[4].setItem(legs);
		if (feet.getItemSlot().equals(EnumItemSlot.FEET))
			this.atributes[5].setItem(feet);
		recalculateStats();
	}

	public void putAtribute(Artifact a, EnumItemSlot slot) {
		if (!a.getItemSlot().equals(slot))
			return;
		ItemStack curr = this.atributes[slot.getId()];
		if (curr == null) {
			this.atributes[slot.getId()] = new ItemStack(a, 1);
			recalculateStats();
			return;
		}
		this.backpack.addItems(curr);
		this.atributes[slot.getId()] = new ItemStack(a, 1);
		recalculateStats();
		return;
	}

	public float getBaseAttack() {
		return baseAttack;
	}

	public void setBaseAttack(float baseAttack) {
		this.baseAttack = baseAttack;
		recalculateStats();
	}

	public float getBaseDefence() {
		return baseDefence;
	}

	public void setBaseDefence(float baseDefence) {
		this.baseDefence = baseDefence;
		recalculateStats();
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public String toString() {
		return "{" + "Name: " + this.name + ", Level: " + this.level + ", XP: " + this.xp + ", Backpack: "
				+ this.backpack.toString() + ", Atributes: {" + EnumItemSlot.HEAD.toString() + ": "
				+ this.atributes[EnumItemSlot.HEAD.getId()] + ", " + EnumItemSlot.L_HAND.toString() + ": "
				+ this.atributes[EnumItemSlot.L_HAND.getId()] + ", " + EnumItemSlot.R_HAND.toString() + ": "
				+ this.atributes[EnumItemSlot.R_HAND.getId()] + ", " + EnumItemSlot.CHEST.toString() + ": "
				+ this.atributes[EnumItemSlot.CHEST.getId()] + ", " + EnumItemSlot.LEGS.toString() + ": "
				+ this.atributes[EnumItemSlot.LEGS.getId()] + ", " + EnumItemSlot.FEET.toString() + ": "
				+ this.atributes[EnumItemSlot.FEET.getId()] + ", " + "}" + ", BaseAttack: " + this.baseAttack
				+ ", BaseDefence: " + this.baseDefence + ", MaxHealth: " + this.maxHealth + ", Health: " + this.health
				+ ", Attack:" + this.attack + ", Defence: " + this.defence + "}";

	}
}
