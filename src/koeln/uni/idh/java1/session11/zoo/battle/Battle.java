package koeln.uni.idh.java1.session11.zoo.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Die rundenbasierte Kampf-Engine. Sie ist bewusst UI-frei: sie kennt nur
 * zwei {@link Battler} und schreibt alles Geschehene in ein Log, das die
 * Darstellung anschließend ausliest.
 */
public class Battle {

	public enum Result {
		LAUFEND, SPIELER_GEWINNT, SPIELER_VERLIERT, GEFLOHEN
	}

	private final Battler player;
	private final Battler enemy;
	private final DamageCalculator calculator;
	private final Random rng;
	private final List<String> log = new ArrayList<>();
	private Result result = Result.LAUFEND;

	public Battle(Battler player, Battler enemy) {
		this(player, enemy, new Random());
	}

	public Battle(Battler player, Battler enemy, Random rng) {
		this.player = player;
		this.enemy = enemy;
		this.rng = rng;
		this.calculator = new DamageCalculator(rng);
		log.add("Ein wildes " + enemy.getName() + " erscheint!");
	}

	public Battler getPlayer() {
		return player;
	}

	public Battler getEnemy() {
		return enemy;
	}

	public List<String> getLog() {
		return log;
	}

	public Result getResult() {
		return result;
	}

	public boolean isOver() {
		return result != Result.LAUFEND;
	}

	/**
	 * Führt eine komplette Runde aus: Der Spieler setzt die gewählte Attacke
	 * ein, das wilde Tier eine zufällige. Wer zuerst handelt, entscheidet die
	 * Initiative (bei Gleichstand der Zufall). Am Rundenende wirken
	 * Statuseffekte.
	 *
	 * @param moveIndex Index der vom Spieler gewählten Attacke
	 */
	public void playerSelectsMove(int moveIndex) {
		if (isOver()) {
			return;
		}
		Move playerMove = player.getMoves().get(moveIndex);
		Move enemyMove = enemy.getMoves().get(rng.nextInt(enemy.getMoves().size()));

		boolean playerFirst = decidePlayerFirst();
		if (playerFirst) {
			executeMove(player, enemy, playerMove);
			if (!checkFainted()) {
				executeMove(enemy, player, enemyMove);
			}
		} else {
			executeMove(enemy, player, enemyMove);
			if (!checkFainted()) {
				executeMove(player, enemy, playerMove);
			}
		}

		if (!checkFainted()) {
			applyEndOfTurn();
			checkFainted();
		}
	}

	/**
	 * Fluchtversuch. Ist der Spieler schneller, gelingt er sicher, sonst mit
	 * 50%. Scheitert er, greift das wilde Tier einmal an.
	 */
	public void flee() {
		if (isOver()) {
			return;
		}
		boolean success = player.getStats().getSpeed() >= enemy.getStats().getSpeed()
				|| rng.nextBoolean();
		if (success) {
			log.add(player.getName() + " flieht aus dem Kampf!");
			result = Result.GEFLOHEN;
		} else {
			log.add("Flucht gescheitert!");
			Move enemyMove = enemy.getMoves().get(rng.nextInt(enemy.getMoves().size()));
			executeMove(enemy, player, enemyMove);
			checkFainted();
		}
	}

	private boolean decidePlayerFirst() {
		int ps = player.getStats().getSpeed();
		int es = enemy.getStats().getSpeed();
		if (ps != es) {
			return ps > es;
		}
		return rng.nextBoolean();
	}

	private void executeMove(Battler attacker, Battler defender, Move move) {
		log.add(attacker.getName() + " setzt " + move.getName() + " ein!");

		// Genauigkeits-Wurf
		if (rng.nextInt(100) >= move.getAccuracy()) {
			log.add("… die Attacke geht daneben!");
			return;
		}

		// Schaden (nur falls die Attacke Stärke hat)
		if (move.getPower() > 0) {
			int damage = calculator.calculate(attacker, defender, move);
			defender.setCurrentHp(Math.max(0, defender.getCurrentHp() - damage));
			log.add(defender.getName() + " erleidet " + damage + " Schaden.");
			double eff = TypeChart.effectiveness(move.getType(), defender.getType());
			if (eff > 1.0) {
				log.add("Sehr effektiv!");
			} else if (eff < 1.0) {
				log.add("Nicht sehr effektiv …");
			}
		}

		applyEffect(move, defender);
	}

	private void applyEffect(Move move, Battler defender) {
		if (move.getEffect() == MoveEffect.KEINER) {
			return;
		}
		if (rng.nextInt(100) >= move.getEffectChance()) {
			return;
		}
		switch (move.getEffect()) {
		case VERGIFTEN:
			if (defender.getStatus() == Status.KEINER) {
				defender.setStatus(Status.VERGIFTET);
				log.add(defender.getName() + " wurde vergiftet!");
			}
			break;
		case ANGRIFF_SENKEN:
			defender.lowerAttack();
			log.add(defender.getName() + ": Angriff sinkt!");
			break;
		case VERTEIDIGUNG_SENKEN:
			defender.lowerDefense();
			log.add(defender.getName() + ": Verteidigung sinkt!");
			break;
		default:
			break;
		}
	}

	private void applyEndOfTurn() {
		poisonTick(player);
		poisonTick(enemy);
	}

	private void poisonTick(Battler battler) {
		if (battler.getStatus() == Status.VERGIFTET && !battler.isFainted()) {
			int damage = Math.max(1, battler.getMaxHp() / 8);
			battler.setCurrentHp(Math.max(0, battler.getCurrentHp() - damage));
			log.add(battler.getName() + " erleidet " + damage + " Giftschaden.");
		}
	}

	/**
	 * Prüft, ob der Kampf beendet ist, und setzt ggf. das Ergebnis.
	 *
	 * @return true, wenn der Kampf vorbei ist
	 */
	private boolean checkFainted() {
		if (enemy.isFainted()) {
			log.add("Das wilde " + enemy.getName() + " wurde besiegt!");
			result = Result.SPIELER_GEWINNT;
			return true;
		}
		if (player.isFainted()) {
			log.add(player.getName() + " wurde besiegt …");
			result = Result.SPIELER_VERLIERT;
			return true;
		}
		return false;
	}
}
