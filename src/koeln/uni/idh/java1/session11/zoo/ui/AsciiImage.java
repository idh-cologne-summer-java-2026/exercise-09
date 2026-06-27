package koeln.uni.idh.java1.session11.zoo.ui;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class AsciiImage {

	/**
	 * The image itself, as ASCII-art
	 */
	private char[][] image;

	/**
	 * Representation of a white pixel
	 */
	private char white = '.';

	/**
	 * Representation of a black pixel
	 */
	private char black = '#';

	/**
	 * Creates a new empty image with the given width, which is expressed as the
	 * number of columns in each line.
	 * 
	 * @param width An integer value that represents the width of the image
	 */
	public AsciiImage(int width, int height) {

		// Initially, the image is empty
		this.image = new char[height][width];
		for (int h = 0; h < image.length; h++) {
			for (int w = 0; w < image[0].length; w++) {
				image[h][w] = white;
			}
		}
	}

	/**
	 * Paint a single black dot at position x and y
	 * 
	 * @param x The horizontal position of the pixel
	 * @param y The vertical position of the pixel
	 */
	public void dot(int x, int y) {
		image[y][x] = black;
	}

	public void dot(int x, int y, WalkingMammal wm) {
		int footprintSize = Math.max(1, wm.getFootprintSize());
		int sideLength = (int) Math.ceil(Math.sqrt(footprintSize));
		int placedCells = 0;

		for (int row = 0; row < sideLength && placedCells < footprintSize; row++) {
			for (int column = 0; column < sideLength && placedCells < footprintSize; column++) {
				ensureCapacity(x + column, y + row);
				image[y + row][x + column] = wm.getSymbol();
				placedCells++;
			}
		}
	}

	public void dot(int x, int y, Drawable drawable) {
		ensureCapacity(x, y);
		image[y][x] = drawable.getSymbol();
	}

	public void drawEnclosure(int x, int y, int width, int height) {
		if (width <= 1 || height <= 1) {
			throw new IllegalArgumentException("Enclosure size must be at least 2x2.");
		}

		ensureCapacity(x + width - 1, y + height - 1);
		for (int row = y; row < y + height; row++) {
			for (int column = x; column < x + width; column++) {
				if (row == y || row == y + height - 1 || column == x || column == x + width - 1) {
					image[row][column] = black;
				}
			}
		}
	}

	private void ensureCapacity(int x, int y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException("Coordinates must be non-negative.");
		}

		if (x < image[0].length && y < image.length) {
			return;
		}

		int newWidth = Math.max(image[0].length, x + 1);
		int newHeight = Math.max(image.length, y + 1);
		char[][] expanded = new char[newHeight][newWidth];

		for (int row = 0; row < image.length; row++) {
			for (int column = 0; column < image[row].length; column++) {
				expanded[row][column] = image[row][column];
			}
		}

		for (int row = 0; row < expanded.length; row++) {
			for (int column = 0; column < expanded[row].length; column++) {
				if (row >= image.length || column >= image[row].length) {
					expanded[row][column] = white;
				}
			}
		}

		image = expanded;
	}


	/**
	 * Generate the image as a String. Used for printing it to the user.
	 */
	public String toString() {
		String r = "";
		for (int h = 0; h < image.length; h++) {
			for (int w = 0; w < image[h].length; w++) {
				r += image[h][w];
			}
			r += "\n";
		}
		return r;
	}

	/**
	 * Returns the width of the image (i.e., the length of the first row)
	 * @return
	 */
	public int width() {
		return image[0].length;
	}
	
	/** 
	 * Returns the height of the image (i.e., number of rows)
	 */
	public int height() {
		return image.length;
	}
}
