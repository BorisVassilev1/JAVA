package edu.school.abilities;

import edu.school.player.Player;

public interface Ability {
	
	float cooldown = 0;
	
	void cast(Player caster, Player target);
	
	void applyEffect(Player p, Player source);
}
