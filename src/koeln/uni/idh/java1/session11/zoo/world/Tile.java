package koeln.uni.idh.java1.session11.zoo.world;

/**
 * Ein Geländetyp im Gehege. Bäume und Wände blockieren die Bewegung, Gras und
 * Wasser sind begehbar.
 */
public enum Tile {
	GRAS('.'),
	WASSER('~'),
	BAUM('T'),
	WAND('#');

	private final char symbol;

	Tile(char symbol) {
		this.symbol = symbol;
	}

	public char getSymbol() {
		return symbol;
	}

	public boolean isBlocking() {
		return this == BAUM || this == WAND;
	}
}
