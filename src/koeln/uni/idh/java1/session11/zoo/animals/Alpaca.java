package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.MoveEffect;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Alpaca extends WalkingMammal {

	public Alpaca() {
		setupBattler("Alpaka", Type.PFLANZE, new Stats(45, 49, 49, 45),
				new Move("Spucke", Type.PFLANZE, 40, 95, MoveEffect.VERGIFTEN, 30),
				new Move("Tritt", Type.NORMAL, 35, 100),
				new Move("Rammstoß", Type.NORMAL, 50, 90));
		setEvolution(8, "Alpakarol", new Move("Wollsturm", Type.PFLANZE, 70, 90));
	}

	@Override
	public char getSymbol() {
		return 'A';
	}
}
