package koeln.uni.idh.java1.session11.zoo.ui;

import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Status;
import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.world.Tile;
import koeln.uni.idh.java1.session11.zoo.world.World;

/**
 * Zeichnet Overworld und Kampfbildschirm mit ANSI-Farben. Im Kampf werden
 * Emoji-Symbole verwendet, auf der Karte einfache Buchstaben (damit das Gitter
 * sauber ausgerichtet bleibt).
 */
public class Renderer {

	// ANSI-Codes
	private static final String RESET = "[0m";
	private static final String BOLD = "[1m";
	private static final String CLEAR = "[2J[H";

	private static final String GREEN = "[32m";
	private static final String BRIGHT_GREEN = "[92m";
	private static final String BLUE = "[34m";
	private static final String CYAN = "[36m";
	private static final String RED = "[31m";
	private static final String YELLOW = "[33m";
	private static final String GRAY = "[90m";
	private static final String WHITE = "[97m";

	private final boolean useEmoji;
	private final boolean crlf;

	/**
	 * @param useEmoji ob Emoji-Symbole verwendet werden
	 * @param rawMode  ob das Terminal im Roh-Modus läuft. Dann muss jede Zeile
	 *                 mit \r\n enden, sonst „treppt" die Ausgabe, weil ein
	 *                 bloßes \n den Cursor nicht an den Zeilenanfang setzt.
	 */
	public Renderer(boolean useEmoji, boolean rawMode) {
		this.useEmoji = useEmoji;
		this.crlf = rawMode;
	}

	public void clear() {
		System.out.print(CLEAR);
		System.out.flush();
	}

	/** Gibt einen Frame aus und übersetzt Zeilenenden passend zum Terminalmodus. */
	private void print(StringBuilder sb) {
		String out = crlf ? sb.toString().replace("\n", "\r\n") : sb.toString();
		System.out.print(out);
		System.out.flush();
	}

	// ---------------- Overworld ----------------

	public void renderWorld(World world) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("🦙⚔️  Zookémon – Overworld").append(RESET).append("\n\n");

		WalkingMammal player = world.getPlayer();
		List<WalkingMammal> wild = world.getWildAnimals();

		for (int y = 0; y < world.getHeight(); y++) {
			for (int x = 0; x < world.getWidth(); x++) {
				if (player != null && player.getX() == x && player.getY() == y) {
					sb.append(BOLD).append(YELLOW).append('@').append(RESET);
					continue;
				}
				WalkingMammal animal = animalAt(wild, x, y);
				if (animal != null) {
					sb.append(BOLD).append(typeColor(animal.getType()))
							.append(animal.getSymbol()).append(RESET);
					continue;
				}
				sb.append(tileCell(world.getTile(x, y)));
			}
			sb.append('\n');
		}

		sb.append('\n');
		sb.append(GRAY).append("Bewegung: ").append(RESET)
				.append("W/A/S/D   ")
				.append(GRAY).append("Beenden: ").append(RESET).append("Q\n");
		sb.append(GRAY).append("Wilde Tiere: ").append(RESET).append(wild.size()).append('\n');
		print(sb);
	}

	private WalkingMammal animalAt(List<WalkingMammal> animals, int x, int y) {
		for (WalkingMammal a : animals) {
			if (a.getX() == x && a.getY() == y) {
				return a;
			}
		}
		return null;
	}

	private String tileCell(Tile tile) {
		switch (tile) {
		case GRAS:
			return GREEN + tile.getSymbol() + RESET;
		case WASSER:
			return BLUE + tile.getSymbol() + RESET;
		case BAUM:
			return BRIGHT_GREEN + tile.getSymbol() + RESET;
		case WAND:
		default:
			return GRAY + tile.getSymbol() + RESET;
		}
	}

	// ---------------- Kampf ----------------

	public void renderBattle(Battle battle) {
		Battler player = battle.getPlayer();
		Battler enemy = battle.getEnemy();

		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("⚔️  KAMPF").append(RESET).append("\n\n");

		// Gegner oben
		sb.append("   ").append(battlerHeader(enemy)).append('\n');
		sb.append("   ").append(hpBar(enemy)).append('\n');
		sb.append('\n');
		// Spieler unten
		sb.append("            ").append(battlerHeader(player)).append('\n');
		sb.append("            ").append(hpBar(player)).append('\n');
		sb.append('\n');

		// Kampf-Log (letzte Zeilen)
		sb.append(GRAY).append("──────── Kampf-Log ────────").append(RESET).append('\n');
		List<String> log = battle.getLog();
		int from = Math.max(0, log.size() - 5);
		for (int i = from; i < log.size(); i++) {
			sb.append("  ").append(log.get(i)).append('\n');
		}
		sb.append('\n');

		if (battle.isOver()) {
			sb.append(BOLD).append(resultText(battle)).append(RESET).append('\n');
			sb.append(GRAY).append("Taste drücken, um fortzufahren …").append(RESET).append('\n');
		} else {
			sb.append(BOLD).append("Deine Attacken:").append(RESET).append('\n');
			List<Move> moves = player.getMoves();
			for (int i = 0; i < moves.size(); i++) {
				Move m = moves.get(i);
				sb.append("  ").append(YELLOW).append(i + 1).append(RESET).append(") ")
						.append(m.getName())
						.append(GRAY).append(" [").append(m.getType().getDisplayName())
						.append(", Stk ").append(m.getPower())
						.append(", Gen ").append(m.getAccuracy()).append("%]").append(RESET)
						.append('\n');
			}
			sb.append("  ").append(YELLOW).append("F").append(RESET).append(") Fliehen\n");
		}

		print(sb);
	}

	private String battlerHeader(Battler b) {
		StringBuilder sb = new StringBuilder();
		sb.append(BOLD).append(emojiFor(b.getSymbol())).append(b.getName()).append(RESET);
		sb.append("  ").append(typeColor(b.getType())).append("(")
				.append(b.getType().getDisplayName()).append(")").append(RESET);
		sb.append(GRAY).append("  Lv ").append(b.getLevel()).append(RESET);
		if (b.getStatus() != Status.KEINER) {
			sb.append("  ").append(RED).append("[").append(b.getStatus().getDisplayName())
					.append("]").append(RESET);
		}
		return sb.toString();
	}

	private String hpBar(Battler b) {
		int width = 20;
		double ratio = (double) b.getCurrentHp() / b.getMaxHp();
		int filled = (int) Math.round(ratio * width);
		String color = ratio > 0.5 ? GREEN : ratio > 0.2 ? YELLOW : RED;

		StringBuilder bar = new StringBuilder();
		bar.append(color).append("[");
		for (int i = 0; i < width; i++) {
			bar.append(i < filled ? '#' : '-');
		}
		bar.append("]").append(RESET);
		bar.append(" ").append(b.getCurrentHp()).append("/").append(b.getMaxHp()).append(" HP");
		return bar.toString();
	}

	private String resultText(Battle battle) {
		switch (battle.getResult()) {
		case SPIELER_GEWINNT:
			return BRIGHT_GREEN + "Du hast gewonnen! 🎉";
		case SPIELER_VERLIERT:
			return RED + "Du wurdest besiegt … 💀";
		case GEFLOHEN:
			return CYAN + "Erfolgreich geflohen! 🏃";
		default:
			return "";
		}
	}

	private String typeColor(Type type) {
		switch (type) {
		case FEUER:
			return RED;
		case WASSER:
			return BLUE;
		case PFLANZE:
			return GREEN;
		case ERDE:
			return YELLOW;
		case LUFT:
			return CYAN;
		case NORMAL:
		default:
			return WHITE;
		}
	}

	private String emojiFor(char symbol) {
		if (!useEmoji) {
			return "";
		}
		switch (symbol) {
		case 'A':
			return "🦙 ";
		case 'H':
			return "🐴 ";
		case 'E':
			return "🐘 ";
		case 'L':
			return "🦁 ";
		case 'D':
			return "🦅 ";
		case 'K':
			return "🐊 ";
		default:
			return "";
		}
	}
}
