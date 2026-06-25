package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;

/**
 * Der Kampfbildschirm. Der Spieler wählt per Zifferntaste eine Attacke oder
 * flieht mit F. Ist der Kampf vorbei, führt ein beliebiger Tastendruck zurück
 * in die Overworld (oder beendet das Spiel bei einer Niederlage).
 */
public class BattleState implements GameState {

	private final Game game;
	private final Battle battle;
	private final WalkingMammal enemy;

	public BattleState(Game game, Battle battle, WalkingMammal enemy) {
		this.game = game;
		this.battle = battle;
		this.enemy = enemy;
	}

	@Override
	public void render() {
		game.getRenderer().renderBattle(battle);
	}

	@Override
	public void handleInput(char key) {
		if (battle.isOver()) {
			resolveAndLeave();
			return;
		}

		if (key == 'f') {
			battle.flee();
			return;
		}

		if (Character.isDigit(key)) {
			int index = key - '1';
			if (index >= 0 && index < battle.getPlayer().getMoves().size()) {
				battle.playerSelectsMove(index);
			}
		}
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
