package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Erzeugt frische Tier-Instanzen – sowohl für die Spieler-Auswahl als auch für
 * zufällige wilde Tiere in der Overworld. Jede Tierart bringt ihre Werte und
 * Attacken im eigenen Konstruktor mit.
 */
public final class AnimalFactory {

	/** Alle wählbaren/spawnbaren Tierarten in fester Reihenfolge. */
	private static final Supplier<WalkingMammal>[] SPECIES = createSpecies();

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

	public static int speciesCount() {
		return SPECIES.length;
	}

	/** Erzeugt das Tier mit dem gegebenen Auswahl-Index (0-basiert). */
	public static WalkingMammal create(int index) {
		return SPECIES[index].get();
	}

	/** Eine frische Reihe je eines Exemplars aller Arten (für das Auswahlmenü). */
	public static WalkingMammal[] oneOfEach() {
		WalkingMammal[] all = new WalkingMammal[SPECIES.length];
		for (int i = 0; i < SPECIES.length; i++) {
			all[i] = SPECIES[i].get();
		}
		return all;
	}

	/** Ein zufälliges wildes Tier. */
	public static WalkingMammal randomWild(Random rng) {
		return SPECIES[rng.nextInt(SPECIES.length)].get();
	}
}
