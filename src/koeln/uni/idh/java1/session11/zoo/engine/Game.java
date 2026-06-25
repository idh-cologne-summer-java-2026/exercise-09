package koeln.uni.idh.java1.session11.zoo.engine;

import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.animals.AnimalFactory;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.Input;
import koeln.uni.idh.java1.session11.zoo.ui.Renderer;
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

	private final Random rng = new Random();
	private final Input input;
	private final Renderer renderer;

	private World world;
	private WalkingMammal player;
	private GameState state;
	private boolean running = false;

	public Game() {
		this.input = new Input();
		this.renderer = new Renderer(true);
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

	public WalkingMammal getPlayer() {
		return player;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public void quit() {
		this.running = false;
	}

	/** Spawnt ein neues zufälliges wildes Tier an einer freien Stelle. */
	public void spawnWildAnimal() {
		int[] pos = world.randomFreeTile();
		world.addWildAnimal(AnimalFactory.randomWild(rng), pos[0], pos[1]);
	}

	/** Startet das Spiel: Auswahl, Welt-Aufbau, Hauptschleife. */
	public void start() {
		try {
			chooseStarter();
			buildWorld();
			this.state = new OverworldState(this);
			this.running = true;
			loop();
		} finally {
			renderer.clear();
			input.restore();
			System.out.println("Danke fürs Spielen! 🦙");
		}
	}

	private void chooseStarter() {
		WalkingMammal[] options = AnimalFactory.oneOfEach();
		StringBuilder sb = new StringBuilder();
		sb.append("[2J[H");
		sb.append("[1m🦙⚔️  Willkommen bei Zookémon![0m\n\n");
		sb.append("Wähle dein Tier:\n\n");
		for (int i = 0; i < options.length; i++) {
			WalkingMammal a = options[i];
			sb.append("  ").append(i + 1).append(") ")
					.append(a.getName())
					.append("  [90m(").append(a.getType().getDisplayName())
					.append(", ").append(a.getMaxHp()).append(" HP)[0m\n");
		}
		sb.append("\nDeine Wahl (1-").append(options.length).append("): ");
		System.out.print(sb);
		System.out.flush();

		int choice = -1;
		while (choice < 0 || choice >= options.length) {
			char key = input.readKey();
			if (key == '\0') {
				choice = 0;
				break;
			}
			if (Character.isDigit(key)) {
				int n = key - '1';
				if (n >= 0 && n < options.length) {
					choice = n;
				}
			}
		}
		this.player = AnimalFactory.create(choice);
	}

	private void buildWorld() {
		this.world = new World(WORLD_WIDTH, WORLD_HEIGHT, rng);
		world.setPlayer(player, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
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
