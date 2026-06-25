package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public abstract class WalkingMammal implements Drawable {

	String name;

	int x = 1;
	int y = 1;

	int stepsize = 1;

	int direction = 0;

	// 🆕 HUNGER SYSTEM
	protected int hunger = 0;
	protected int maxHunger = 100;

	public void walk() {

		// Hunger steigt bei jeder Bewegung
		increaseHunger(1);

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

		System.out.println(getClass().getSimpleName() + " moved. Hunger: " + hunger);
	}

	public void turn(int turnDirection) {
		this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90)) % 360;
		System.out.println("Animal turned. Direction: " + direction);
	}

	// 🆕 HUNGER LOGIK
	public void increaseHunger(int amount) {
		hunger += amount;
		if (hunger > maxHunger) {
			hunger = maxHunger;
		}
	}

	public void feed(int amount) {
		hunger -= amount;
		if (hunger < 0) {
			hunger = 0;
		}
	}

	public boolean isStarving() {
		return hunger >= maxHunger;
	}

	public int getHunger() {
		return hunger;
	}

	public abstract char getSymbol();

	// optional (für Debug später nützlich)
	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
}