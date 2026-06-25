package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Typ-Effektivitäts-Tabelle (Angreifer-Typ → Verteidiger-Typ → Multiplikator).
 * 2.0 = sehr effektiv, 0.5 = nicht sehr effektiv, 1.0 = normal.
 *
 * Die Indizes folgen der Reihenfolge der Konstanten in {@link Type}:
 * NORMAL, ERDE, WASSER, FEUER, PFLANZE, LUFT.
 */
public final class TypeChart {

	// Zeile = Angreifer, Spalte = Verteidiger
	private static final double[][] CHART = {
		// NORM  ERDE  WASS  FEUR  PFLA  LUFT
		{ 1.0,  1.0,  1.0,  1.0,  1.0,  1.0 }, // NORMAL
		{ 1.0,  1.0,  0.5,  2.0,  0.5,  0.5 }, // ERDE
		{ 1.0,  2.0,  0.5,  2.0,  0.5,  1.0 }, // WASSER
		{ 1.0,  1.0,  0.5,  0.5,  2.0,  1.0 }, // FEUER
		{ 1.0,  2.0,  2.0,  0.5,  0.5,  0.5 }, // PFLANZE
		{ 1.0,  2.0,  1.0,  1.0,  2.0,  1.0 }, // LUFT
	};

	private TypeChart() {
	}

	public static double effectiveness(Type attacker, Type defender) {
		return CHART[attacker.ordinal()][defender.ordinal()];
	}
}
