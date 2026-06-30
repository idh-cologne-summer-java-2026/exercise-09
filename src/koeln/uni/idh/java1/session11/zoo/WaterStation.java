package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class WaterStation implements Drawable {
	private final int x;
	private final int y;
	private int liters;

	public WaterStation(int x, int y, int liters) {
		this.x = x;
		this.y = y;
		this.liters = liters;
	}

	public boolean drink() {
		if (liters <= 0) {
			return false;
		}
		liters--;
		return true;
	}

	public void refill(int liters) {
		this.liters += liters;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getLiters() {
		return liters;
	}

	@Override
	public char getSymbol() {
		return 'W';
	}
}
