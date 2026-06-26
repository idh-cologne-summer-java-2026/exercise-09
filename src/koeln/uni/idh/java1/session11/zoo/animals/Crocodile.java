package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Crocodile extends WalkingMammal {

	public Crocodile() {
		setupBattler("Schnappix", Type.WASSER, new Stats(60, 65, 60, 35),
				new Move("Wasserstoß", Type.WASSER, 45, 95),
				new Move("Todesrolle", Type.WASSER, 55, 75),
				new Move("Biss", Type.NORMAL, 45, 100));
		setEvolution(8, "Tidekrok", new Move("Urbiss", Type.WASSER, 70, 90));
	}

	@Override
	public char getSymbol() {
		return 'K';
	}
}
