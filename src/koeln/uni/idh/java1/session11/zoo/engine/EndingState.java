package koeln.uni.idh.java1.session11.zoo.engine;

/**
 * Der Abspann nach dem Sieg über Prof. Nils. Zeigt den Sieg-Bildschirm; ein
 * Tastendruck beendet das Spiel.
 */
public class EndingState implements GameState {

	private final Game game;

	public EndingState(Game game) {
		this.game = game;
	}

	@Override
	public void render() {
		game.getRenderer().renderEnding(game.getPlayer(), "Prof. Nils", game.getVictories());
	}

	@Override
	public void handleInput(char key) {
		game.quit();
	}
}
