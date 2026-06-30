package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class FeedingStation implements Drawable {
	private final int x;
	private final int y;
	private int portions;

	public FeedingStation(int x, int y, int portions) {
		this.x = x;
		this.y = y;
		this.portions = portions;
	}

	public boolean takePortion() {
		if (portions <= 0) {
			return false;
		}
		portions--;
		return true;
	}

	public void refill(int portions) {
		this.portions += portions;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPortions() {
		return portions;
	}

	@Override
	public char getSymbol() {
		return 'F';
	}
}
