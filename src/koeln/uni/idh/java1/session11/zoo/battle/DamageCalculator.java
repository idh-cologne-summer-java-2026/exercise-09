package koeln.uni.idh.java1.session11.zoo.battle;

import java.util.Random;

/**
 * Berechnet den Schaden einer Attacke nach einer vereinfachten Pokémon-Formel:
 *
 * <pre>
 * schaden = ( (2*level/5 + 2) * staerke * (angriff/verteidigung) / 50 + 2 )
 *           * typEffektivitaet
 *           * zufall(0.85 .. 1.0)
 * </pre>
 *
 * Bei einem Treffer wird mindestens 1 Schaden verursacht.
 */
public class DamageCalculator {

	private final Random rng;

	public DamageCalculator() {
		this(new Random());
	}

	public DamageCalculator(Random rng) {
		this.rng = rng;
	}

	/** Wahrscheinlichkeit eines Volltreffers (1 zu CRIT_CHANCE). */
	private static final int CRIT_CHANCE = 16;
	private static final double CRIT_MULTIPLIER = 1.5;

	/**
	 * Globaler Schadensfaktor, mit dem sich das Tempo der Kämpfe einstellen lässt:
	 * höher = kürzere Kämpfe. 1.0 entspräche der reinen Pokémon-Formel.
	 */
	private static final double DAMAGE_MULTIPLIER = 1.4;

	public DamageResult calculate(Battler attacker, Battler defender, Move move) {
		double effectiveness = TypeChart.effectiveness(move.getType(), defender.getType());
		boolean critical = rng.nextInt(CRIT_CHANCE) == 0;
		double critFactor = critical ? CRIT_MULTIPLIER : 1.0;
		double base = (2.0 * attacker.getLevel() / 5 + 2)
				* move.getPower()
				* ((double) attacker.getEffectiveAttack() / defender.getEffectiveDefense())
				/ 50 + 2;
		double random = 0.85 + rng.nextDouble() * 0.15;
		int damage = Math.max(1,
				(int) (base * effectiveness * random * critFactor * DAMAGE_MULTIPLIER));
		return new DamageResult(damage, effectiveness, critical);
	}
}
