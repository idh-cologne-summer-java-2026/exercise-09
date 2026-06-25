package koeln.uni.idh.java1.session11.zoo.battle;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Die rundenbasierte Kampf-Engine. Sie ist bewusst UI-frei: sie kennt nur
 * zwei {@link Battler} und schreibt alles Geschehene in ein Log, das die
 * Darstellung anschließend ausliest.
 *
 * Eine Runde wird nicht auf einen Schlag berechnet, sondern in einzelne
 * Schritte zerlegt (eigener Angriff, Gegenangriff, Giftschaden …). Die
 * Darstellung kann so jeden Schritt mit einer kurzen Pause zeigen
 * ({@link #step()} / {@link #hasPendingSteps()}).
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
	private final Deque<Runnable> steps = new ArrayDeque<>();
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

	/** Ob noch Schritte der aktuellen Runde abzuarbeiten sind. */
	public boolean hasPendingSteps() {
		return !steps.isEmpty();
	}

	/**
	 * Plant eine komplette Runde: Der Spieler setzt die gewählte Attacke ein,
	 * das wilde Tier eine zufällige. Wer zuerst handelt, entscheidet die
	 * Initiative (bei Gleichstand der Zufall). Am Rundenende wirken
	 * Statuseffekte. Ausgeführt werden die Schritte dann über {@link #step()}.
	 *
	 * @param moveIndex Index der vom Spieler gewählten Attacke
	 */
	public void playerSelectsMove(int moveIndex) {
		if (isOver() || hasPendingSteps()) {
			return;
		}
		Move playerMove = player.getMoves().get(moveIndex);
		Move enemyMove = enemy.getMoves().get(rng.nextInt(enemy.getMoves().size()));

		boolean playerFirst = decidePlayerFirst();
		Battler first = playerFirst ? player : enemy;
		Battler second = playerFirst ? enemy : player;
		Move firstMove = playerFirst ? playerMove : enemyMove;
		Move secondMove = playerFirst ? enemyMove : playerMove;

		steps.add(() -> executeMove(first, second, firstMove));
		steps.add(() -> executeMove(second, first, secondMove));
		steps.add(() -> poisonTick(player));
		steps.add(() -> poisonTick(enemy));
	}

	/**
	 * Fluchtversuch. Ist der Spieler schneller, gelingt er sicher, sonst mit
	 * 50%. Scheitert er, greift das wilde Tier einmal an (als geplante Schritte).
	 */
	public void flee() {
		if (isOver() || hasPendingSteps()) {
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
			steps.add(() -> executeMove(enemy, player, enemyMove));
			steps.add(() -> poisonTick(player));
			steps.add(() -> poisonTick(enemy));
		}
	}

	/**
	 * Führt den nächsten geplanten Schritt der Runde aus. Endet der Kampf
	 * dabei, werden die restlichen Schritte verworfen.
	 *
	 * @return true, wenn ein Schritt ausgeführt wurde
	 */
	public boolean step() {
		Runnable next = steps.poll();
		if (next == null) {
			return false;
		}
		next.run();
		if (checkFainted()) {
			steps.clear();
		}
		return true;
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
		// Wer schon besiegt ist, handelt nicht mehr.
		if (attacker.isFainted()) {
			return;
		}
		log.add(attacker.getName() + " setzt " + move.getName() + " ein!");

		// Genauigkeits-Wurf
		if (rng.nextInt(100) >= move.getAccuracy()) {
			log.add("… die Attacke geht daneben!");
			return;
		}

		// Schaden (nur falls die Attacke Stärke hat)
		if (move.getPower() > 0) {
			DamageResult dmg = calculator.calculate(attacker, defender, move);
			defender.setCurrentHp(Math.max(0, defender.getCurrentHp() - dmg.getDamage()));
			log.add(defender.getName() + " erleidet " + dmg.getDamage() + " Schaden.");
			if (dmg.isCritical()) {
				log.add("Volltreffer!");
			}
			if (dmg.getEffectiveness() > 1.0) {
				log.add("Sehr effektiv!");
			} else if (dmg.getEffectiveness() < 1.0) {
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
