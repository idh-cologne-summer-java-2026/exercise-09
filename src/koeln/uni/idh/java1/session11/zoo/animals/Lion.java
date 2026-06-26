package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.MoveEffect;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Lion extends WalkingMammal {

	public Lion() {
		setupBattler("Glutprankel", Type.FEUER, new Stats(55, 70, 45, 60),
				new Move("Flammenbiss", Type.FEUER, 50, 100),
				new Move("Brüllen", Type.NORMAL, 0, 100, MoveEffect.VERTEIDIGUNG_SENKEN, 100),
				new Move("Feuersturm", Type.FEUER, 65, 90));
		setEvolution(8, "Pyroleon", new Move("Höllenglut", Type.FEUER, 75, 95));
	}

	@Override
	public char getSymbol() {
		return 'L';
	}
}
