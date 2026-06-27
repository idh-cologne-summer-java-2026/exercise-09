package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		AsciiImage ai = new AsciiImage(45, 10);

		WalkingMammal alpaca = new Alpaca();
		WalkingMammal horse = new Horse("Sunny", "brown");
		WalkingMammal elephant = new Elephant();

		ai.drawEnclosure(1, 1, 12, 6);
		ai.drawEnclosure(16, 1, 12, 6);
		ai.drawEnclosure(31, 1, 12, 6);

		ai.dot(0, 0, new Tree());
		ai.dot(14, 0, new Tree());
		ai.dot(29, 0, new Tree());
		ai.dot(44, 0, new Tree());
		ai.dot(7, 7, new Tree());
		ai.dot(22, 7, new Tree());
		ai.dot(37, 7, new Tree());

		ai.dot(3, 3, alpaca);
		ai.dot(18, 3, horse);
		ai.dot(34, 2, elephant);

		System.out.println("Zoo map:");
		System.out.println(ai.toString());
	}

}
