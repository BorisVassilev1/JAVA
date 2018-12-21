package edu.school.abilities;

import edu.school.player.Player;

public class HealingSpell implements Ability {

	@Override
	public void cast(Player caster, Player target) {
		float value = caster.getDefence() * 0.5f + 100;
		target.heal(value);
	}

	@Override
	public void applyEffect(Player p, Player source) {
		
	}

}
