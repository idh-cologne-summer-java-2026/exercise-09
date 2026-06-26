package koeln.uni.idh.java1.session11.zoo.engine;

import java.util.ArrayList;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

/**
 * Das Team des Spielers: bis zu {@link #MAX_SIZE} Tiere, von denen genau eines
 * gerade „aktiv" ist (das in der Overworld läuft und im Kampf antritt). Gefangene
 * wilde Tiere wandern hier hinein; im Kampf kann der Spieler das aktive Tier
 * gegen ein anderes austauschen.
 */
public class Team {

	public static final int MAX_SIZE = 6;

	private final List<WalkingMammal> members = new ArrayList<>();
	private int activeIndex = 0;

	/** Das gerade kämpfende bzw. laufende Tier. */
	public WalkingMammal getActive() {
		return members.get(activeIndex);
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public List<WalkingMammal> getMembers() {
		return members;
	}

	public int size() {
		return members.size();
	}

	public boolean isFull() {
		return members.size() >= MAX_SIZE;
	}

	/**
	 * Nimmt ein Tier ins Team auf, sofern noch Platz ist.
	 *
	 * @return true, wenn das Tier aufgenommen wurde
	 */
	public boolean add(WalkingMammal animal) {
		if (isFull()) {
			return false;
		}
		members.add(animal);
		return true;
	}

	/** Macht das Tier an der Stelle {@code index} zum aktiven Tier. */
	public void setActive(int index) {
		if (index >= 0 && index < members.size()) {
			this.activeIndex = index;
		}
	}

	/** Ob außer dem aktiven Tier noch ein weiteres kampffähig ist. */
	public boolean hasOtherAlive() {
		for (int i = 0; i < members.size(); i++) {
			if (i != activeIndex && !members.get(i).isFainted()) {
				return true;
			}
		}
		return false;
	}

	/** Ob das gesamte Team besiegt ist (dann ist das Spiel verloren). */
	public boolean allFainted() {
		for (WalkingMammal m : members) {
			if (!m.isFainted()) {
				return false;
			}
		}
		return true;
	}
}
