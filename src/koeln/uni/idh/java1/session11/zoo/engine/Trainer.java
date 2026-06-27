package koeln.uni.idh.java1.session11.zoo.engine;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

/**
 * Ein Trainer als Kampfgegner – im Gegensatz zu wilden Tieren. Aktuell gibt es
 * genau einen: den Erzfeind: <b>Der Professor</b>, der mit einem einzigen, starken
 * Signatur-Zookémon antritt (der entwickelten Konterform deines Starters).
 */
public class Trainer {

	private final String name;
	private final WalkingMammal zookemon;

	public Trainer(String name, WalkingMammal zookemon) {
		this.name = name;
		this.zookemon = zookemon;
	}

	public String getName() {
		return name;
	}

	public WalkingMammal getZookemon() {
		return zookemon;
	}
}
