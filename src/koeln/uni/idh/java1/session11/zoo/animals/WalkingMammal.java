package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.Weather;
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
	protected String name;
	private int hunger = 25;
	private int thirst = 25;
	private int energy = 80;
	private int stress = 10;
	private int health = 100;

	/**
	 * the current x position of the mammal
	 */
	protected int x = 1;

	/**
	 * The current y position of the mammal
	 */
	protected int y = 1;
	
	/**
	 * How far the animal walks in a single step
	 */
	protected int stepsize = 1;

	/**
	 * The current view direction of the horse, on a 360° wheel (compass rose).
	 * 0 => top, 90 => right, 180 => bottom, 270 => left
	 */
	protected int direction = 0;

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

	public void liveOneHour(Weather weather, Random random, int width, int height) {
		hunger = limit(hunger + 8 + weather.getHungerEffect());
		thirst = limit(thirst + 9 + weather.getThirstEffect());
		stress = limit(stress + weather.getStressEffect());
		energy = limit(energy - 4);

		if (hunger > 75 || thirst > 75 || stress > 70) {
			health = limit(health - 3);
		}
		if (hunger < 45 && thirst < 45 && stress < 45) {
			health = limit(health + 2);
		}

		decideNextAction(weather, random);
		moveInside(width, height);
	}

	private void decideNextAction(Weather weather, Random random) {
		if (weather == Weather.HEATWAVE && thirst > 55) {
			direction = 90;
		} else if (energy < 30) {
			energy = limit(energy + 12);
			stress = limit(stress - 4);
		} else {
			direction = random.nextInt(4) * 90;
			walkQuietly();
		}
	}

	private void walkQuietly() {
		switch (direction) {
		case 0:
			y = y - stepsize;
			break;
		case 90:
			x = x + stepsize;
			break;
		case 180:
			y = y + stepsize;
			break;
		case 270:
			x = x - stepsize;
			break;
		default:
			break;
		}
	}

	public void feed() {
		hunger = limit(hunger - 35);
		energy = limit(energy + 8);
	}

	public void drink() {
		thirst = limit(thirst - 40);
		health = limit(health + 2);
	}

	public void pet() {
		stress = limit(stress - 18);
		energy = limit(energy - 3);
	}

	public boolean needsVet() {
		return health < 55;
	}

	public void treatByVet() {
		health = limit(health + 30);
		stress = limit(stress + 8);
	}

	public int welfareScore() {
		return (health + energy + (100 - hunger) + (100 - thirst) + (100 - stress)) / 5;
	}

	public String statusLine() {
		return getDisplayName() + " [" + getSymbol() + "] Hunger:" + hunger + " Durst:" + thirst + " Energie:"
				+ energy + " Stress:" + stress + " Gesundheit:" + health;
	}

	public String getDisplayName() {
		if (name == null || name.isBlank()) {
			return getClass().getSimpleName();
		}
		return name;
	}

	private void moveInside(int width, int height) {
		x = Math.max(0, Math.min(width - 1, x));
		y = Math.max(0, Math.min(height - 1, y));
	}

	private int limit(int value) {
		return Math.max(0, Math.min(100, value));
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

}
