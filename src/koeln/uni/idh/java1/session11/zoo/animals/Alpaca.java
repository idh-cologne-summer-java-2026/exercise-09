package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.MoveEffect;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Alpaca extends WalkingMammal {

	public Alpaca() {
		setupBattler("Flauschpaka", Type.PFLANZE, new Stats(45, 49, 49, 45),
				new Move("Spucke", Type.PFLANZE, 45, 100, MoveEffect.VERGIFTEN, 20),
				new Move("Tritt", Type.NORMAL, 45, 100),
				new Move("Rammstoß", Type.NORMAL, 60, 90));
		setEvolution(8, "Wollkoloss", new Move("Wollsturm", Type.PFLANZE, 75, 95));
	}

	@Override
	public char getSymbol() {
		return 'A';
	}
}
