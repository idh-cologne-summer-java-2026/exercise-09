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

	public int calculate(Battler attacker, Battler defender, Move move) {
		double effectiveness = TypeChart.effectiveness(move.getType(), defender.getType());
		double base = (2.0 * attacker.getLevel() / 5 + 2)
				* move.getPower()
				* ((double) attacker.getEffectiveAttack() / defender.getEffectiveDefense())
				/ 50 + 2;
		double random = 0.85 + rng.nextDouble() * 0.15;
		int damage = (int) (base * effectiveness * random);
		return Math.max(1, damage);
	}
}
