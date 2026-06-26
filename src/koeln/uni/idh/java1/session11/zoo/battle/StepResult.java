package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Beschreibt, was in einem einzelnen Kampfschritt passiert ist. Die
 * Darstellung nutzt das, um passende Animationen abzuspielen (z. B. ein
 * Projektil vom Angreifer zum Verteidiger in der Farbe des Attacken-Typs).
 */
public class StepResult {

	public enum Kind {
		/** Eine Attacke wurde eingesetzt. */
		MOVE,
		/** Ein Statuseffekt (z. B. Gift) hat am Rundenende gewirkt. */
		STATUS,
		/** Nichts Sichtbares (z. B. ein bereits besiegtes Tier kommt nicht dran). */
		NONE
	}

	private final Kind kind;
	private final Battler attacker;
	private final Battler defender;
	private final Move move;

	private StepResult(Kind kind, Battler attacker, Battler defender, Move move) {
		this.kind = kind;
		this.attacker = attacker;
		this.defender = defender;
		this.move = move;
	}

	public static StepResult move(Battler attacker, Battler defender, Move move) {
		return new StepResult(Kind.MOVE, attacker, defender, move);
	}

	public static StepResult status(Battler target) {
		return new StepResult(Kind.STATUS, null, target, null);
	}

	public static StepResult none() {
		return new StepResult(Kind.NONE, null, null, null);
	}

	public Kind getKind() {
		return kind;
	}

	public Battler getAttacker() {
		return attacker;
	}

	public Battler getDefender() {
		return defender;
	}

	public Move getMove() {
		return move;
	}
}
