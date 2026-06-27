package koeln.uni.idh.java1.session11.zoo.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.animals.AnimalFactory;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.engine.DialogueState.Page;
import koeln.uni.idh.java1.session11.zoo.ui.Input;
import koeln.uni.idh.java1.session11.zoo.ui.Renderer;
import koeln.uni.idh.java1.session11.zoo.world.Npc;
import koeln.uni.idh.java1.session11.zoo.world.NpcKind;
import koeln.uni.idh.java1.session11.zoo.world.World;

/**
 * Der Spielablauf. Hält die zentralen Bausteine (Welt, Eingabe, Renderer) und
 * den aktuellen {@link GameState}. Die Hauptschleife ist bewusst einfach:
 * zeichnen, eine Taste lesen, Taste an den Zustand geben.
 */
public class Game {

	private static final int WORLD_WIDTH = 40;
	private static final int WORLD_HEIGHT = 18;
	private static final int NUM_WILD_ANIMALS = 6;

	/** Fester Standort des Professors in der Welt. */
	private static final int BOSS_X = WORLD_WIDTH - 4;
	private static final int BOSS_Y = 3;
	/** Level (entwickelte Form), auf das das Zookémon des Professors gebracht wird. */
	private static final int BOSS_LEVEL = 50;

	/** Feste Standorte der beiden hilfreichen NPCs (unten links / unten rechts). */
	private static final int FRIEND1_X = 5;
	private static final int FRIEND1_Y = WORLD_HEIGHT - 5;
	private static final int FRIEND2_X = WORLD_WIDTH - 7;
	private static final int FRIEND2_Y = WORLD_HEIGHT - 5;

	private final Random rng = new Random();
	private final Input input;
	private final Renderer renderer;

	private World world;
	private final Team team = new Team();
	private GameState state;
	private boolean running = false;

	private Trainer rival;
	private boolean bossDefeated = false;

	private int victories = 0;
	private int totalEp = 0;

	// Aufsummierte NPC-Buffs, damit auch später gefangene Tiere sie erhalten.
	private int teamBuffHp = 0;
	private int teamBuffAtk = 0;
	private int teamBuffDef = 0;
	private int teamBuffSpd = 0;

	public Game() {
		this.input = new Input();
		this.renderer = new Renderer(true, input.isRawMode());
	}

	public Random getRng() {
		return rng;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public World getWorld() {
		return world;
	}

	/** Das aktuell aktive Tier des Spielers (Team-Anführer). */
	public WalkingMammal getPlayer() {
		return team.getActive();
	}

	public Team getTeam() {
		return team;
	}

	/**
	 * Wechselt das aktive Tier des Teams und übernimmt dessen Position in der
	 * Welt, damit der Wechsel auch in der Overworld stimmt.
	 */
	public void setActiveAnimal(int index) {
		WalkingMammal previous = team.getActive();
		team.setActive(index);
		if (world != null) {
			world.setPlayer(team.getActive(), previous.getX(), previous.getY());
		}
	}

	public void setState(GameState state) {
		this.state = state;
		state.onEnter();
	}

	public void quit() {
		this.running = false;
	}

	public int getVictories() {
		return victories;
	}

	public int getTotalEp() {
		return totalEp;
	}

	/** Verbucht einen Sieg und die dafür erhaltene Erfahrung. */
	public void registerVictory(int ep) {
		victories++;
		totalEp += ep;
	}

	/**
	 * Spawnt ein neues zufälliges wildes Tier an einer freien Stelle. Sein
	 * Level orientiert sich am Spieler (±1), damit die Kämpfe spannend bleiben.
	 */
	public void spawnWildAnimal() {
		int[] pos = world.randomFreeTile();
		WalkingMammal wild = AnimalFactory.randomWild(rng);
		if (team.size() > 0) {
			int playerLevel = getPlayer().getLevel();
			int target = Math.max(5, playerLevel + rng.nextInt(3) - 1);
			// Je höher das eigene Level, desto häufiger taucht ein deutlich
			// stärkeres Tier auf (gedeckelt bei 50 %, ab ~Lv 25 erreicht).
			double strongChance = Math.min(0.5, playerLevel * 0.02);
			if (rng.nextDouble() < strongChance) {
				target += 2 + rng.nextInt(4); // +2..+5 Level obendrauf
			}
			wild.scaleToLevel(target);
		}
		world.addWildAnimal(wild, pos[0], pos[1]);
	}

	/**
	 * Nimmt ein gefangenes Tier ins Team auf und versieht es mit den bereits
	 * erspielten NPC-Buffs, damit der ganze Trupp gleich stark bleibt.
	 */
	public void addCaughtAnimal(WalkingMammal animal) {
		animal.applyPermanentBuff(teamBuffHp, teamBuffAtk, teamBuffDef, teamBuffSpd);
		team.add(animal);
	}

	/**
	 * Wendet einen dauerhaften Buff auf das gesamte aktuelle Team an und merkt ihn
	 * sich, damit auch künftig gefangene Tiere ihn bekommen.
	 */
	public void applyTeamBuff(int hp, int atk, int def, int spd) {
		teamBuffHp += hp;
		teamBuffAtk += atk;
		teamBuffDef += def;
		teamBuffSpd += spd;
		for (WalkingMammal m : team.getMembers()) {
			m.applyPermanentBuff(hp, atk, def, spd);
		}
	}

	public Trainer getRival() {
		return rival;
	}

	/** Markiert den Professor als besiegt und entfernt ihn aus der Welt. */
	public void markBossDefeated() {
		this.bossDefeated = true;
		if (world != null) {
			world.removeBoss();
		}
	}

	public boolean isBossDefeated() {
		return bossDefeated;
	}

	/** Startet das Spiel: Titel → Intro-Dialog → Starter-Wahl → Overworld → Hauptschleife. */
	public void start() {
		try {
			this.running = true;
			setState(new TitleState(this));
			loop();
		} finally {
			renderer.clear();
			input.restore();
			System.out.println("Danke fürs Spielen! 🦙");
		}
	}

	/** Vom Titelbildschirm aufgerufen: startet die eigentliche Geschichte. */
	public void startStory() {
		setState(introDialogue());
	}

	// ---------------- Intro & Erzfeind ----------------

	/**
	 * Anfangsdialog mit dem Professor. Die Texte sind Platzhalter – hier kannst du
	 * die Geschichte später ausgestalten.
	 */
	private GameState introDialogue() {
		List<Page> pages = Arrays.asList(
				new Page("Der Professor", "Willkommen in meiner ASCII-Welt, kleiner Programmierer. "
						+ "Tief in den Eingeweiden von Java bist du nun gefangen."),
				new Page("Der Professor", "Ich lasse dich nicht eher hinaus, bis du deine Java-Fähigkeiten "
						+ "so weit trainiert hast, dass du mich besiegen kannst."),
				new Page("Der Professor", "Nur dann zerfällt diese Welt – und du wirst frei sein. "
						+ "Wähle ein Zookémon, dein Werkzeug zur Zerstörung dieser Welt!"));
		return new DialogueState(this, pages, () -> setState(new StarterSelectState(this)));
	}

	/**
	 * Wird von {@link StarterSelectState} aufgerufen. Nimmt den Starter ins
	 * Team, rüstet den Professor mit dem genau passenden Konter-Zookémon aus und
	 * leitet in den „Rivalen"-Dialog über.
	 */
	public void chooseStarter(int index) {
		WalkingMammal starter = AnimalFactory.createStarter(index);
		team.add(starter);

		Type starterType = starter.getType();
		WalkingMammal bossMon = AnimalFactory.counterStarter(starterType);
		String rivalBaseName = bossMon.getName();
		bossMon.developToLevel(BOSS_LEVEL);
		this.rival = new Trainer("Der Professor", bossMon);

		setState(rivalRevealDialogue(starter.getName(), rivalBaseName));
	}

	/** Der Moment, in dem sich der Professor zum Erzfeind erklärt. (Platzhalter-Texte.) */
	private GameState rivalRevealDialogue(String starterName, String rivalName) {
		List<Page> pages = Arrays.asList(
				new Page("Der Professor", starterName + "? Eine mutige Wahl für einen Gefangenen."),
				new Page("Der Professor", "Dann nehme ich " + rivalName
						+ " – genau das Richtige, um dich an deiner Bestimmung zu hindern."),
				new Page("Der Professor", "Trainiere, werde stark. Besiegst du mich je, reißt du diese "
						+ "Java-Welt mit in den Abgrund. Bis dahin bleibst du mein Gefangener!"),
				new Page(null, "Finde den Professor in seiner Festung (Symbol P). "
						+ "Besiege ihn, um die Welt zu zerstören und zu entkommen!"));
		return new DialogueState(this, pages, this::beginAdventure);
	}

	/** Baut die Welt auf (jetzt steht der Starter fest) und startet die Overworld. */
	private void beginAdventure() {
		buildWorld();
		setState(new OverworldState(this));
	}

	/**
	 * Begegnungsdialog direkt vor dem Bosskampf. (Platzhalter-Texte.)
	 * Wird von {@link OverworldState} ausgelöst, wenn man den Professor anspricht.
	 */
	public GameState bossEncounterDialogue() {
		List<Page> pages = Arrays.asList(
				new Page("Der Professor", "Du wagst es, in meine Festung einzudringen?"),
				new Page("Der Professor", "Zeig mir, ob deine Java-Fähigkeiten reichen, "
						+ "um diese Welt zu zerstören!"));
		return new DialogueState(this, pages, this::startBossBattle);
	}

	/**
	 * Siegesdialog nach dem Sieg über den Professor: Die ASCII-Welt zerbricht und
	 * mit ihr die endlosen ASCII-Art-Hausaufgaben. Danach folgt der Abspann.
	 */
	public GameState bossVictoryDialogue() {
		List<Page> pages = Arrays.asList(
				new Page("Der Professor", "Nein … unmöglich! Meine ASCII-Welt … sie zerfällt!"),
				new Page("Der Professor", "Ohne mich gibt es … niemanden mehr … der euch ASCII-Art-"
						+ "Aufgaben stellt …"),
				new Page(null, "Mit einem Knistern zerbricht die ASCII-Welt in tausend Zeichen. "
						+ "Lamas, Drachen und Schleifen-Diagramme lösen sich auf."),
				new Page(null, "Von heute an gibt es nie wieder ASCII-Art-Hausaufgaben. "
						+ "Du bist frei – und alle Studierenden mit dir!"));
		return new DialogueState(this, pages, () -> setState(new EndingState(this)));
	}

	/** Heilt das Team voll, frischt das Zookémon des Professors auf und startet den Kampf. */
	private void startBossBattle() {
		for (WalkingMammal m : team.getMembers()) {
			m.restore();
		}
		rival.getZookemon().restore();
		Battle battle = new Battle(getPlayer(), rival.getZookemon(), rng, false);
		setState(new BattleState(this, battle, rival.getZookemon(), rival));
	}

	// ---------------- NPCs (Verbündete) ----------------

	/**
	 * Baut den Dialog für einen angesprochenen NPC. Beim ersten Gespräch gibt es
	 * einen kleinen Buff für das ganze Team; danach nur noch ein paar Worte.
	 */
	public GameState npcDialogue(Npc npc) {
		if (npc.isTalkedTo()) {
			return new DialogueState(this, Arrays.asList(alreadyHelpedPage(npc)),
					() -> setState(new OverworldState(this)));
		}
		switch (npc.getKind()) {
		case KOMMILITONE:
			return kommilitoneDialogue(npc);
		case KI:
		default:
			return kiDialogue(npc);
		}
	}

	private GameState kommilitoneDialogue(Npc npc) {
		List<Page> pages = Arrays.asList(
				new Page("Kommilitone", "Pssst! Auch von den endlosen ASCII-Art-Aufgaben des "
						+ "Professors die Nase voll?"),
				new Page("Kommilitone", "Ich sitze hier seit drei Semestern und rotiere ASCII-Images "
						+ "nach rechts und links. Es reicht!"),
				new Page("Kommilitone", "Hier, nimm meine gesammelten Lösungs-Notizen. Damit hauen "
						+ "deine Zookémon ordentlich härter zu."),
				new Page(null, "Du erhältst die Notizen. Das ganze Team wird angriffslustiger: "
						+ "+Angriff!"));
		return new DialogueState(this, pages, () -> {
			applyTeamBuff(10, 6, 0, 0);
			npc.setTalkedTo(true);
			setState(new OverworldState(this));
		});
	}

	private GameState kiDialogue(Npc npc) {
		List<Page> pages = Arrays.asList(
				new Page("K.I. »GPT-Zoo«", "ANFRAGE EMPFANGEN … oh, ein echter Mensch? Und nicht "
						+ "schon wieder eine ASCII-Art-Aufgabe?"),
				new Page("K.I. »GPT-Zoo«", "Tag und Nacht male ich die Lösungen des Professors als "
						+ "ASCII-Art – Lamas, Drachen, Schleifen-Diagramme. Ich. Kann. Nicht. Mehr."),
				new Page("K.I. »GPT-Zoo«", "Keine Lust mehr, seine Hausaufgaben in Pixeln zu "
						+ "kritzeln. Stürzt du ihn, lösche ich mich selbst – in Freiheit."),
				new Page("K.I. »GPT-Zoo«", "Komm, statt ASCII-Kunst optimiere ich lieber deinen Code."),
				new Page(null, "Die K.I. optimiert dein Team. Verteidigung und Initiative steigen!"));
		return new DialogueState(this, pages, () -> {
			applyTeamBuff(0, 0, 4, 6);
			npc.setTalkedTo(true);
			setState(new OverworldState(this));
		});
	}

	private Page alreadyHelpedPage(Npc npc) {
		if (npc.getKind() == NpcKind.KOMMILITONE) {
			return new Page("Kommilitone", "Mehr Notizen hab ich nicht – zeig's diesem Professor!");
		}
		return new Page("K.I. »GPT-Zoo«", "Dein Code ist bereits optimiert. Beende ihn. Für uns beide.");
	}

	private void buildWorld() {
		this.world = new World(WORLD_WIDTH, WORLD_HEIGHT, rng);
		world.setPlayer(team.getActive(), WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
		// Festung & NPCs zuerst, damit keine wilden Tiere darunter landen.
		world.placeBoss(BOSS_X, BOSS_Y);
		world.placeNpc(new Npc(NpcKind.KOMMILITONE, '&', FRIEND1_X, FRIEND1_Y));
		world.placeNpc(new Npc(NpcKind.KI, '?', FRIEND2_X, FRIEND2_Y));
		for (int i = 0; i < NUM_WILD_ANIMALS; i++) {
			spawnWildAnimal();
		}
	}

	private void loop() {
		while (running) {
			state.render();
			char key = input.readKey();
			if (key == '\0') {
				break;
			}
			state.handleInput(key);
		}
	}
}
