package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Anhaltende Statuseffekte eines Kämpfers.
 * VERGIFTET zieht am Ende jeder Runde einen kleinen Teil der HP ab.
 */
public enum Status {
	KEINER("ok"),
	VERGIFTET("vergiftet");

	private final String displayName;

	Status(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
