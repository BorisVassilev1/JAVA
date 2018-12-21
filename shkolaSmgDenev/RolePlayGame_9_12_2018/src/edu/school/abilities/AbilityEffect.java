package edu.school.abilities;

import edu.school.player.Player;

public class AbilityEffect {

	public AbilityEffect(Player source, Ability ability) {
		this.ability = ability;
		this.source = source;
	}
	
	private Player source;
	private Ability ability;
	public Player getSource() {
		return source;
	}
	public Ability getAbility() {
		return ability;
	}
	
}
