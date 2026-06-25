package koeln.uni.idh.java1.session11.zoo.battle;

import java.util.List;

/**
 * Alles, was kämpfen kann. Die Brücke zwischen dem Tier-Modell
 * ({@code WalkingMammal}) und der Kampf-Engine ({@link Battle}).
 */
public interface Battler {
	String getName();

	Type getType();

	Stats getStats();

	List<Move> getMoves();

	int getLevel();

	int getCurrentHp();

	void setCurrentHp(int hp);

	int getMaxHp();

	Status getStatus();

	void setStatus(Status status);

	boolean isFainted();

	/** Angriff inklusive aktueller Stufen-Modifikatoren. */
	int getEffectiveAttack();

	/** Verteidigung inklusive aktueller Stufen-Modifikatoren. */
	int getEffectiveDefense();

	void lowerAttack();

	void lowerDefense();

	/** Das Symbol des Tieres (kommt aus Drawable). */
	char getSymbol();
}
