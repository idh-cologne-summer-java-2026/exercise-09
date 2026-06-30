package koeln.uni.idh.java1.session11.zoo.ui;

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

	/**
	 * Paint anything that is {@link Drawable} at position x and y. By depending
	 * only on the interface (and not on a concrete class like WalkingMammal), we
	 * can draw animals AND trees AND anything we invent later with this one method.
	 *
	 * @param x The horizontal position
	 * @param y The vertical position
	 * @param d The object to draw; its getSymbol() decides the character
	 */
	public void dot(int x, int y, Drawable d) {
		image[y][x] = d.getSymbol();
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
