package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.ui.Renderer;
import koeln.uni.idh.java1.session11.zoo.ui.Sound;

/**
 * Der Titelbildschirm: Beim Betreten fliegt das große „ZOOKEMON"-Logo animiert
 * herein (Farbverlauf + Einschub von links), danach wartet das Spiel auf einen
 * Tastendruck und startet die Geschichte.
 */
public class TitleState implements GameState {

	private static final long FRAME_MS = 130;

	private final Game game;

	public TitleState(Game game) {
		this.game = game;
	}

	@Override
	public void onEnter() {
		Renderer renderer = game.getRenderer();
		// Einblende-Animation: Logo fliegt herein und färbt sich kräftiger.
		for (int frame = 0; frame <= Renderer.TITLE_FRAMES; frame++) {
			renderer.renderTitle(frame, false);
			pause(FRAME_MS);
		}
		Sound.bossRoar();
	}

	@Override
	public void render() {
		game.getRenderer().renderTitle(Renderer.TITLE_FRAMES, true);
	}

	@Override
	public void handleInput(char key) {
		Sound.select();
		game.startStory();
	}

	private void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
