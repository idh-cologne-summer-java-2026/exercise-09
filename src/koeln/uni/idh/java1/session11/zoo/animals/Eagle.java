package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.battle.Move;
import koeln.uni.idh.java1.session11.zoo.battle.Stats;
import koeln.uni.idh.java1.session11.zoo.battle.Type;

public class Eagle extends WalkingMammal {

	public Eagle() {
		setupBattler("Wirbelaar", Type.LUFT, new Stats(40, 60, 40, 80),
				new Move("Windstoß", Type.LUFT, 50, 100),
				new Move("Krallen", Type.NORMAL, 45, 100),
				new Move("Sturzflug", Type.LUFT, 65, 90));
		setEvolution(8, "Orkanus", new Move("Orkanschwinge", Type.LUFT, 75, 95));
	}

	@Override
	public char getSymbol() {
		return 'D';
	}
}
