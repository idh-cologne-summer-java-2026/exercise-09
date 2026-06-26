package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.MoveEffect;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Horse extends WalkingMammal {

	public Horse() {
		setupBattler("Pferd", Type.NORMAL, new Stats(50, 52, 43, 70),
				new Move("Huftritt", Type.NORMAL, 45, 95),
				new Move("Wiehern", Type.NORMAL, 0, 100, MoveEffect.ANGRIFF_SENKEN, 100),
				new Move("Galopp", Type.NORMAL, 55, 90));
	}

	@Override
	public char getSymbol() {
		return 'H';
	}
}
