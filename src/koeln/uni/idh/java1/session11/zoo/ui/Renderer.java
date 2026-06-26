package koeln.uni.idh.java1.session11.zoo.ui;

import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.battle.LevelUpResult;
import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Status;
import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.battle.TypeChart;
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

	private static final String MAGENTA = "[95m";

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

	private static final int PROJECTILE_WIDTH = 38;
	private static final int ENEMY_SPRITE_INDENT = 22;
	private static final int PLAYER_SPRITE_INDENT = 4;
	private static final int LOG_LINES = 5;

	/** Normaler Kampfbildschirm: mit Attacken-Menü, wenn der Spieler dran ist. */
	public void renderBattle(Battle battle) {
		renderScene(battle, true, null);
	}

	/** Intro: das wilde Tier erscheint – ohne Menü, für einen kurzen Moment. */
	public void renderBattleIntro(Battle battle) {
		renderScene(battle, false, null);
	}

	/**
	 * Animations-Frame während des Rundenablaufs (ohne Menü), mit optionalen
	 * Effekten: fliegendes Projektil, Treffer-Blitz, Screen-Shake.
	 */
	public void renderBattleFx(Battle battle, BattleFx fx) {
		renderScene(battle, false, fx);
	}

	private void renderScene(Battle battle, boolean showMenu, BattleFx fx) {
		Battler player = battle.getPlayer();
		Battler enemy = battle.getEnemy();
		String pad = spaces(fx != null ? fx.shake : 0);

		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("⚔️  KAMPF").append(RESET).append("\n\n");

		// Gegner: Info + HP, darunter sein Sprite
		sb.append(pad).append("  ").append(battlerHeader(enemy)).append(hitMarker(enemy, fx)).append('\n');
		sb.append(pad).append("  ").append(hpBar(enemy)).append('\n');
		appendSprite(sb, enemy, fx, ENEMY_SPRITE_INDENT, pad);

		// Effekt-Zeile mit dem fliegenden Projektil
		sb.append(pad).append(projectileRow(fx)).append('\n');

		// Spieler: Sprite, darunter Info + HP
		appendSprite(sb, player, fx, PLAYER_SPRITE_INDENT, pad);
		sb.append(pad).append("  ").append(battlerHeader(player)).append(hitMarker(player, fx)).append('\n');
		sb.append(pad).append("  ").append(hpBar(player)).append('\n');
		sb.append('\n');

		// Kampf-Log: immer LOG_LINES Zeilen, damit das Layout ruhig bleibt
		sb.append(GRAY).append("──────── Kampf-Log ────────").append(RESET).append('\n');
		List<String> log = battle.getLog();
		int from = Math.max(0, log.size() - LOG_LINES);
		int shown = log.size() - from;
		for (int i = 0; i < LOG_LINES - shown; i++) {
			sb.append('\n');
		}
		for (int i = from; i < log.size(); i++) {
			sb.append("  ").append(log.get(i)).append('\n');
		}
		sb.append('\n');

		if (battle.isOver()) {
			sb.append(BOLD).append(resultText(battle)).append(RESET).append('\n');
			sb.append(GRAY).append("Taste drücken, um fortzufahren …").append(RESET).append('\n');
		} else if (showMenu) {
			sb.append(BOLD).append("Deine Attacken:").append(RESET).append('\n');
			List<Move> moves = player.getMoves();
			for (int i = 0; i < moves.size(); i++) {
				sb.append("  ").append(YELLOW).append(i + 1).append(RESET).append(") ")
						.append(moveLine(moves.get(i), enemy)).append('\n');
			}
			sb.append("  ").append(YELLOW).append("F").append(RESET).append(") Fliehen\n");
		}

		print(sb);
	}

	/** Hängt das ASCII-Bild eines Tieres an – rot blinkend, wenn es getroffen wird. */
	private void appendSprite(StringBuilder sb, Battler b, BattleFx fx, int indent, String pad) {
		boolean flashing = fx != null && fx.flash == b;
		String color = flashing ? (BOLD + RED) : typeColor(b.getType());
		for (String line : Sprites.forSymbol(b.getSymbol())) {
			sb.append(pad).append(spaces(indent)).append(color).append(line).append(RESET).append('\n');
		}
	}

	/** Baut die Zeile mit dem fliegenden Projektil (oder eine leere Zeile). */
	private String projectileRow(BattleFx fx) {
		char[] row = new char[PROJECTILE_WIDTH];
		for (int i = 0; i < row.length; i++) {
			row[i] = ' ';
		}
		if (fx != null && fx.projectileType != null) {
			char glyph = projectileGlyph(fx.projectileType);
			for (int k = 0; k < 3; k++) {
				int c = fx.projectileColumn - k;
				if (c >= 0 && c < PROJECTILE_WIDTH) {
					row[c] = glyph;
				}
			}
			return "      " + typeColor(fx.projectileType) + BOLD + new String(row) + RESET;
		}
		return "      " + new String(row);
	}

	private char projectileGlyph(Type type) {
		switch (type) {
		case FEUER:
			return '*';
		case WASSER:
			return '~';
		case PFLANZE:
			return '%';
		case ERDE:
			return 'o';
		case LUFT:
			return '-';
		case NORMAL:
		default:
			return '+';
		}
	}

	private String spaces(int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	/** Eine Menüzeile inkl. Hinweis auf die Typ-Effektivität gegen den Gegner. */
	private String moveLine(Move m, Battler enemy) {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getName());
		sb.append(GRAY).append(" [").append(m.getType().getDisplayName())
				.append(", Stk ").append(m.getPower())
				.append(", Gen ").append(m.getAccuracy()).append("%]").append(RESET);
		if (m.getPower() > 0) {
			double eff = TypeChart.effectiveness(m.getType(), enemy.getType());
			if (eff > 1.0) {
				sb.append(BRIGHT_GREEN).append(" ⚡ sehr effektiv").append(RESET);
			} else if (eff < 1.0) {
				sb.append(GRAY).append(" ▽ schwach").append(RESET);
			}
		}
		return sb.toString();
	}

	/** Der „Treffer!"-Blitz neben dem gerade getroffenen Tier. */
	private String hitMarker(Battler b, BattleFx fx) {
		if (fx != null && fx.flash == b) {
			return "  " + BOLD + RED + "💥 TREFFER!" + RESET;
		}
		return "";
	}

	/**
	 * Ein Frame der Entwicklungs-Animation: das Tier flackert zwischen seinem
	 * Bild und einer Silhouette.
	 *
	 * @param symbol     Karten-Symbol des Tieres (wählt das Sprite)
	 * @param text       Überschrift über dem Bild
	 * @param silhouette true = nur Umriss (alle Zeichen als Block)
	 */
	public void renderEvolutionFrame(char symbol, String text, boolean silhouette) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR).append("\n\n");
		sb.append(BOLD).append(MAGENTA).append("  ✨ ").append(text).append(" ✨").append(RESET).append("\n\n\n");
		String color = silhouette ? MAGENTA : BRIGHT_GREEN;
		for (String line : Sprites.forSymbol(symbol)) {
			sb.append("            ").append(BOLD).append(color)
					.append(silhouette ? toSilhouette(line) : line).append(RESET).append('\n');
		}
		print(sb);
	}

	private String toSilhouette(String line) {
		StringBuilder sb = new StringBuilder();
		for (char c : line.toCharArray()) {
			sb.append(c == ' ' ? ' ' : '#');
		}
		return sb.toString();
	}

	/** Sieg-Zusammenfassung: besiegtes Tier, Erfahrung, Level-Fortschritt, Bilanz. */
	public void renderVictory(Battler player, Battler enemy, int epGained, int totalEp,
			int victories, LevelUpResult level) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append(BRIGHT_GREEN).append("🎉  SIEG!").append(RESET).append("\n\n");

		sb.append("  Du hast das wilde ").append(BOLD).append(emojiFor(enemy.getSymbol()))
				.append(enemy.getName()).append(RESET).append(" besiegt!\n\n");

		sb.append("  ").append(YELLOW).append("Erfahrung erhalten:").append(RESET)
				.append("  +").append(epGained).append(" EP\n");
		sb.append("  ").append(GRAY).append("Siege insgesamt:").append(RESET)
				.append("     ").append(victories).append("\n\n");

		if (level != null && level.leveledUp()) {
			sb.append("  ").append(BOLD).append(BRIGHT_GREEN)
					.append("⬆ Level ").append(level.getNewLevel()).append(" erreicht!")
					.append(RESET).append('\n');
		}
		if (level != null && level.isEvolved()) {
			sb.append("  ").append(BOLD).append(MAGENTA)
					.append("✨ ").append(level.getOldName()).append(" → ")
					.append(level.getNewName()).append("!").append(RESET).append('\n');
		}

		sb.append('\n');
		sb.append("  ").append(emojiFor(player.getSymbol())).append(BOLD).append(player.getName())
				.append(RESET).append(GRAY).append("  Lv ").append(player.getLevel())
				.append("  (EP ").append(player.getXp()).append("/")
				.append(player.getXpForNextLevel()).append(")").append(RESET).append('\n');
		sb.append("  ").append(hpBar(player)).append("\n\n");

		sb.append(GRAY).append("Taste drücken, um weiterzuziehen …").append(RESET).append('\n');
		print(sb);
	}

	private String battlerHeader(Battler b) {
		StringBuilder sb = new StringBuilder();
		sb.append(BOLD).append(emojiFor(b.getSymbol())).append(b.getName()).append(RESET);
		if (b.isEvolved()) {
			sb.append(MAGENTA).append("★").append(RESET);
		}
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
