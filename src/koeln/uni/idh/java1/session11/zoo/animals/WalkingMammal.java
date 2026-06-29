package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

/**
 * Base class for all walking mammals in the zoo simulation.
 *
 * The class now models a few state values that are useful for a small
 * simulation: position, direction, hunger, thirst and friendliness.
 */
public abstract class WalkingMammal implements Drawable {
	private String name;
	private int x = 1;
	private int y = 1;
	private int stepsize = 1;

	/**
	 * 0 => top, 90 => right, 180 => left, 270 => bottom
	 */
	private int direction = 0;

	private int hunger = 30;
	private int thirst = 30;
	private int friendliness = 50;

	protected WalkingMammal(String name) {
		this.name = name;
	}

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
		default:
			break;
		}
		hunger = limit(hunger + 6);
		thirst = limit(thirst + 8);
	}

	public void turn(int turnDirection) {
		this.direction = Math.floorMod((int) (this.direction + Math.signum(turnDirection) * 90), 360);
	}

	public void feed() {
		hunger = limit(hunger - 35);
		friendliness = limit(friendliness + 5);
	}

	public void giveWater() {
		thirst = limit(thirst - 40);
		friendliness = limit(friendliness + 2);
	}

	public void pet() {
		if (hunger > 75 || thirst > 75) {
			friendliness = limit(friendliness - 10);
		} else {
			friendliness = limit(friendliness + 12);
		}
	}

	public void heatWave() {
		thirst = limit(thirst + 25);
		hunger = limit(hunger + 5);
	}

	public boolean isHungry() {
		return hunger >= 60;
	}

	public boolean isThirsty() {
		return thirst >= 60;
	}

	public boolean isHappy() {
		return hunger < 60 && thirst < 60 && friendliness >= 50;
	}

	public String status() {
		return getName() + " (" + getSymbol() + ") hunger=" + hunger + ", thirst=" + thirst
				+ ", friendliness=" + friendliness + ", position=(" + x + "," + y + ")";
	}

	private int limit(int value) {
		return Math.max(0, Math.min(100, value));
	}

	public abstract char getSymbol();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public int getStepsize() {
		return stepsize;
	}

	public void setStepsize(int stepsize) {
		this.stepsize = Math.max(1, stepsize);
	}
}
