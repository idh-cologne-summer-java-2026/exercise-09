package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

/**
 * This class represents walking mammals. Walking mammals have a position (x and
 * y coordinates), a face direction, a step size (i.e., the number of units they
 * go when making a single step) and a name.
 * 
 * Walking mammals can turn and walk, and they know how they should be
 * represented in a zoo visualization.
 * 
 * @author nils.reiter@uni-koeln.de
 *
 */
public abstract class WalkingMammal implements Drawable {
	String name;

	/**
	 * the current x position of the mammal
	 */
	int x = 1;

	/**
	 * The current y position of the mammal
	 */
	int y = 1;
	
	/**
	 * How far the animal walks in a single step
	 */
	int stepsize = 1;

	/**
	 * The current view direction of the horse, on a 360° wheel (compass rose).
	 * 0 => top, 90 => right, 180 => bottom, 270 => left
	 */
	int direction = 0;

	/**
	 * The animal walks a single step in the direction in which it is looking.
	 */
	public void walk() {

		switch (direction) {
		case 0:
			this.y = this.y - stepsize;
			break;
		case 180:
			this.x = this.x - stepsize;
			break;
		case 270:
			this.y = this.y + stepsize;
			break;
		case 90:
			this.x = this.x + stepsize;
		}
		System.out.println("Animal has moved.");
	}

	/**
	 * This method calculates the new direction by taking the sign of the argument
	 * with Math.signum(), multiplying that with 90 and add it to the old direction
	 * value. To avoid that we produce direction values > 360, we take the modulo of
	 * 360.
	 * 
	 * @param turnDirection If the argument is a negative number, the animal turns
	 *                      to the left. If it's positive number, it turns to the
	 *                      right.
	 */
	public void turn(int turnDirection) {
		this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90) % 360);
		System.out.println("Animal " + name + " has turned and is now looking towards " + direction + ".");

	}

	/**
	 * How to represent the animal on the zoo field. Note that this is not an
	 * individual animal, but one that symbolizes the class of the animal.
	 * 
	 * @return A character used to represent the animal
	 */
	public abstract char getSymbol();

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return name != null ? name : this.getClass().getSimpleName();
	}

	private static final int MAX_NEED = 100;
	private static final int MIN_NEED = 0;
	private static final int NEED_STEP = 25;

	private int hunger = 50;
	private int thirst = 50;
	private int tiredness = 50;

	public String getNeedStatus() {
		return "Hunger: " + hunger + "/100  Durst: " + thirst + "/100  Müdigkeit: " + tiredness + "/100";
	}

	public void feed() {
		hunger = clampNeed(hunger + NEED_STEP);
		System.out.println(getDisplayName() + " wurde gefüttert.");
	}

	public void drink() {
		thirst = clampNeed(thirst + NEED_STEP);
		System.out.println(getDisplayName() + " hat getrunken.");
	}

	public void rest() {
		tiredness = clampNeed(tiredness + NEED_STEP);
		System.out.println(getDisplayName() + " hat sich ausgeruht.");
	}

	private int clampNeed(int value) {
		if (value < MIN_NEED)
			return MIN_NEED;
		if (value > MAX_NEED)
			return MAX_NEED;
		return value;
	}

	public String getAsciiArt() {
		return "   " + getDisplayName() + "\n"
			+ "    " + getSymbol() + "  \n"
			+ "   /|\\ \n"
			+ "  / | \\ \n"
			+ "   / \\ \n";
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
