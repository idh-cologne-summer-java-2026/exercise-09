package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.battle.Battler;
import koeln.uni.idh.java1.session11.zoo.battle.LevelUpResult;
import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Status;
import koeln.uni.idh.java1.session11.zoo.battle.Type;
import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

/**
 * Diese Klasse repräsentiert laufende Säugetiere. Sie haben eine Position
 * (x/y), eine Blickrichtung und eine Schrittweite und wissen, wie sie in der
 * Zoo-Visualisierung dargestellt werden.
 *
 * Für „Zookémon" ist die Klasse zusätzlich ein {@link Battler}: jedes Tier
 * trägt seinen Typ, seine Werte ({@link Stats}), seine Attacken sowie die im
 * Kampf veränderlichen Daten (aktuelle HP, Status, Stat-Stufen).
 *
 * @author nils.reiter@uni-koeln.de
 */
public abstract class WalkingMammal implements Drawable, Battler {
	protected String name;

	/** die aktuelle x-Position des Tieres */
	int x = 1;

	/** die aktuelle y-Position des Tieres */
	int y = 1;

	/** wie weit das Tier in einem einzelnen Schritt läuft */
	int stepsize = 1;

	/**
	 * Aktuelle Blickrichtung auf einer 360°-Rose.
	 * 0 => oben, 90 => rechts, 180 => links, 270 => unten
	 */
	int direction = 0;

	// --- Kampf-Eigenschaften ---
	protected Type type = Type.NORMAL;
	protected Stats stats = new Stats(40, 40, 40, 40);
	protected final List<Move> moves = new ArrayList<>();
	protected int level = 5;

	protected int currentHp;
	protected Status status = Status.KEINER;
	protected int attackStage = 0;
	protected int defenseStage = 0;

	// --- Fortschritt (Level/Erfahrung/Entwicklung) ---
	protected int xp = 0;
	protected int evolutionLevel = Integer.MAX_VALUE;
	protected String evolvedName = null;
	protected Move evolutionMove = null;
	protected boolean evolved = false;

	/**
	 * Richtet die Kampf-Eigenschaften eines Tieres ein. Wird von den
	 * Unterklassen im Konstruktor aufgerufen. Setzt die HP auf das Maximum.
	 */
	protected void setupBattler(String name, Type type, Stats stats, Move... moves) {
		this.name = name;
		this.type = type;
		this.stats = stats;
		this.moves.clear();
		this.moves.addAll(Arrays.asList(moves));
		this.currentHp = stats.getMaxHp();
	}

	/**
	 * Legt fest, wie sich das Tier entwickelt: ab welchem Level, zu welchem
	 * Namen und mit welcher zusätzlichen Signatur-Attacke. Von den Unterklassen
	 * im Konstruktor aufgerufen.
	 */
	protected void setEvolution(int atLevel, String evolvedName, Move evolutionMove) {
		this.evolutionLevel = atLevel;
		this.evolvedName = evolvedName;
		this.evolutionMove = evolutionMove;
	}

	/**
	 * Das Tier läuft einen einzelnen Schritt in seine Blickrichtung. Wird in
	 * der Overworld nicht direkt genutzt – dort prüft die Welt vorher mit
	 * {@link #peekStep()}, ob das Zielfeld begehbar ist.
	 */
	public void walk() {
		int[] next = peekStep();
		this.x = next[0];
		this.y = next[1];
	}

	/**
	 * Berechnet das Zielfeld eines Schrittes, ohne die Position zu verändern.
	 *
	 * @return ein Array {neuesX, neuesY}
	 */
	public int[] peekStep() {
		int nx = x;
		int ny = y;
		switch (direction) {
		case 0:
			ny = y - stepsize;
			break;
		case 90:
			nx = x + stepsize;
			break;
		case 180:
			nx = x - stepsize;
			break;
		case 270:
			ny = y + stepsize;
			break;
		default:
			break;
		}
		return new int[] { nx, ny };
	}

	/**
	 * Dreht das Tier nach links (negativ) oder rechts (positiv). Das Ergebnis
	 * wird sauber auf den Bereich 0..359 normalisiert (behebt den früheren
	 * Modulo-Bug).
	 *
	 * @param turnDirection negativ = nach links, positiv = nach rechts
	 */
	public void turn(int turnDirection) {
		int delta = (int) Math.signum(turnDirection) * 90;
		this.direction = ((this.direction + delta) % 360 + 360) % 360;
	}

	public void setDirection(int direction) {
		this.direction = ((direction % 360) + 360) % 360;
	}

	public int getDirection() {
		return direction;
	}

	/**
	 * Wie das Tier auf dem Zoo-Feld dargestellt wird (klassenweit, kein
	 * individuelles Tier).
	 */
	public abstract char getSymbol();

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	// --- Battler-Implementierung ---

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public Stats getStats() {
		return stats;
	}

	@Override
	public List<Move> getMoves() {
		return moves;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getXp() {
		return xp;
	}

	@Override
	public int getXpForNextLevel() {
		return level * 20;
	}

	@Override
	public boolean isEvolved() {
		return evolved;
	}

	@Override
	public int getCurrentHp() {
		return currentHp;
	}

	@Override
	public void setCurrentHp(int hp) {
		this.currentHp = Math.max(0, Math.min(hp, getMaxHp()));
	}

	@Override
	public int getMaxHp() {
		return stats.getMaxHp();
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public boolean isFainted() {
		return currentHp <= 0;
	}

	@Override
	public int getEffectiveAttack() {
		return Math.max(1, (int) (stats.getAttack() * stageMultiplier(attackStage)));
	}

	@Override
	public int getEffectiveDefense() {
		return Math.max(1, (int) (stats.getDefense() * stageMultiplier(defenseStage)));
	}

	@Override
	public void lowerAttack() {
		if (attackStage > -6) {
			attackStage--;
		}
	}

	/**
	 * Hebt den Angriff um mehrere Stufen an – z. B. wenn des Professors Zookémon in
	 * Phase 2 vor Wut aufdreht. Begrenzt auf die übliche Höchststufe (+6).
	 */
	public void raiseAttack(int stages) {
		for (int i = 0; i < stages; i++) {
			if (attackStage < 6) {
				attackStage++;
			}
		}
	}

	@Override
	public void lowerDefense() {
		if (defenseStage > -6) {
			defenseStage--;
		}
	}

	/**
	 * Lässt das Tier Erfahrung sammeln. Reicht sie für ein oder mehrere Level,
	 * steigen die Werte; wird dabei das Entwicklungs-Level erreicht, entwickelt
	 * sich das Tier.
	 *
	 * @param amount gewonnene Erfahrung
	 * @return was sich dabei verändert hat (für die Anzeige)
	 */
	public LevelUpResult gainExperience(int amount) {
		int levelsBefore = level;
		boolean evolvedBefore = evolved;
		String oldName = name;

		xp += amount;
		while (xp >= getXpForNextLevel()) {
			xp -= getXpForNextLevel();
			level++;
			growStats();
			if (!evolved && level >= evolutionLevel && evolvedName != null) {
				evolve();
			}
		}
		return new LevelUpResult(level - levelsBefore, level,
				evolved && !evolvedBefore, oldName, name);
	}

	/**
	 * Hebt ein frisches Tier auf das Ziellevel an (für wilde Tiere, damit sie
	 * mit dem Spieler mitwachsen). Heilt anschließend vollständig.
	 */
	public void scaleToLevel(int targetLevel) {
		while (level < targetLevel) {
			level++;
			growStats();
		}
		this.currentHp = getMaxHp();
	}

	/**
	 * Bringt ein frisches Tier inklusive Entwicklung auf das Ziellevel – für
	 * starke Trainer-Tiere (z. B. der Professor), die in entwickelter Form und auf
	 * hohem Level antreten. Heilt anschließend vollständig.
	 */
	public void developToLevel(int targetLevel) {
		while (level < targetLevel) {
			level++;
			growStats();
			if (!evolved && level >= evolutionLevel && evolvedName != null) {
				evolve();
			}
		}
		this.currentHp = getMaxHp();
	}

	/** Erhöht die Basiswerte um einen festen Betrag pro Level. */
	private void growStats() {
		int oldMax = stats.getMaxHp();
		stats = new Stats(stats.getMaxHp() + 6, stats.getAttack() + 3,
				stats.getDefense() + 3, stats.getSpeed() + 3);
		// Das neu gewonnene Leben kommt direkt auf die aktuellen HP obendrauf.
		currentHp += stats.getMaxHp() - oldMax;
	}

	/** Entwickelt das Tier: neuer Name, kräftiger Stat-Schub, Signatur-Attacke. */
	private void evolve() {
		int oldMax = stats.getMaxHp();
		stats = new Stats((int) (stats.getMaxHp() * 1.2), (int) (stats.getAttack() * 1.2),
				(int) (stats.getDefense() * 1.2), (int) (stats.getSpeed() * 1.2));
		currentHp += stats.getMaxHp() - oldMax;
		name = evolvedName;
		if (evolutionMove != null) {
			moves.add(evolutionMove);
		}
		evolved = true;
	}

	/**
	 * Hebt die Basiswerte dauerhaft an – z. B. durch den Buff eines NPC. Zusätzliche
	 * maximale HP gibt es sofort als geheilte HP obendrauf.
	 */
	public void applyPermanentBuff(int hpUp, int atkUp, int defUp, int spdUp) {
		stats = new Stats(stats.getMaxHp() + hpUp, stats.getAttack() + atkUp,
				stats.getDefense() + defUp, stats.getSpeed() + spdUp);
		currentHp += hpUp;
	}

	/** Stellt HP, Status und Stat-Stufen vollständig wieder her. */
	public void restore() {
		this.currentHp = getMaxHp();
		clearBattleModifiers();
	}

	/**
	 * Setzt die nur im Kampf gültigen Veränderungen zurück (Status-Effekte und
	 * Stat-Stufen), behält aber die aktuellen HP. Wird nach jedem Kampf
	 * aufgerufen.
	 */
	public void clearBattleModifiers() {
		this.status = Status.KEINER;
		this.attackStage = 0;
		this.defenseStage = 0;
	}

	/** Pokémon-artiger Stufen-Multiplikator (-6..+6). */
	private static double stageMultiplier(int stage) {
		if (stage >= 0) {
			return (2.0 + stage) / 2.0;
		}
		return 2.0 / (2.0 - stage);
	}
}
