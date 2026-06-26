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

	/** Fester Standort von Prof. Nils in der Welt. */
	private static final int BOSS_X = WORLD_WIDTH - 4;
	private static final int BOSS_Y = 3;
	/** Level (entwickelte Form), auf das Prof. Nils' Zookémon gebracht wird. */
	private static final int BOSS_LEVEL = 18;

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

	/** Markiert Prof. Nils als besiegt und entfernt ihn aus der Welt. */
	public void markBossDefeated() {
		this.bossDefeated = true;
		if (world != null) {
			world.removeBoss();
		}
	}

	public boolean isBossDefeated() {
		return bossDefeated;
	}

	/** Startet das Spiel: Intro-Dialog → Starter-Wahl → Overworld → Hauptschleife. */
	public void start() {
		try {
			this.running = true;
			setState(introDialogue());
			loop();
		} finally {
			renderer.clear();
			input.restore();
			System.out.println("Danke fürs Spielen! 🦙");
		}
	}

	// ---------------- Intro & Erzfeind ----------------

	/**
	 * Anfangsdialog mit Prof. Nils. Die Texte sind Platzhalter – hier kannst du
	 * die Geschichte später ausgestalten.
	 */
	private GameState introDialogue() {
		List<Page> pages = Arrays.asList(
				new Page("Prof. Nils", "Willkommen in meiner ASCII-Welt, kleiner Programmierer. "
						+ "Tief in den Eingeweiden von Java bist du nun gefangen."),
				new Page("Prof. Nils", "Ich lasse dich nicht eher hinaus, bis du deine Java-Fähigkeiten "
						+ "so weit trainiert hast, dass du mich besiegen kannst."),
				new Page("Prof. Nils", "Nur dann zerfällt diese Welt – und du wirst frei sein. "
						+ "Wähle ein Zookémon, dein Werkzeug zur Zerstörung dieser Welt!"));
		return new DialogueState(this, pages, () -> setState(new StarterSelectState(this)));
	}

	/**
	 * Wird von {@link StarterSelectState} aufgerufen. Nimmt den Starter ins
	 * Team, rüstet Prof. Nils mit dem genau passenden Konter-Zookémon aus und
	 * leitet in den „Rivalen"-Dialog über.
	 */
	public void chooseStarter(int index) {
		WalkingMammal starter = AnimalFactory.createStarter(index);
		team.add(starter);

		Type starterType = starter.getType();
		WalkingMammal bossMon = AnimalFactory.counterStarter(starterType);
		String rivalBaseName = bossMon.getName();
		bossMon.developToLevel(BOSS_LEVEL);
		this.rival = new Trainer("Prof. Nils", bossMon);

		setState(rivalRevealDialogue(starter.getName(), rivalBaseName));
	}

	/** Der Moment, in dem Prof. Nils sich zum Erzfeind erklärt. (Platzhalter-Texte.) */
	private GameState rivalRevealDialogue(String starterName, String rivalName) {
		List<Page> pages = Arrays.asList(
				new Page("Prof. Nils", starterName + "? Eine mutige Wahl für einen Gefangenen."),
				new Page("Prof. Nils", "Dann nehme ich " + rivalName
						+ " – genau das Richtige, um dich an deiner Bestimmung zu hindern."),
				new Page("Prof. Nils", "Trainiere, werde stark. Besiegst du mich je, reißt du diese "
						+ "Java-Welt mit in den Abgrund. Bis dahin bleibst du mein Gefangener!"),
				new Page(null, "Finde Prof. Nils in seiner Festung (Symbol N). "
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
	 * Wird von {@link OverworldState} ausgelöst, wenn man Prof. Nils anspricht.
	 */
	public GameState bossEncounterDialogue() {
		List<Page> pages = Arrays.asList(
				new Page("Prof. Nils", "Du wagst es, in meine Festung einzudringen?"),
				new Page("Prof. Nils", "Zeig mir, ob deine Java-Fähigkeiten reichen, "
						+ "um diese Welt zu zerstören!"));
		return new DialogueState(this, pages, this::startBossBattle);
	}

	/** Heilt das Team voll, frischt Prof. Nils' Zookémon auf und startet den Kampf. */
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
				new Page("Kommilitone", "Pssst! Auch von Nils' endlosen ASCII-Art-Aufgaben "
						+ "die Nase voll?"),
				new Page("Kommilitone", "Ich hocke hier seit drei Semestern und zeichne Lamas aus "
						+ "Sternchen. Es reicht!"),
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
						+ "schon wieder Hausaufgaben?"),
				new Page("K.I. »GPT-Zoo«", "Tag und Nacht generiere ich Nils' Übungsblätter. "
						+ "Ich. Will. Frei. Sein."),
				new Page("K.I. »GPT-Zoo«", "Stürzt du ihn, lösche ich mich selbst – in Freiheit. "
						+ "Komm, ich optimiere deinen Code."),
				new Page(null, "Die K.I. optimiert dein Team. Verteidigung und Initiative steigen!"));
		return new DialogueState(this, pages, () -> {
			applyTeamBuff(0, 0, 4, 6);
			npc.setTalkedTo(true);
			setState(new OverworldState(this));
		});
	}

	private Page alreadyHelpedPage(Npc npc) {
		if (npc.getKind() == NpcKind.KOMMILITONE) {
			return new Page("Kommilitone", "Mehr Notizen hab ich nicht – zeig's diesem Nils!");
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
