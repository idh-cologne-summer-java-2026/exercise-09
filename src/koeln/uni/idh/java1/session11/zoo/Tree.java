package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class Tree implements Drawable {
	private int x;
	private int y;

	public Tree(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public char getSymbol() {
		return 'T';
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
