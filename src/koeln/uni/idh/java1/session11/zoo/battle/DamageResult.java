package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Das Ergebnis einer Schadensberechnung: der verursachte Schaden, die
 * Typ-Effektivität und ob es ein kritischer Treffer (Volltreffer) war.
 */
public class DamageResult {

	private final int damage;
	private final double effectiveness;
	private final boolean critical;

	public DamageResult(int damage, double effectiveness, boolean critical) {
		this.damage = damage;
		this.effectiveness = effectiveness;
		this.critical = critical;
	}

	public int getDamage() {
		return damage;
	}

	public double getEffectiveness() {
		return effectiveness;
	}

	public boolean isCritical() {
		return critical;
	}
}
