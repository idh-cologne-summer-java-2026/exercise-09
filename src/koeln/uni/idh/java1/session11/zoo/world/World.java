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
	private final List<Npc> npcs = new ArrayList<>();
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
	 * Stellt Prof. Nils an einen festen Ort und baut eine kleine Festung um ihn:
	 * einen Mauerring mit einem einzelnen Tor an der Unterseite. Drinnen ist
	 * alles begehbar, sodass man durch das Tor zu ihm gelangt.
	 */
	public void placeBoss(int x, int y) {
		this.bossX = x;
		this.bossY = y;
		buildFortress(x, y);
	}

	private void buildFortress(int cx, int cy) {
		int r = 2;
		for (int yy = cy - r; yy <= cy + r; yy++) {
			for (int xx = cx - r; xx <= cx + r; xx++) {
				if (xx <= 0 || yy <= 0 || xx >= width - 1 || yy >= height - 1) {
					continue; // den äußeren Spielfeldrand nicht überschreiben
				}
				boolean ring = xx == cx - r || xx == cx + r || yy == cy - r || yy == cy + r;
				grid[yy][xx] = ring ? Tile.WAND : Tile.GRAS;
			}
		}
		// Tor an der Unterseite (Richtung Spielfeld) und das Feld von Nils frei.
		if (cy + r < height - 1) {
			grid[cy + r][cx] = Tile.GRAS;
		}
		grid[cy][cx] = Tile.GRAS;
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

	// ---------------- NPCs ----------------

	public List<Npc> getNpcs() {
		return npcs;
	}

	/** Liefert den NPC auf (x,y) oder null. */
	public Npc npcAt(int x, int y) {
		for (Npc n : npcs) {
			if (n.getX() == x && n.getY() == y) {
				return n;
			}
		}
		return null;
	}

	public boolean isNpcAt(int x, int y) {
		return npcAt(x, y) != null;
	}

	/**
	 * Stellt einen NPC an seinen Ort und gestaltet je nach Art eine kleine,
	 * unverwechselbare Umgebung um ihn: ein Baum-Hain für den Kommilitonen, einen
	 * Wasser-Teich für die K.I. So erkennt man schon von Weitem, dass dort etwas
	 * anderes als ein wildes Tier wartet.
	 */
	public void placeNpc(Npc npc) {
		npcs.add(npc);
		switch (npc.getKind()) {
		case KOMMILITONE:
			buildGrove(npc.getX(), npc.getY());
			break;
		case KI:
			buildLake(npc.getX(), npc.getY());
			break;
		default:
			break;
		}
	}

	/** Baum-Ring (Lernecke) mit einem Eingang von unten; der NPC steht frei. */
	private void buildGrove(int cx, int cy) {
		for (int yy = cy - 1; yy <= cy + 1; yy++) {
			for (int xx = cx - 1; xx <= cx + 1; xx++) {
				if (xx <= 0 || yy <= 0 || xx >= width - 1 || yy >= height - 1) {
					continue;
				}
				grid[yy][xx] = Tile.BAUM;
			}
		}
		grid[cy][cx] = Tile.GRAS; // Feld des NPC
		if (cy + 1 < height - 1) {
			grid[cy + 1][cx] = Tile.GRAS; // Eingang von unten
		}
	}

	/** Wasser-Teich (begehbar) rund um den NPC; der NPC selbst steht auf Gras. */
	private void buildLake(int cx, int cy) {
		for (int yy = cy - 1; yy <= cy + 1; yy++) {
			for (int xx = cx - 1; xx <= cx + 1; xx++) {
				if (xx <= 0 || yy <= 0 || xx >= width - 1 || yy >= height - 1) {
					continue;
				}
				grid[yy][xx] = Tile.WASSER;
			}
		}
		grid[cy][cx] = Tile.GRAS; // Feld des NPC
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
			if (isBossAt(nx, ny) || isNpcAt(nx, ny)) {
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
			if (grid[y][x].isBlocking() || isOccupiedByWild(x, y)
					|| isBossAt(x, y) || isNpcAt(x, y)) {
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
