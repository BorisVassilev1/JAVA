package edu.school.abilities;

import edu.school.player.Player;

public class DamageSpell implements Ability {

	@Override
	public void cast(Player caster, Player target) {
		float damage = (caster.getAttack() * 1.3f + 250);
		target.dealDamage(damage);
	}

	@Override
	public void addEffect(Player p) {
		// TODO Auto-generated method stub
		
	}

}
