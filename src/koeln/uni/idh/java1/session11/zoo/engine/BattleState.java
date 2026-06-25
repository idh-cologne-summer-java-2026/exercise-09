package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.ui.Renderer;

/**
 * Der Kampfbildschirm. Beim Betreten erscheint kurz das wilde Tier (Intro).
 * Der Spieler wählt per Zifferntaste eine Attacke oder flieht mit F. Die Runde
 * läuft anschließend Schritt für Schritt mit kurzen Pausen ab; getroffene Tiere
 * blitzen auf und die HP-Balken leeren sich sichtbar. Nach einem Sieg gibt es
 * einen Zusammenfassungs-Bildschirm, danach geht es zurück in die Overworld
 * (eine Niederlage beendet das Spiel).
 */
public class BattleState implements GameState {

	/** Pause, bevor der nächste Schritt (z. B. der Gegenangriff) erscheint. */
	private static final long STEP_PAUSE_MS = 550;
	/** Dauer des Treffer-Blitzes. */
	private static final long FLASH_MS = 160;
	/** Pause, nachdem sich ein HP-Balken geleert hat. */
	private static final long AFTER_HIT_PAUSE_MS = 300;
	/** Anzahl Frames für die HP-Balken-Animation. */
	private static final int HP_FRAMES = 8;
	private static final long HP_FRAME_MS = 45;
	/** Pause der Intro-Sequenz, in der das wilde Tier erscheint. */
	private static final long INTRO_MS = 900;

	private final Game game;
	private final Battle battle;
	private final WalkingMammal enemy;

	private boolean summaryShown = false;
	private int epGained = 0;

	public BattleState(Game game, Battle battle, WalkingMammal enemy) {
		this.game = game;
		this.battle = battle;
		this.enemy = enemy;
	}

	@Override
	public void onEnter() {
		game.getRenderer().renderBattleIntro(battle);
		pause(INTRO_MS);
	}

	@Override
	public void render() {
		if (battle.isOver() && summaryShown
				&& battle.getResult() == Battle.Result.SPIELER_GEWINNT) {
			game.getRenderer().renderVictory(battle.getPlayer(), enemy,
					epGained, game.getTotalEp(), game.getVictories());
		} else {
			game.getRenderer().renderBattle(battle);
		}
	}

	@Override
	public void handleInput(char key) {
		if (battle.isOver()) {
			advanceAfterBattle();
			return;
		}

		if (key == 'f') {
			battle.flee();
			playRound();
			return;
		}

		if (Character.isDigit(key)) {
			int index = key - '1';
			if (index >= 0 && index < battle.getPlayer().getMoves().size()) {
				battle.playerSelectsMove(index);
				playRound();
			}
		}
	}

	/**
	 * Spielt die geplanten Schritte der Runde nacheinander ab: Aktionstext
	 * zeigen, kurz warten, getroffenes Tier aufblitzen lassen, HP-Balken
	 * animiert leeren, kurz warten.
	 */
	private void playRound() {
		Renderer renderer = game.getRenderer();
		Battler player = battle.getPlayer();

		while (battle.hasPendingSteps()) {
			int oldPlayerHp = player.getCurrentHp();
			int oldEnemyHp = enemy.getCurrentHp();

			battle.step();

			int newPlayerHp = player.getCurrentHp();
			int newEnemyHp = enemy.getCurrentHp();

			// Erst den Aktionstext mit noch unverändertem HP-Stand zeigen.
			player.setCurrentHp(oldPlayerHp);
			enemy.setCurrentHp(oldEnemyHp);
			renderer.renderBattleStep(battle, null);
			pause(STEP_PAUSE_MS);

			Battler hit = whoWasHit(oldPlayerHp, newPlayerHp, oldEnemyHp, newEnemyHp);
			if (hit != null) {
				// Treffer-Blitz auf dem getroffenen Tier.
				renderer.renderBattleStep(battle, hit);
				pause(FLASH_MS);
				// HP-Balken sichtbar leeren.
				animateHp(player, oldPlayerHp, newPlayerHp, oldEnemyHp, newEnemyHp);
				pause(AFTER_HIT_PAUSE_MS);
			}

			// Sicherstellen, dass am Ende der exakte Endstand steht.
			player.setCurrentHp(newPlayerHp);
			enemy.setCurrentHp(newEnemyHp);
		}
	}

	private Battler whoWasHit(int oldPlayerHp, int newPlayerHp, int oldEnemyHp, int newEnemyHp) {
		if (newEnemyHp < oldEnemyHp) {
			return enemy;
		}
		if (newPlayerHp < oldPlayerHp) {
			return battle.getPlayer();
		}
		return null;
	}

	private void animateHp(Battler player, int oldPlayerHp, int newPlayerHp,
			int oldEnemyHp, int newEnemyHp) {
		Renderer renderer = game.getRenderer();
		for (int frame = 1; frame <= HP_FRAMES; frame++) {
			player.setCurrentHp(lerp(oldPlayerHp, newPlayerHp, frame));
			enemy.setCurrentHp(lerp(oldEnemyHp, newEnemyHp, frame));
			renderer.renderBattleStep(battle, null);
			pause(HP_FRAME_MS);
		}
	}

	/** Lineare Interpolation zwischen Start- und Zielwert über HP_FRAMES Frames. */
	private int lerp(int from, int to, int frame) {
		return from + (to - from) * frame / HP_FRAMES;
	}

	private void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Tastendruck nach Kampfende: Bei einem Sieg zuerst der Sieg-Bildschirm,
	 * danach (oder bei Flucht/Niederlage sofort) der eigentliche Abschluss.
	 */
	private void advanceAfterBattle() {
		if (battle.getResult() == Battle.Result.SPIELER_GEWINNT && !summaryShown) {
			epGained = experienceFor(enemy);
			game.registerVictory(epGained);
			summaryShown = true;
			return; // Sieg-Bildschirm wird beim nächsten render() gezeigt
		}
		resolveAndLeave();
	}

	/** Erfahrung für einen Sieg, grob abhängig von Größe und Level des Gegners. */
	private int experienceFor(WalkingMammal foe) {
		return 10 + foe.getMaxHp() / 2 + foe.getLevel() * 2;
	}

	private void resolveAndLeave() {
		WalkingMammal player = game.getPlayer();
		player.clearBattleModifiers();

		switch (battle.getResult()) {
		case SPIELER_GEWINNT:
			// Besiegtes Tier verschwindet, ein neues wildes Tier erscheint.
			game.getWorld().removeWildAnimal(enemy);
			game.spawnWildAnimal();
			game.setState(new OverworldState(game));
			break;
		case GEFLOHEN:
			game.setState(new OverworldState(game));
			break;
		case SPIELER_VERLIERT:
		default:
			game.quit();
			break;
		}
	}
}
