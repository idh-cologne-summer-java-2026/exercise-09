package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.battle.Battle;
import koeln.uni.idh.java1.session11.zoo.world.Player;
import koeln.uni.idh.java1.session11.zoo.world.Tile;
import koeln.uni.idh.java1.session11.zoo.world.World;

/**
 * Die begehbare Spielwelt. Der Spieler bewegt sein Tier mit WASD, wilde Tiere
 * ziehen mit jedem Schritt zufällig weiter. Läuft der Spieler auf ein wildes
 * Tier, beginnt ein Kampf. Wasserfelder heilen ein wenig.
 */
public class OverworldState implements GameState {

	private final Game game;
	private final World world;

	public OverworldState(Game game) {
		this.game = game;
		this.world = game.getWorld();
	}

	@Override
	public void render() {
		game.getRenderer().renderWorld(world);
	}

	@Override
	public void handleInput(char key) {
		int direction;
		switch (key) {
		case 'w':
			direction = Player.OBEN;
			break;
		case 's':
			direction = Player.UNTEN;
			break;
		case 'a':
			direction = Player.LINKS;
			break;
		case 'd':
			direction = Player.RECHTS;
			break;
		case 'q':
			game.quit();
			return;
		default:
			return; // andere Tasten ignorieren
		}

		boolean moved = world.movePlayer(direction);
		if (moved) {
			healOnWater();
			world.tickWildAnimals();
		}

		WalkingMammal encounter = world.encounterAt(
				world.getPlayer().getX(), world.getPlayer().getY());
		if (encounter != null) {
			startBattle(encounter);
		}
	}

	private void healOnWater() {
		WalkingMammal player = world.getPlayer();
		if (world.getTile(player.getX(), player.getY()) == Tile.WASSER) {
			int heal = Math.max(1, player.getMaxHp() / 16);
			player.setCurrentHp(player.getCurrentHp() + heal);
		}
	}

	private void startBattle(WalkingMammal enemy) {
		Battle battle = new Battle(world.getPlayer(), enemy, game.getRng());
		game.setState(new BattleState(game, battle, enemy));
	}
}
