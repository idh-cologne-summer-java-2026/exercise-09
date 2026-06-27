package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;
import java.util.function.Supplier;

import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.battle.TypeChart;

/**
 * Erzeugt frische Tier-Instanzen – sowohl für die Spieler-Auswahl als auch für
 * zufällige wilde Tiere in der Overworld. Jede Tierart bringt ihre Werte und
 * Attacken im eigenen Konstruktor mit.
 */
public final class AnimalFactory {

	/** Alle spawnbaren Tierarten in fester Reihenfolge. */
	private static final Supplier<WalkingMammal>[] SPECIES = createSpecies();

	/**
	 * Die zu Beginn wählbaren Starter-Zookémon: das klassische Trio aus
	 * Pflanze (Flauschpaka), Feuer (Glutprankel) und Wasser (Schnappix). Die
	 * übrigen Arten trifft man nur als wilde Tiere.
	 */
	private static final int[] STARTER_INDICES = { 0, 3, 5 };

	@SuppressWarnings("unchecked")
	private static Supplier<WalkingMammal>[] createSpecies() {
		return new Supplier[] {
			(Supplier<WalkingMammal>) Alpaca::new,
			(Supplier<WalkingMammal>) Horse::new,
			(Supplier<WalkingMammal>) Elephant::new,
			(Supplier<WalkingMammal>) Lion::new,
			(Supplier<WalkingMammal>) Eagle::new,
			(Supplier<WalkingMammal>) Crocodile::new,
		};
	}

	private AnimalFactory() {
	}

	/** Die Anzahl der wählbaren Starter-Zookémon. */
	public static int starterCount() {
		return STARTER_INDICES.length;
	}

	/** Je ein frisches Exemplar der wählbaren Starter (für das Auswahlmenü). */
	public static WalkingMammal[] starters() {
		WalkingMammal[] all = new WalkingMammal[STARTER_INDICES.length];
		for (int i = 0; i < STARTER_INDICES.length; i++) {
			all[i] = SPECIES[STARTER_INDICES[i]].get();
		}
		return all;
	}

	/** Erzeugt den Starter mit dem gegebenen Auswahl-Index (0-basiert). */
	public static WalkingMammal createStarter(int starterIndex) {
		return SPECIES[STARTER_INDICES[starterIndex]].get();
	}

	/**
	 * Liefert das Starter-Zookémon, dessen Typ super-effektiv (2.0) gegen den
	 * gegebenen Typ ist – also genau das „Konter-Starter". Damit rüstet sich
	 * der Professor gezielt gegen den Starter des Spielers aus.
	 */
	public static WalkingMammal counterStarter(Type type) {
		for (int idx : STARTER_INDICES) {
			WalkingMammal candidate = SPECIES[idx].get();
			if (TypeChart.effectiveness(candidate.getType(), type) >= 2.0) {
				return candidate;
			}
		}
		// Sollte für die drei Starter-Typen nie eintreten.
		return SPECIES[STARTER_INDICES[0]].get();
	}

	/** Ein zufälliges wildes Tier. */
	public static WalkingMammal randomWild(Random rng) {
		return SPECIES[rng.nextInt(SPECIES.length)].get();
	}
}
