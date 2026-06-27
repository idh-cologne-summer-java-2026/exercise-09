package koeln.uni.idh.java1.session11.zoo.ui;

/**
 * Einfache Geräuschkulisse über den Terminal-Signalton ({@code \007}, die
 * „Glocke"). Pitch und Klang lassen sich damit nicht steuern – nur Anzahl und
 * Rhythmus der Töne. Das reicht aber, um Treffer, Level-Aufstiege, Siege und den
 * Auftritt des Bosses hörbar zu untermalen.
 *
 * Per {@link #toggle()} (Taste M in der Overworld) lässt sich der Ton stumm
 * schalten – praktisch, falls die Glocke im Terminal zu laut oder störend ist.
 */
public final class Sound {

	private static boolean enabled = true;

	private Sound() {
	}

	public static void setEnabled(boolean on) {
		enabled = on;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void toggle() {
		enabled = !enabled;
	}

	/** Ein einzelner Signalton. */
	private static void bell() {
		if (!enabled) {
			return;
		}
		System.out.print('\007');
		System.out.flush();
	}

	/** Mehrere Töne im Abstand von {@code gapMs} Millisekunden (kleine „Melodie"). */
	private static void bells(int count, long gapMs) {
		for (int i = 0; i < count; i++) {
			bell();
			if (i < count - 1) {
				sleep(gapMs);
			}
		}
	}

	/** Ein gewöhnlicher Treffer. */
	public static void hit() {
		bell();
	}

	/** Ein Tier geht zu Boden. */
	public static void faint() {
		bells(3, 90);
	}

	/** Level-Aufstieg. */
	public static void levelUp() {
		bells(3, 80);
	}

	/** Entwicklung eines Tieres. */
	public static void evolve() {
		bells(5, 70);
	}

	/** Sieg-Fanfare. */
	public static void victory() {
		bells(4, 120);
	}

	/** Menü-/Bestätigungston. */
	public static void select() {
		bell();
	}

	/** Dramatischer Auftritt bzw. Wutanfall des Bosses. */
	public static void bossRoar() {
		bells(6, 70);
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
