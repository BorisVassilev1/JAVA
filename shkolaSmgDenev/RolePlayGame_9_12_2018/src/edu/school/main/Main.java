package edu.school.main;

import edu.school.items.Artifact;
import edu.school.items.EnumItemSlot;
import edu.school.items.Item;
import edu.school.player.Backpack;
import edu.school.player.Player;

public class Main {
	public static void main(String[] args) {
		Player p = new Player("Pesho", 10, 10, 100);
		Backpack bp = p.getBackpack();
		
		Item wood = new Item(0,"Wood", 1.5f, 64);
		Item stone = new Item(1, "Stone", 3.0f, 64);
		Artifact sword = new Artifact("Sword", EnumItemSlot.R_HAND, 10.0f, 30, 2, 0);
		Artifact shield = new Artifact("Shield", EnumItemSlot.L_HAND, 12.2f, 3, 18, 0);
		Artifact axe = new Artifact("Axe", EnumItemSlot.R_HAND, 9.5f, 25, 8, 0);
		
		bp.addItems(wood, 10);
		bp.addItems(stone, 100);
		bp.addItems(wood, 60);
		p.putAtribute(sword, EnumItemSlot.R_HAND);
		p.putAtribute(shield, EnumItemSlot.L_HAND);
		System.out.println(p.toString());
		p.putAtribute(axe, EnumItemSlot.R_HAND);
		System.out.println(p.toString());
	}
}
