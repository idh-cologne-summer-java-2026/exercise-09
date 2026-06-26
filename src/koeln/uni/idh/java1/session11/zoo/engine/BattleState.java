package koeln.uni.idh.java1.session11.zoo.engine;

import java.util.List;

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

	private static final long STEP_PAUSE_MS = 320;
	private static final long FLASH_MS = 55;
	private static final long AFTER_HIT_PAUSE_MS = 150;
	private static final int HP_FRAMES = 6;
	private static final long HP_FRAME_MS = 28;
	private static final long INTRO_MS = 650;

	/** Muss zur PROJECTILE_WIDTH des Renderers passen. */
	private static final int PROJECTILE_WIDTH = 38;
	private static final int PROJECTILE_FRAMES = 6;
	private static final long PROJECTILE_FRAME_MS = 24;

	private final Game game;
	private final Battle battle;
	private final Team team;
	private final WalkingMammal enemy;
	/** Gesetzt bei einem Trainerkampf (z. B. Prof. Nils); null im Wildkampf. */
	private final Trainer trainer;

	private boolean summaryShown = false;
	private int epGained = 0;
	private LevelUpResult levelResult = null;

	/** Ob gerade der Team-Auswahlbildschirm (Tierwechsel) gezeigt wird. */
	private boolean choosingSwitch = false;
	/** Bei einem erzwungenen Wechsel (aktives Tier besiegt) ist Abbrechen tabu. */
	private boolean switchForced = false;

	/** Wildkampf. */
	public BattleState(Game game, Battle battle, WalkingMammal enemy) {
		this(game, battle, enemy, null);
	}

	/** Trainerkampf, wenn {@code trainer} gesetzt ist – sonst Wildkampf. */
	public BattleState(Game game, Battle battle, WalkingMammal enemy, Trainer trainer) {
		this.game = game;
		this.battle = battle;
		this.team = game.getTeam();
		this.enemy = enemy;
		this.trainer = trainer;
	}

	private boolean isTrainerBattle() {
		return trainer != null;
	}

	@Override
	public void onEnter() {
		game.getRenderer().renderBattleIntro(battle);
		pause(INTRO_MS);
	}

	@Override
	public void render() {
		if (choosingSwitch) {
			game.getRenderer().renderTeamMenu(team.getMembers(), team.getActiveIndex(), switchForced);
		} else if (summaryShown && battle.getResult() == Battle.Result.SPIELER_GEWINNT) {
			game.getRenderer().renderVictory(battle.getPlayer(), enemy,
					epGained, game.getTotalEp(), game.getVictories(), levelResult);
		} else {
			// Im Trainerkampf gibt es weder Fangen noch Fliehen.
			game.getRenderer().renderBattle(battle, !isTrainerBattle(), !isTrainerBattle());
		}
	}

	@Override
	public void handleInput(char key) {
		if (choosingSwitch) {
			handleSwitchSelection(key);
			return;
		}
		if (battle.isOver()) {
			advanceAfterBattle();
			return;
		}

		switch (key) {
		case 'f':
			if (isTrainerBattle()) {
				battle.getLog().add("Vor " + trainer.getName() + " kann man nicht fliehen!");
			} else {
				battle.flee();
				runRound();
			}
			return;
		case 'z':
			if (isTrainerBattle()) {
				battle.getLog().add("Ein fremdes Zookémon kann man nicht fangen!");
			} else {
				tryCatch();
			}
			return;
		case 'w':
			openSwitchMenu(false);
			return;
		default:
			break;
		}

		if (Character.isDigit(key)) {
			int index = key - '1';
			if (index >= 0 && index < battle.getPlayer().getMoves().size()) {
				battle.playerSelectsMove(index);
				runRound();
			}
		}
	}

	/**
	 * Spielt die geplante Runde ab. Wird das aktive Tier dabei besiegt und ist
	 * noch ein anderes kampffähig, muss der Spieler wechseln (statt zu verlieren).
	 */
	private void runRound() {
		playRound();
		if (battle.getResult() == Battle.Result.SPIELER_VERLIERT && team.hasOtherAlive()) {
			openSwitchMenu(true);
		}
	}

	/** Fangversuch, sofern noch Platz im Team ist. */
	private void tryCatch() {
		if (team.isFull()) {
			battle.getLog().add("Dein Team ist voll – du kannst nichts fangen!");
			return;
		}
		battle.attemptCatch();
		runRound();
	}

	/** Öffnet den Team-Auswahlbildschirm (freiwillig oder erzwungen). */
	private void openSwitchMenu(boolean forced) {
		if (!forced && !team.hasOtherAlive()) {
			battle.getLog().add("Du hast kein weiteres kampffähiges Tier!");
			return;
		}
		choosingSwitch = true;
		switchForced = forced;
	}

	/** Verarbeitet die Tastatureingabe im Team-Auswahlbildschirm. */
	private void handleSwitchSelection(char key) {
		// Abbrechen ist nur beim freiwilligen Wechsel erlaubt.
		if (!switchForced && (key == 'w' || key == 'f')) {
			choosingSwitch = false;
			return;
		}
		if (!Character.isDigit(key)) {
			return;
		}
		int index = key - '1';
		List<WalkingMammal> members = team.getMembers();
		if (index < 0 || index >= members.size()) {
			return;
		}
		if (index == team.getActiveIndex() || members.get(index).isFainted()) {
			return; // bereits aktiv oder besiegt – nicht wählbar
		}

		choosingSwitch = false;
		if (switchForced) {
			// Erzwungen: das neue Tier tritt an, der Kampf läuft normal weiter.
			game.setActiveAnimal(index);
			battle.continueWithNewPlayer(game.getPlayer());
		} else {
			// Freiwillig: kostet den Zug, das wilde Tier greift einmal an.
			game.setActiveAnimal(index);
			battle.switchPlayer(game.getPlayer());
			runRound();
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
		// Trainerkampf: kein EP-Bildschirm, direkt auflösen (Sieg → Abspann).
		if (isTrainerBattle()) {
			resolveAndLeave();
			return;
		}
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

	/**
	 * Erfahrung für einen Sieg – bewusst großzügig, damit man zügig zum Bosskampf
	 * kommt: ein Sieg bringt rund 5 Level. Die EP-Kosten für fünf Aufstiege ab
	 * Level L betragen 20·(L + L+1 + … + L+4) = 100·L + 200; genau so viel geben
	 * wir (am Level des Gegners bemessen, das etwa unserem entspricht). Stärkere
	 * (Elite-)Gegner bringen dadurch automatisch noch etwas mehr.
	 */
	private int experienceFor(WalkingMammal foe) {
		return 200 + foe.getLevel() * 100;
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
			if (isTrainerBattle()) {
				// Erzfeind besiegt: Spiel gewonnen → Abspann.
				game.markBossDefeated();
				game.setState(new EndingState(game));
			} else {
				// Besiegtes Tier verschwindet, ein neues wildes Tier erscheint.
				game.getWorld().removeWildAnimal(enemy);
				game.spawnWildAnimal();
				game.setState(new OverworldState(game));
			}
			break;
		case GEFANGEN:
			// Gefangenes Tier kommt geheilt ins Team, ein neues erscheint.
			enemy.restore();
			game.addCaughtAnimal(enemy);
			game.getWorld().removeWildAnimal(enemy);
			game.spawnWildAnimal();
			game.setState(new OverworldState(game));
			break;
		case GEFLOHEN:
			game.setState(new OverworldState(game));
			break;
		case SPIELER_VERLIERT:
		default:
			// Niederlage: Team voll heilen und zurück in die Overworld, um
			// stärker zu werden und es erneut zu versuchen (auch gegen Nils).
			for (WalkingMammal m : team.getMembers()) {
				m.restore();
			}
			game.setState(new OverworldState(game));
			break;
		}
	}
}
