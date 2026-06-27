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
import koeln.uni.idh.java1.session11.zoo.world.Npc;
import koeln.uni.idh.java1.session11.zoo.world.NpcKind;
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

	public void renderWorld(World world, int teamSize, int teamMax) {
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
				if (world.isBossAt(x, y)) {
					sb.append(BOLD).append(MAGENTA).append('P').append(RESET);
					continue;
				}
				Npc npc = world.npcAt(x, y);
				if (npc != null) {
					sb.append(BOLD).append(npcColor(npc.getKind()))
							.append(npc.getGlyph()).append(RESET);
					continue;
				}
				WalkingMammal animal = animalAt(wild, x, y);
				if (animal != null) {
					// Farbe nach Level (nicht Typ): so erkennt man auf der Karte,
					// wo es starke Tiere herzieht – das Level steht hier sonst nirgends.
					sb.append(BOLD).append(levelColor(animal.getLevel()))
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
				.append(GRAY).append("Ton: ").append(RESET)
				.append(Sound.isEnabled() ? "an" : "aus").append(" (M)   ")
				.append(GRAY).append("Beenden: ").append(RESET).append("Q\n");
		sb.append(GRAY).append("Wilde Tiere: ").append(RESET).append(wild.size())
				.append(GRAY).append("    Team: ").append(RESET)
				.append(teamSize).append("/").append(teamMax);
		if (world.isBossPresent()) {
			sb.append(GRAY).append("    Ziel: ").append(RESET)
					.append(MAGENTA).append("P").append(RESET)
					.append(GRAY).append(" = Der Professor").append(RESET);
		}
		sb.append('\n');
		// Legende: was die Tier-Farben über das Level verraten.
		sb.append(GRAY).append("Lv-Farbe: ").append(RESET)
				.append(WHITE).append("0-9 ").append(RESET)
				.append(CYAN).append("10-19 ").append(RESET)
				.append(YELLOW).append("20-29 ").append(RESET)
				.append(RED).append("30+").append(RESET).append('\n');
		// Legende der freundlichen NPCs (sofern welche da sind).
		if (!world.getNpcs().isEmpty()) {
			sb.append(GRAY).append("NPCs: ").append(RESET);
			for (Npc npc : world.getNpcs()) {
				sb.append(BOLD).append(npcColor(npc.getKind())).append(npc.getGlyph())
						.append(RESET).append(' ').append(npcLabel(npc.getKind())).append("  ");
			}
			sb.append('\n');
		}
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

	// ---------------- Titelbildschirm ----------------

	/** Anzahl der Frames der Titel-Einblendung (vgl. {@link #renderTitle}). */
	public static final int TITLE_FRAMES = 5;

	/**
	 * Der Titelbildschirm mit großem „ZOOKEMON"-Logo. Über {@code frame} (0..
	 * {@link #TITLE_FRAMES}) wird die Einblendung gesteuert: Das Logo fliegt von
	 * links herein und färbt sich dabei von Grau über Cyan bis Magenta – ein
	 * kleiner Gruß an die rotierenden ASCII-Bilder aus den Übungen.
	 *
	 * @param frame  aktueller Animations-Schritt
	 * @param prompt ob der „Taste drücken"-Hinweis schon angezeigt wird
	 */
	public void renderTitle(int frame, boolean prompt) {
		String[] palette = { GRAY, BLUE, CYAN, BRIGHT_GREEN, MAGENTA };
		String color = palette[Math.min(frame, palette.length - 1)];
		int slide = Math.max(0, TITLE_FRAMES - frame);
		String indent = spaces(4 + slide * 3);
		boolean settled = frame >= TITLE_FRAMES;

		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR).append("\n\n");
		for (String line : Sprites.titleLogo()) {
			sb.append(indent).append(BOLD).append(color).append(line).append(RESET).append('\n');
		}
		sb.append('\n');
		String pre = settled ? "✨ " : "";
		String post = settled ? " ✨" : "";
		sb.append(spaces(8)).append(BOLD).append(MAGENTA)
				.append(pre).append("Der Lachs, der sie alle fängt").append(post)
				.append(RESET).append("\n");
		sb.append(spaces(8)).append(GRAY)
				.append("Entkomme der ASCII-Welt des Professors!").append(RESET).append("\n\n\n");
		if (prompt) {
			sb.append(spaces(8)).append(BOLD).append(YELLOW)
					.append("▶ Drücke eine Taste, um zu starten").append(RESET).append('\n');
			sb.append(spaces(8)).append(GRAY)
					.append("Ton: ").append(Sound.isEnabled() ? "an" : "aus")
					.append(" (im Spiel mit M umschaltbar)").append(RESET).append('\n');
		}
		print(sb);
	}

	// ---------------- Bosskampf-Inszenierung ----------------

	/**
	 * Ganzseitige Boss-Inszenierung: das Bildnis des Professors, dazu zwei
	 * Textzeilen. Über {@code frame} entstehen Screen-Shake und ein rotes
	 * Aufblitzen – für den dramatischen Auftritt wie für den Wutausbruch in
	 * Phase 2.
	 *
	 * @param line1 große Überschrift (z. B. der Name)
	 * @param line2 Untertitel (z. B. „fordert dich heraus!")
	 * @param frame Animations-Schritt (steuert Wackeln und Blitz)
	 */
	public void renderBossSplash(String line1, String line2, int frame) {
		boolean flash = frame % 2 == 0;
		String color = flash ? (BOLD + RED) : (BOLD + MAGENTA);
		String pad = spaces(frame % 2 == 0 ? 2 : 0);

		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR).append("\n\n");
		for (String line : Sprites.professor()) {
			sb.append(pad).append(spaces(8)).append(color).append(line).append(RESET).append('\n');
		}
		sb.append('\n');
		sb.append(pad).append(spaces(8)).append(BOLD).append(RED).append("⚡ ").append(line1)
				.append(" ⚡").append(RESET).append('\n');
		if (line2 != null && !line2.isEmpty()) {
			sb.append(pad).append(spaces(8)).append(BOLD).append(YELLOW).append(line2)
					.append(RESET).append('\n');
		}
		print(sb);
	}

	// ---------------- Dialog & Intro ----------------

	// Bewusst schmal gehalten (passt zur 40 Zeichen breiten Welt), damit der
	// Text auch in schmalen Terminals nicht mitten im Wort umbricht.
	private static final int DIALOGUE_WIDTH = 36;

	/**
	 * Eine Dialog-Textbox mit Sprecher (oder null = Erzähler) und Weiter-Hinweis.
	 * Bewusst nur mit linker Kante und oberer/unterer Linie (wie das Kampf-Log):
	 * So gibt es keine rechte Kante, die sich an Sonderzeichen verschieben könnte –
	 * die Box sitzt in jedem Terminal sauber.
	 */
	public void renderDialogue(String speaker, String text, boolean hasMore) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("🦙⚔️  Zookémon").append(RESET).append("\n\n\n");
		if (speaker != null) {
			sb.append("  ").append(BOLD).append(MAGENTA).append(speaker).append(":").append(RESET).append('\n');
		}
		String rule = repeat('─', DIALOGUE_WIDTH);
		sb.append("  ").append(GRAY).append("┌").append(rule).append(RESET).append('\n');
		for (String line : wrap(text, DIALOGUE_WIDTH)) {
			sb.append("  ").append(GRAY).append("│ ").append(RESET).append(line).append('\n');
		}
		sb.append("  ").append(GRAY).append("└").append(rule).append(RESET).append("\n\n");
		sb.append("  ").append(GRAY)
				.append(hasMore ? "Taste drücken für weiter ▼" : "Taste drücken …")
				.append(RESET).append('\n');
		print(sb);
	}

	/** Bricht einen Text wortweise auf die gegebene Breite um. */
	private List<String> wrap(String text, int width) {
		List<String> lines = new java.util.ArrayList<>();
		StringBuilder line = new StringBuilder();
		for (String word : text.split(" ")) {
			if (line.length() > 0 && line.length() + 1 + word.length() > width) {
				lines.add(line.toString());
				line.setLength(0);
			}
			if (line.length() > 0) {
				line.append(' ');
			}
			line.append(word);
		}
		lines.add(line.toString());
		return lines;
	}

	/** Die Starter-Auswahl: drei wählbare Zookémon mit Typ und HP. */
	public void renderStarterSelect(WalkingMammal[] options) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("🦙⚔️  Wähle dein erstes Zookémon!").append(RESET).append("\n\n");
		for (int i = 0; i < options.length; i++) {
			WalkingMammal a = options[i];
			sb.append("  ").append(YELLOW).append(i + 1).append(RESET).append(") ")
					.append(BOLD).append(emojiFor(a.getSymbol())).append(a.getName()).append(RESET)
					.append("  ").append(typeColor(a.getType())).append("(")
					.append(a.getType().getDisplayName()).append(")").append(RESET)
					.append(GRAY).append("  ").append(a.getMaxHp()).append(" HP").append(RESET)
					.append('\n');
		}
		sb.append('\n').append(GRAY).append("Deine Wahl (Ziffer):").append(RESET).append('\n');
		print(sb);
	}

	/** Abspann nach dem Sieg über den Erzfeind. */
	public void renderEnding(WalkingMammal champion, String bossName, int victories) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR).append("\n\n");
		sb.append(BOLD).append(MAGENTA).append("  ✨💥  W E L T   Z E R S T Ö R T  💥✨").append(RESET).append("\n\n\n");
		sb.append("  Du hast ").append(BOLD).append(bossName).append(RESET)
				.append(" besiegt! Die ASCII-Welt zerfällt um dich herum –\n");
		sb.append("  deine Java-Fähigkeiten haben dich befreit. Du bist frei!\n\n");
		sb.append("  ").append(emojiFor(champion.getSymbol())).append(BOLD).append(champion.getName())
				.append(RESET);
		if (champion.isEvolved()) {
			sb.append(MAGENTA).append("★").append(RESET);
		}
		sb.append(GRAY).append("  Lv ").append(RESET)
				.append(levelColor(champion.getLevel())).append(champion.getLevel()).append(RESET).append('\n');
		sb.append("  ").append(GRAY).append("Gewonnene Kämpfe insgesamt: ").append(RESET)
				.append(victories).append("\n\n\n");
		sb.append(GRAY).append("  Taste drücken, um das Spiel zu beenden …").append(RESET).append('\n');
		print(sb);
	}

	private String repeat(char c, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	// ---------------- Kampf ----------------

	private static final int PROJECTILE_WIDTH = 38;
	private static final int ENEMY_SPRITE_INDENT = 22;
	private static final int PLAYER_SPRITE_INDENT = 4;
	private static final int LOG_LINES = 5;

	/**
	 * Normaler Kampfbildschirm mit Attacken-Menü, wenn der Spieler dran ist.
	 *
	 * @param canCatch ob die Option „Fangen" angeboten wird (nur im Wildkampf)
	 * @param canFlee  ob die Option „Fliehen" angeboten wird (nur im Wildkampf)
	 */
	public void renderBattle(Battle battle, boolean canCatch, boolean canFlee) {
		renderScene(battle, true, null, canCatch, canFlee);
	}

	/** Intro: das Gegner-Tier erscheint – ohne Menü, für einen kurzen Moment. */
	public void renderBattleIntro(Battle battle) {
		renderScene(battle, false, null, false, false);
	}

	/**
	 * Animations-Frame während des Rundenablaufs (ohne Menü), mit optionalen
	 * Effekten: fliegendes Projektil, Treffer-Blitz, Screen-Shake.
	 */
	public void renderBattleFx(Battle battle, BattleFx fx) {
		renderScene(battle, false, fx, false, false);
	}

	private void renderScene(Battle battle, boolean showMenu, BattleFx fx,
			boolean canCatch, boolean canFlee) {
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
			sb.append("  ");
			if (canCatch) {
				sb.append(YELLOW).append("Z").append(RESET).append(") Fangen   ");
			}
			sb.append(YELLOW).append("W").append(RESET).append(") Tier wechseln");
			if (canFlee) {
				sb.append("   ").append(YELLOW).append("F").append(RESET).append(") Fliehen");
			}
			sb.append('\n');
		}

		print(sb);
	}

	/**
	 * Team-Auswahlbildschirm für den Tierwechsel. Besiegte und das gerade aktive
	 * Tier sind nicht wählbar.
	 *
	 * @param members     alle Tiere im Team
	 * @param activeIndex Index des aktiven Tieres
	 * @param forced      true, wenn der Wechsel erzwungen ist (kein Abbrechen)
	 */
	public void renderTeamMenu(List<WalkingMammal> members, int activeIndex, boolean forced) {
		StringBuilder sb = new StringBuilder();
		sb.append(CLEAR);
		sb.append(BOLD).append("🐾  Dein Team").append(RESET).append("\n\n");
		if (forced) {
			sb.append(RED).append("  Dein Tier wurde besiegt! Wähle ein neues:").append(RESET).append("\n\n");
		} else {
			sb.append(GRAY).append("  Welches Tier soll kämpfen?").append(RESET).append("\n\n");
		}

		for (int i = 0; i < members.size(); i++) {
			WalkingMammal m = members.get(i);
			boolean active = i == activeIndex;
			boolean fainted = m.isFainted();
			boolean selectable = !active && !fainted;

			sb.append("  ");
			if (selectable) {
				sb.append(YELLOW).append(i + 1).append(RESET).append(") ");
			} else {
				sb.append(GRAY).append(i + 1).append(")").append(RESET).append(" ");
			}
			sb.append(emojiFor(m.getSymbol())).append(BOLD).append(m.getName()).append(RESET);
			if (m.isEvolved()) {
				sb.append(MAGENTA).append("★").append(RESET);
			}
			sb.append(GRAY).append("  Lv ").append(RESET)
					.append(levelColor(m.getLevel())).append(m.getLevel()).append(RESET);
			sb.append("  ").append(hpBar(m));
			if (fainted) {
				sb.append(RED).append("  ✗ besiegt").append(RESET);
			} else if (active) {
				sb.append(BRIGHT_GREEN).append("  ◄ aktiv").append(RESET);
			}
			sb.append('\n');
		}

		sb.append('\n');
		if (forced) {
			sb.append(GRAY).append("Wähle mit der Ziffer.").append(RESET).append('\n');
		} else {
			sb.append(GRAY).append("Wähle mit der Ziffer, W oder F bricht ab.").append(RESET).append('\n');
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
				.append(RESET).append(GRAY).append("  Lv ").append(RESET)
				.append(levelColor(player.getLevel())).append(player.getLevel()).append(RESET)
				.append(GRAY).append("  (EP ").append(player.getXp()).append("/")
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
		sb.append(GRAY).append("  Lv ").append(RESET)
				.append(levelColor(b.getLevel())).append(b.getLevel()).append(RESET);
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
		case GEFANGEN:
			return MAGENTA + "Gefangen! Ein neuer Freund! 🤝";
		default:
			return "";
		}
	}

	/**
	 * Farbe nach Stärke (Level): je höher das Level, desto „heißer" die Farbe.
	 * weiß ≤ 9, cyan 10–19, gelb 20–29, rot ≥ 30. Die gleiche Skala wird auf der
	 * Karte (Tier-Symbol) und bei der „Lv X"-Zahl verwendet, damit Farben überall
	 * dasselbe bedeuten. (Bewusst kein Grün – das ließe schwache Tiere wie Bäume
	 * aussehen.)
	 */
	private String levelColor(int level) {
		if (level < 10) {
			return WHITE;
		}
		if (level < 20) {
			return CYAN;
		}
		if (level < 30) {
			return YELLOW;
		}
		return RED;
	}

	/** Farbe eines NPC auf der Karte – bewusst andere Töne als Tiere/Boss. */
	private String npcColor(NpcKind kind) {
		switch (kind) {
		case KOMMILITONE:
			return CYAN;
		case KI:
		default:
			return WHITE;
		}
	}

	/** Kurzbezeichnung eines NPC für die Karten-Legende. */
	private String npcLabel(NpcKind kind) {
		switch (kind) {
		case KOMMILITONE:
			return "Kommilitone";
		case KI:
		default:
			return "K.I.";
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
