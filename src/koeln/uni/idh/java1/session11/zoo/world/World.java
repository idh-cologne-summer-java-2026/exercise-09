package koeln.uni.idh.java1.session11.zoo.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

/**
 * Das Spielfeld: ein 2D-Gitter aus {@link Tile}s plus die wilden Tiere und das
 * Spieler-Tier. Die Welt ist die einzige Instanz, die Positionen verändert –
 * so liegt die Grenz- und Hindernisprüfung an einer Stelle (behebt den
 * fehlenden Grenz-Check des alten {@code walk()}).
 */
public class World {

	private final int width;
	private final int height;
	private final Tile[][] grid;
	private final List<WalkingMammal> wildAnimals = new ArrayList<>();
	private final Random rng;

	private WalkingMammal player;

	// Position des Erzfeindes Prof. Nils (fester Ort). -1 = nicht (mehr) da.
	private int bossX = -1;
	private int bossY = -1;

	public World(int width, int height, Random rng) {
		this.width = width;
		this.height = height;
		this.rng = rng;
		this.grid = new Tile[height][width];
		generateTerrain();
	}

	private void generateTerrain() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boolean border = x == 0 || y == 0 || x == width - 1 || y == height - 1;
				grid[y][x] = border ? Tile.WAND : Tile.GRAS;
			}
		}
		// Ein kleiner Wasser-Teich
		scatterPatch(Tile.WASSER, width / 6, width / 6);
		// Vereinzelte Bäume
		int trees = (width * height) / 18;
		for (int i = 0; i < trees; i++) {
			int x = 1 + rng.nextInt(width - 2);
			int y = 1 + rng.nextInt(height - 2);
			grid[y][x] = Tile.BAUM;
		}
	}

	private void scatterPatch(Tile tile, int w, int h) {
		int startX = 1 + rng.nextInt(Math.max(1, width - w - 2));
		int startY = 1 + rng.nextInt(Math.max(1, height - h - 2));
		for (int y = startY; y < startY + h && y < height - 1; y++) {
			for (int x = startX; x < startX + w && x < width - 1; x++) {
				grid[y][x] = tile;
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile getTile(int x, int y) {
		return grid[y][x];
	}

	public WalkingMammal getPlayer() {
		return player;
	}

	public List<WalkingMammal> getWildAnimals() {
		return wildAnimals;
	}

	public void setPlayer(WalkingMammal player, int x, int y) {
		this.player = player;
		player.setX(x);
		player.setY(y);
	}

	public void addWildAnimal(WalkingMammal animal, int x, int y) {
		animal.setX(x);
		animal.setY(y);
		wildAnimals.add(animal);
	}

	public void removeWildAnimal(WalkingMammal animal) {
		wildAnimals.remove(animal);
	}

	/**
	 * Stellt Prof. Nils an einen festen Ort und macht das Feld sowie seine
	 * direkten Nachbarfelder begehbar, damit man ihn erreichen kann.
	 */
	public void placeBoss(int x, int y) {
		this.bossX = x;
		this.bossY = y;
		grid[y][x] = Tile.GRAS;
		clearAround(x, y);
	}

	private void clearAround(int x, int y) {
		int[][] offsets = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };
		for (int[] o : offsets) {
			int nx = x + o[0];
			int ny = y + o[1];
			if (nx > 0 && ny > 0 && nx < width - 1 && ny < height - 1) {
				grid[ny][nx] = Tile.GRAS;
			}
		}
	}

	public boolean isBossPresent() {
		return bossX >= 0;
	}

	public boolean isBossAt(int x, int y) {
		return isBossPresent() && bossX == x && bossY == y;
	}

	public int getBossX() {
		return bossX;
	}

	public int getBossY() {
		return bossY;
	}

	/** Entfernt Prof. Nils aus der Welt (nachdem er besiegt wurde). */
	public void removeBoss() {
		this.bossX = -1;
		this.bossY = -1;
	}

	/** Liegt (x,y) im Feld und ist das Zielfeld begehbar? */
	public boolean isWalkable(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		}
		return !grid[y][x].isBlocking();
	}

	private boolean isOccupiedByWild(int x, int y) {
		for (WalkingMammal a : wildAnimals) {
			if (a.getX() == x && a.getY() == y) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Bewegt den Spieler in die angegebene Blickrichtung, falls das Zielfeld
	 * begehbar ist. Das Betreten eines Feldes mit einem wilden Tier ist
	 * erlaubt – das löst anschließend über {@link #encounterAt} eine Begegnung
	 * aus.
	 *
	 * @return true, wenn sich der Spieler bewegt hat
	 */
	public boolean movePlayer(int direction) {
		player.setDirection(direction);
		int[] next = player.peekStep();
		if (!isWalkable(next[0], next[1])) {
			return false;
		}
		player.setX(next[0]);
		player.setY(next[1]);
		return true;
	}

	/** Liefert das wilde Tier auf dem Spielerfeld oder null. */
	public WalkingMammal encounterAt(int x, int y) {
		for (WalkingMammal a : wildAnimals) {
			if (a.getX() == x && a.getY() == y) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Bewegt jedes wilde Tier einen zufälligen Schritt. Tiere bleiben stehen,
	 * wenn das Zielfeld blockiert, belegt oder das Spielerfeld ist (Begegnungen
	 * gehen vom Spieler aus).
	 */
	public void tickWildAnimals() {
		for (WalkingMammal a : wildAnimals) {
			if (rng.nextInt(100) < 30) {
				continue; // manchmal stehen bleiben
			}
			int direction = rng.nextInt(4) * 90;
			a.setDirection(direction);
			int[] next = a.peekStep();
			int nx = next[0];
			int ny = next[1];
			if (!isWalkable(nx, ny)) {
				continue;
			}
			if (isOccupiedByWild(nx, ny)) {
				continue;
			}
			if (player != null && player.getX() == nx && player.getY() == ny) {
				continue;
			}
			if (isBossAt(nx, ny)) {
				continue;
			}
			a.setX(nx);
			a.setY(ny);
		}
	}

	/** Sucht eine zufällige begehbare, freie Position (für Spawns). */
	public int[] randomFreeTile() {
		for (int attempt = 0; attempt < 1000; attempt++) {
			int x = 1 + rng.nextInt(width - 2);
			int y = 1 + rng.nextInt(height - 2);
			if (grid[y][x].isBlocking() || isOccupiedByWild(x, y) || isBossAt(x, y)) {
				continue;
			}
			if (player != null && player.getX() == x && player.getY() == y) {
				continue;
			}
			return new int[] { x, y };
		}
		return new int[] { 1, 1 };
	}
}
