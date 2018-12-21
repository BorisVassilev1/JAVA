package edu.school.abilities;

import edu.school.player.Player;

public class ReduceDefenceSpell implements Ability{

	@Override
	public void cast(Player caster, Player target) {
		target.addEffect(new AbilityEffect(caster,  this));
	}

	@Override
	public void applyEffect(Player p, Player source) {
		float val = source.getAttack() * 0.5f;
		p.setDefence(p.getDefence() - val);
	}

}
