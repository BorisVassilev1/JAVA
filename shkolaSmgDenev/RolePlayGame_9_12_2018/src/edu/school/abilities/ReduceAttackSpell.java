package edu.school.abilities;

import edu.school.player.Player;

public class ReduceAttackSpell implements Ability{

	@Override
	public void cast(Player caster, Player target) {
		target.addEffect(new AbilityEffect(caster, this));
	}

	@Override
	public void applyEffect(Player p, Player source) {
		float val = source.getAttack() * 0.5f;
		p.setAttack(p.getAttack() - val);
	}

}
