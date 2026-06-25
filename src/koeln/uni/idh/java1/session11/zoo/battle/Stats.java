package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Die unveränderlichen Basis-Werte eines Kämpfers: maximale Lebenspunkte,
 * Angriff, Verteidigung und Initiative (Geschwindigkeit).
 */
public class Stats {
	private final int maxHp;
	private final int attack;
	private final int defense;
	private final int speed;

	public Stats(int maxHp, int attack, int defense, int speed) {
		this.maxHp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpeed() {
		return speed;
	}
}
