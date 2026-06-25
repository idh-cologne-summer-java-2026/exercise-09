package koeln.uni.idh.java1.session11.zoo.world;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

/**
 * Der Spieler steuert genau ein Tier durch die Overworld. Die Klasse hält
 * dieses Tier und übersetzt Bewegungswünsche in Himmelsrichtungen.
 */
public class Player {

	/** Blickrichtungen passend zur Konvention in WalkingMammal. */
	public static final int OBEN = 0;
	public static final int RECHTS = 90;
	public static final int LINKS = 180;
	public static final int UNTEN = 270;

	private final WalkingMammal animal;

	public Player(WalkingMammal animal) {
		this.animal = animal;
	}

	public WalkingMammal getAnimal() {
		return animal;
	}

	public int getX() {
		return animal.getX();
	}

	public int getY() {
		return animal.getY();
	}

	public char getSymbol() {
		return '@';
	}
}
