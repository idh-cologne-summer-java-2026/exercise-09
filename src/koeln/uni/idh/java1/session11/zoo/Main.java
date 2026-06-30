package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;
import koeln.uni.idh.java1.session11.zoo.Tree;

public class Main {

	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;

	public static void main(String[] args) {
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);
		AsciiImage ai = new AsciiImage(WIDTH, HEIGHT);
		List<WalkingMammal> animals = new ArrayList<>();

		for (int i = 0; i < random.nextInt(6); i++) {
			Alpaca alpaca = new Alpaca();
			placeAnimal(ai, alpaca, random);
			animals.add(alpaca);
		}

		for (int i = 0; i < random.nextInt(6); i++) {
			Elephant elephant = new Elephant();
			placeAnimal(ai, elephant, random);
			animals.add(elephant);
		}

		for (int i = 0; i < random.nextInt(6); i++) {
			Horse horse = new Horse("Horse " + (i + 1), "brown");
			placeAnimal(ai, horse, random);
			animals.add(horse);
		}

		for (WalkingMammal animal : animals) {
			ai.place(animal.getX(), animal.getY(), animal);
		}

		System.out.println("Zoo initialized. You can add trees by entering coordinates.");
		System.out.println(ai.toString());

		while (true) {
			System.out.print("Enter 'add x y' to place a tree, or 'quit' to exit: ");
			String input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase("quit")) {
				break;
			}

			String[] parts = input.split("\\s+");
			if (parts.length != 3 || !parts[0].equalsIgnoreCase("add")) {
				System.out.println("Please use the format: add x y");
				continue;
			}

			try {
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				Tree tree = new Tree();
				if (ai.place(x, y, tree)) {
					System.out.println("Tree placed.");
				} else {
					System.out.println("That spot is already occupied or outside the grid.");
				}
				System.out.println(ai.toString());
			} catch (NumberFormatException e) {
				System.out.println("Coordinates must be integers.");
			}
		}

		scanner.close();
	}

	private static void placeAnimal(AsciiImage ai, WalkingMammal animal, Random random) {
		for (int attempt = 0; attempt < 100; attempt++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			if (ai.place(x, y, animal)) {
				animal.setX(x);
				animal.setY(y);
				return;
			}
		}
	}

}
