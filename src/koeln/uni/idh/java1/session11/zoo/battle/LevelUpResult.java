package koeln.uni.idh.java1.session11.zoo.battle;

/**
 * Ergebnis eines Erfahrungs-Zuwachses: wie viele Level gewonnen wurden und ob
 * sich das Tier dabei entwickelt hat. Die Darstellung zeigt daraufhin
 * „Level X erreicht!" bzw. eine Entwicklungs-Animation.
 */
public class LevelUpResult {

	private final int levelsGained;
	private final int newLevel;
	private final boolean evolved;
	private final String oldName;
	private final String newName;

	public LevelUpResult(int levelsGained, int newLevel, boolean evolved,
			String oldName, String newName) {
		this.levelsGained = levelsGained;
		this.newLevel = newLevel;
		this.evolved = evolved;
		this.oldName = oldName;
		this.newName = newName;
	}

	public int getLevelsGained() {
		return levelsGained;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public boolean leveledUp() {
		return levelsGained > 0;
	}

	public boolean isEvolved() {
		return evolved;
	}

	public String getOldName() {
		return oldName;
	}

	public String getNewName() {
		return newName;
	}
}
