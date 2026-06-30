package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.DayNightAware;
import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public abstract class WalkingMammal implements Drawable, DayNightAware {

	String name;
	int x = 1;
	int y = 1;
	int stepsize = 1;
	int direction = 0;

	/** Ob das Tier gerade schläft */
	protected boolean asleep = false;

	/**
	 * Wird von der Clock aufgerufen, wenn sich die Uhrzeit ändert.
	 * Tiere schlafen standardmäßig nachts (22-5 Uhr) und sind tagsüber wach.
	 */
	@Override
	public void onTimeChange(int hour) {
		boolean isNight = hour >= 22 || hour < 6;
		if (isNight && !asleep) {
			asleep = true;
			System.out.println(getClass().getSimpleName() + " legt sich schlafen.");
		} else if (!isNight && asleep) {
			asleep = false;
			System.out.println(getClass().getSimpleName() + " wacht auf.");
		}
	}

	public boolean isAsleep() {
		return asleep;
	}

	public void walk() {
		if (asleep) {
			System.out.println("Tier schläft und bewegt sich nicht.");
			return;
		}

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

	public void turn(int turnDirection) {
		if (asleep) {
			System.out.println("Tier schläft und dreht sich nicht.");
			return;
		}
		this.direction = (int) (this.direction + (Math.signum(turnDirection) * 90) % 360);
		System.out.println("Animal " + name + " has turned and is now looking towards " + direction + ".");
	}

	public abstract char getSymbol();

	/**
	 * Symbol abhängig vom Schlafzustand: schlafend = Kleinbuchstabe.
	 */
	public char getDisplaySymbol() {
		return asleep ? Character.toLowerCase(getSymbol()) : getSymbol();
	}

	public int getX() { return x; }
	public void setX(int x) { this.x = x; }
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
}
