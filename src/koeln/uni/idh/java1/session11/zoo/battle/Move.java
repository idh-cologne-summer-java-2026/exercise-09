package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Eine Attacke. Sie hat einen Namen, einen Typ, eine Stärke (0 = reine
 * Status-Attacke ohne Schaden), eine Genauigkeit in Prozent und optional einen
 * Zusatzeffekt mit eigener Eintritts-Wahrscheinlichkeit.
 */
public class Move {
	private final String name;
	private final Type type;
	private final int power;
	private final int accuracy;
	private final MoveEffect effect;
	private final int effectChance;

	public Move(String name, Type type, int power, int accuracy, MoveEffect effect, int effectChance) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.accuracy = accuracy;
		this.effect = effect;
		this.effectChance = effectChance;
	}

	/** Bequemer Konstruktor für eine reine Schadens-Attacke ohne Effekt. */
	public Move(String name, Type type, int power, int accuracy) {
		this(name, type, power, accuracy, MoveEffect.KEINER, 0);
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public int getPower() {
		return power;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public MoveEffect getEffect() {
		return effect;
	}

	public int getEffectChance() {
		return effectChance;
	}
}
