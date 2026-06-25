package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.MoveEffect;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Lion extends WalkingMammal {

	public Lion() {
		setupBattler("Löwe", Type.FEUER, new Stats(55, 70, 45, 60),
				new Move("Flammenbiss", Type.FEUER, 45, 95),
				new Move("Brüllen", Type.NORMAL, 0, 100, MoveEffect.VERTEIDIGUNG_SENKEN, 100));
	}

	@Override
	public char getSymbol() {
		return 'L';
	}
}
