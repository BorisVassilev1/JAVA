package kontrolni.example.wtf.krasi_proekt;

public class Card {
	private int health;
	private int damage;

	private boolean alive;
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Card(int health, int damage) {
		this.health = health;
		this.damage = damage;
	}
	
	public void attack(Card c) {
		c.takeDamage(this.damage);
		this.takeDamage(c.getDamage());
	}
	
	public void takeDamage(int dmg) {
		this.health -= dmg;
		checkDie();
	}
	
	private void checkDie() {
		 if(health > 0) {
			 alive = true;
		 }
		 else {
			 alive = false;
		 }
	}

	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public String toString() {
		return "Card: " + health + "  " + damage;
	}
}
