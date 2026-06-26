package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Elephant extends WalkingMammal {

	public Elephant() {
		setupBattler("Elefant", Type.ERDE, new Stats(80, 60, 70, 25),
				new Move("Stampfer", Type.ERDE, 50, 90),
				new Move("Rüsselschlag", Type.NORMAL, 40, 100),
				new Move("Erdbeben", Type.ERDE, 65, 80));
	}

	@Override
	public char getSymbol() {
		return 'E';
	}
}
