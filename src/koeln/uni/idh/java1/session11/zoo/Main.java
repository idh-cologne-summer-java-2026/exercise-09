package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Giraffe;
import koeln.uni.idh.java1.session11.zoo.animals.HybridMammal;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.Penguin;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		Castle castle = new Castle();

		Alpaca alpaca = new Alpaca();
		Elephant elephant = new Elephant();
		Lion lion = new Lion();
		Giraffe giraffe = new Giraffe();
		Penguin penguin = new Penguin();

		alpaca.setX(6);
		alpaca.setY(4);
		elephant.setX(9);
		elephant.setY(5);
		lion.setX(12);
		lion.setY(4);
		giraffe.setX(8);
		giraffe.setY(6);
		penguin.setX(11);
		penguin.setY(5);

		System.out.println("Zoo before the escape:");
		printScene(castle, alpaca, elephant, lion, giraffe, penguin);

		System.out.println("\nThe lion, elephant and penguin are escaping!");
		escape(lion, 0, 3);       // lion moves upward out of the castle
		escape(elephant, 1, 7);   // elephant runs to the right
		escape(penguin, -1, 4);   // penguin waddles downward

		System.out.println("\nJetzt vermehren sich einige Tiere:");
		HybridMammal giraffeLion = reproduce(giraffe, lion, 'Q', "Giraffion");
		HybridMammal elephantPenguin = reproduce(elephant, penguin, 'X', "Elepengu");

		System.out.println("\nZoo after escape and reproduction:");
		printScene(castle, alpaca, elephant, lion, giraffe, penguin, giraffeLion, elephantPenguin);
	}

	private static HybridMammal reproduce(WalkingMammal parent1, WalkingMammal parent2, char symbol, String species) {
		HybridMammal child = new HybridMammal(symbol, species, parent1.getClass().getSimpleName() + "-" + parent2.getClass().getSimpleName() + " child");
		child.setX((parent1.getX() + parent2.getX()) / 2);
		child.setY((parent1.getY() + parent2.getY()) / 2);
		System.out.println("" + parent1.getClass().getSimpleName() + " and " + parent2.getClass().getSimpleName()
				+ " have produced a hybrid: " + child.getSpecies() + " at (" + child.getX() + ", " + child.getY() + ").");
		return child;
	}

	private static void escape(WalkingMammal animal, int turnDirection, int steps) {
		if (turnDirection != 0) {
			animal.turn(turnDirection);
		}
		for (int i = 0; i < steps; i++) {
			animal.walk();
		}
	}

	private static void printScene(Castle castle, WalkingMammal... animals) {
		AsciiImage ai = new AsciiImage(20, 12);
		drawCastle(ai, castle);
		for (WalkingMammal animal : animals) {
			ai.dot(animal.getX(), animal.getY(), animal);
		}
		System.out.println(ai.toString());
	}

	private static void drawCastle(AsciiImage ai, Castle castle) {
		for (int x = 4; x < 16; x++) {
			ai.dot(x, 2, castle);
			ai.dot(x, 8, castle);
		}
		for (int y = 2; y <= 8; y++) {
			ai.dot(4, y, castle);
			ai.dot(15, y, castle);
		}
		ai.dot(4, 5, castle);
		ai.dot(15, 5, castle);
	}
}
