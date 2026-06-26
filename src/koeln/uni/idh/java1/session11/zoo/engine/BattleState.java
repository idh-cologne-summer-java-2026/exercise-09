package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.battle.LevelUpResult;
import koeln.uni.idh.java1.session11.zoo.battle.StepResult;
import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.ui.BattleFx;
import koeln.uni.idh.java1.session11.zoo.ui.Renderer;

/**
 * Der Kampfbildschirm. Beim Betreten erscheint kurz das wilde Tier (Intro).
 * Der Spieler wählt per Zifferntaste eine Attacke oder flieht mit F. Die Runde
 * läuft Schritt für Schritt ab: ein Projektil in der Farbe des Attacken-Typs
 * fliegt zum Ziel, das getroffene Tier blitzt auf, der Bildschirm wackelt und
 * der HP-Balken leert sich. Nach einem Sieg gibt es Erfahrung (mit möglichem
 * Level-Aufstieg und Entwicklungs-Animation) und einen Sieg-Bildschirm.
 */
public class BattleState implements GameState {

	private static final long STEP_PAUSE_MS = 500;
	private static final long FLASH_MS = 70;
	private static final long AFTER_HIT_PAUSE_MS = 250;
	private static final int HP_FRAMES = 8;
	private static final long HP_FRAME_MS = 45;
	private static final long INTRO_MS = 900;

	/** Muss zur PROJECTILE_WIDTH des Renderers passen. */
	private static final int PROJECTILE_WIDTH = 38;
	private static final int PROJECTILE_FRAMES = 9;
	private static final long PROJECTILE_FRAME_MS = 30;

	private final Game game;
	private final Battle battle;
	private final WalkingMammal enemy;

	private boolean summaryShown = false;
	private int epGained = 0;
	private LevelUpResult levelResult = null;

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
		if (summaryShown && battle.getResult() == Battle.Result.SPIELER_GEWINNT) {
			game.getRenderer().renderVictory(battle.getPlayer(), enemy,
					epGained, game.getTotalEp(), game.getVictories(), levelResult);
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

	/** Spielt die geplanten Schritte der Runde mit allen Animationen ab. */
	private void playRound() {
		Renderer renderer = game.getRenderer();
		Battler player = battle.getPlayer();

		while (battle.hasPendingSteps()) {
			int oldPlayerHp = player.getCurrentHp();
			int oldEnemyHp = enemy.getCurrentHp();

			StepResult step = battle.step();

			int newPlayerHp = player.getCurrentHp();
			int newEnemyHp = enemy.getCurrentHp();

			// Aktionstext mit noch unverändertem HP-Stand zeigen.
			player.setCurrentHp(oldPlayerHp);
			enemy.setCurrentHp(oldEnemyHp);
			renderer.renderBattleFx(battle, null);
			pause(STEP_PAUSE_MS);

			// Projektil fliegen lassen (nur bei Schadens-Attacken).
			if (step != null && step.getKind() == StepResult.Kind.MOVE
					&& step.getMove().getPower() > 0) {
				animateProjectile(step.getAttacker(), step.getMove().getType());
			}

			Battler hit = whoWasHit(oldPlayerHp, newPlayerHp, oldEnemyHp, newEnemyHp);
			if (hit != null) {
				flashImpact(hit);
				animateHp(player, oldPlayerHp, newPlayerHp, oldEnemyHp, newEnemyHp);
				pause(AFTER_HIT_PAUSE_MS);
			}

			// Exakten Endstand sicherstellen.
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

	/** Lässt ein Projektil vom Angreifer zum Ziel über den Bildschirm fliegen. */
	private void animateProjectile(Battler attacker, Type type) {
		Renderer renderer = game.getRenderer();
		boolean rightward = attacker == battle.getPlayer();
		for (int i = 0; i <= PROJECTILE_FRAMES; i++) {
			BattleFx fx = new BattleFx();
			fx.projectileType = type;
			fx.projectileColumn = rightward
					? i * PROJECTILE_WIDTH / PROJECTILE_FRAMES
					: PROJECTILE_WIDTH - i * PROJECTILE_WIDTH / PROJECTILE_FRAMES;
			renderer.renderBattleFx(battle, fx);
			pause(PROJECTILE_FRAME_MS);
		}
	}

	/** Treffer-Blitz mit kurzem Bildschirm-Wackeln auf dem getroffenen Tier. */
	private void flashImpact(Battler hit) {
		Renderer renderer = game.getRenderer();
		for (int k = 0; k < 3; k++) {
			BattleFx fx = new BattleFx();
			fx.flash = hit;
			fx.shake = (k % 2 == 0) ? 1 : 0;
			renderer.renderBattleFx(battle, fx);
			pause(FLASH_MS);
		}
	}

	private void animateHp(Battler player, int oldPlayerHp, int newPlayerHp,
			int oldEnemyHp, int newEnemyHp) {
		Renderer renderer = game.getRenderer();
		for (int frame = 1; frame <= HP_FRAMES; frame++) {
			player.setCurrentHp(lerp(oldPlayerHp, newPlayerHp, frame));
			enemy.setCurrentHp(lerp(oldEnemyHp, newEnemyHp, frame));
			renderer.renderBattleFx(battle, null);
			pause(HP_FRAME_MS);
		}
	}

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
	 * Tastendruck nach Kampfende: Bei einem Sieg zuerst Erfahrung gutschreiben
	 * (evtl. mit Entwicklungs-Animation) und den Sieg-Bildschirm zeigen, beim
	 * zweiten Druck (oder bei Flucht/Niederlage sofort) abschließen.
	 */
	private void advanceAfterBattle() {
		if (battle.getResult() == Battle.Result.SPIELER_GEWINNT && !summaryShown) {
			epGained = experienceFor(enemy);
			game.registerVictory(epGained);
			levelResult = game.getPlayer().gainExperience(epGained);
			if (levelResult.isEvolved()) {
				playEvolution(game.getPlayer(), levelResult.getOldName(), levelResult.getNewName());
			}
			summaryShown = true;
			return; // Sieg-Bildschirm folgt beim nächsten render()
		}
		resolveAndLeave();
	}

	/** Erfahrung für einen Sieg, grob abhängig von Größe und Level des Gegners. */
	private int experienceFor(WalkingMammal foe) {
		return 10 + foe.getMaxHp() / 2 + foe.getLevel() * 2;
	}

	/** Entwicklungs-Animation: das Tier flackert zwischen Bild und Silhouette. */
	private void playEvolution(WalkingMammal animal, String oldName, String newName) {
		Renderer renderer = game.getRenderer();
		long[] delays = { 320, 320, 240, 240, 160, 160, 110, 110, 70, 70 };
		boolean silhouette = true;
		for (long delay : delays) {
			renderer.renderEvolutionFrame(animal.getSymbol(),
					oldName + " entwickelt sich", silhouette);
			pause(delay);
			silhouette = !silhouette;
		}
		renderer.renderEvolutionFrame(animal.getSymbol(),
				oldName + " wurde zu " + newName, false);
		pause(1600);
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
