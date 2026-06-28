package koeln.uni.idh.java1.session11.zoo.ui;

public class AsciiImage {

	/**
	 * The image itself, as ASCII-art
	 */
	private final char [][] image;
	 
	 private final char background = '.';
	
	 
	/**
	 * Creates a new empty image with the given width, which is expressed as the
	 * number of columns in each line.
	 * 
	 * @param width An integer value that represents the width of the image
	 */

	public AsciiImage(int width, int height) {
	        this.image = new char[height][width];
	
	        for (int y = 0; y < image.length; y++) {
	            for (int x = 0; x < image[y].length; x++) {
	                image[y][x] = background;
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
        setPixel(x, y, '#');
    }

    public void dot(int x, int y, Drawable drawable) {
        if (drawable != null) {
            setPixel(x, y, drawable.getSymbol());
        }
    }

    public void setPixel(int x, int y, char symbol) {
        if (isInside(x, y)) {
            image[y][x] = symbol;
        }
    }

	public boolean isInside(int x, int y) {
	        return y >= 0 && y < image.length
	                && x >= 0 && x < image[0].length;
	    }



	/**
	 * Generate the image as a String. Used for printing it to the user.
	 */

	@Override
	    public String toString() {
	        String result = "";
	
	        for (int y = 0; y < image.length; y++) {
	            for (int x = 0; x < image[y].length; x++) {
	                result += image[y][x] + " ";
	            }
	            result += "\n";
	        }
	
	        return result;
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
