package koeln.uni.idh.java1.session11.zoo.ui;

public class AsciiImage {
	private char[][] image;
	private char white = '.';
	private char black = '#';

	public AsciiImage(int width, int height) {
		this.image = new char[height][width];
		clear();
	}

	public void clear() {
		for (int h = 0; h < image.length; h++) {
			for (int w = 0; w < image[0].length; w++) {
				image[h][w] = white;
			}
		}
	}

	public void dot(int x, int y) {
		dot(x, y, black);
	}

	public void dot(int x, int y, Drawable drawable) {
		dot(x, y, drawable.getSymbol());
	}

	public void dot(int x, int y, char symbol) {
		if (isInside(x, y)) {
			image[y][x] = symbol;
		}
	}

	public void rectangle(int x, int y, int width, int height, char border) {
		for (int currentX = x; currentX < x + width; currentX++) {
			dot(currentX, y, border);
			dot(currentX, y + height - 1, border);
		}
		for (int currentY = y; currentY < y + height; currentY++) {
			dot(x, currentY, border);
			dot(x + width - 1, currentY, border);
		}
	}

	private boolean isInside(int x, int y) {
		return y >= 0 && y < image.length && x >= 0 && x < image[0].length;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int h = 0; h < image.length; h++) {
			for (int w = 0; w < image[h].length; w++) {
				builder.append(image[h][w]);
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public int width() {
		return image[0].length;
	}

	public int height() {
		return image.length;
	}
}
