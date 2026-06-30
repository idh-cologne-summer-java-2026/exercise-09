package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Season;
import koeln.uni.idh.java1.session11.zoo.areas.Habitat;
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

	private Habitat habitat;
	private int hunger = 50;
	private int thirst = 50;

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

	public Habitat getHabitat() {
		return habitat;
	}

	public void setHabitat(Habitat habitat) {
		this.habitat = habitat;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = Math.max(0, Math.min(100, hunger));
	}

	public int getThirst() {
		return thirst;
	}

	public void setThirst(int thirst) {
		this.thirst = Math.max(0, Math.min(100, thirst));
	}

	public void eat() {
		setHunger(getHunger() - 20);
		setThirst(getThirst() - 5);
		String displayName = (name != null && !name.isEmpty()) ? name : getClass().getSimpleName();
		System.out.println(displayName + " has eaten and feels better.");
	}

	public void drink() {
		setThirst(getThirst() - 20);
		String displayName = (name != null && !name.isEmpty()) ? name : getClass().getSimpleName();
		System.out.println(displayName + " has drunk water.");
	}

	public boolean canReproduceInSeason(Season season) {
		return season == Season.SPRING;
	}

	public WalkingMammal reproduce(Season season) {
		if (!canReproduceInSeason(season)) {
			System.out.println(getDisplayName() + " does not reproduce in " + season + ".");
			return null;
		}

		WalkingMammal offspring = createOffspring();
		System.out.println(getDisplayName() + " reproduced in " + season + " and welcomed a baby " + offspring.getClass().getSimpleName() + ".");
		return offspring;
	}

	protected WalkingMammal createOffspring() {
		try {
			return getClass().getDeclaredConstructor(String.class).newInstance("Baby " + getDisplayName());
		} catch (Exception e) {
			try {
				return getClass().getDeclaredConstructor().newInstance();
			} catch (Exception ignored) {
				return this;
			}
		}
	}

	public String getDisplayName() {
		return (name != null && !name.isEmpty()) ? name : getClass().getSimpleName();
	}

}
