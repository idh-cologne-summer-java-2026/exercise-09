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

	// Neue Attribute
	private int hunger = 0;
	private int thirst = 0;

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
			break;
		}

		// Hunger und Durst steigen bei jeder Bewegung
		hunger++;
		thirst++;

		System.out.println("Animal has moved.");
	}

	/**
	 * Turns the animal.
	 */
	public void turn(int turnDirection) {
		this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90) % 360);
		System.out.println("Animal " + name + " has turned and is now looking towards " + direction + ".");
	}

	/**
	 * Symbol of the animal.
	 */
	public abstract char getSymbol();

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	// Neue Methoden

	public void feed() {
		hunger = 0;
		System.out.println("The animal has been fed.");
	}

	public void drink() {
		thirst = 0;
		System.out.println("The animal has drunk water.");
	}

	public int getHunger() {
		return hunger;
	}

	public int getThirst() {
		return thirst;
	}
}