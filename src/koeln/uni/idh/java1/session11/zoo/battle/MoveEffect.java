package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Optionaler Zusatzeffekt einer Attacke, der nach dem Schaden angewendet wird.
 */
public enum MoveEffect {
	/** Kein Zusatzeffekt. */
	KEINER,
	/** Vergiftet das Ziel (Status {@link Status#VERGIFTET}). */
	VERGIFTEN,
	/** Senkt den Angriffswert des Ziels um eine Stufe. */
	ANGRIFF_SENKEN,
	/** Senkt den Verteidigungswert des Ziels um eine Stufe. */
	VERTEIDIGUNG_SENKEN
}
