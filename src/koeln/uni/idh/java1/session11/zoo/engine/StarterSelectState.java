package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.AnimalFactory;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

/**
 * Die Starter-Auswahl als eigener Spielzustand (statt einer blockierenden
 * Schleife): Der Spieler wählt per Zifferntaste eines der drei Starter-Zookémon.
 * Danach übernimmt {@link Game} (Rivalen festlegen und weiter ins Intro).
 */
public class StarterSelectState implements GameState {

	private final Game game;
	private final WalkingMammal[] options = AnimalFactory.starters();

	public StarterSelectState(Game game) {
		this.game = game;
	}

	@Override
	public void render() {
		game.getRenderer().renderStarterSelect(options);
	}

	@Override
	public void handleInput(char key) {
		if (!Character.isDigit(key)) {
			return;
		}
		int index = key - '1';
		if (index >= 0 && index < options.length) {
			game.chooseStarter(index);
		}
	}
}
