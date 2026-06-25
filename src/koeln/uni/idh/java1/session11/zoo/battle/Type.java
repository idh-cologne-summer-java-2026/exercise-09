package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Die Element-Typen der Tiere. Die Reihenfolge der Konstanten ist wichtig:
 * sie muss zur Reihenfolge in {@link TypeChart} passen, da dort über
 * {@link #ordinal()} indiziert wird.
 */
public enum Type {
	NORMAL("Normal"),
	ERDE("Erde"),
	WASSER("Wasser"),
	FEUER("Feuer"),
	PFLANZE("Pflanze"),
	LUFT("Luft");

	private final String displayName;

	Type(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
