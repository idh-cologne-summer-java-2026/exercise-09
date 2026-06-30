package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.Penguin;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {
	private static final int WIDTH = 24;
	private static final int HEIGHT = 12;
	private static final int MAX_ROUNDS = 12;

	public static void main(String[] args) {
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);

		List<WalkingMammal> animals = new ArrayList<>();
		animals.add(place(new Alpaca(), 3, 3));
		animals.add(place(new Elephant(), 10, 6));
		animals.add(place(new Penguin(), 18, 4));
		animals.add(place(new Lion(), 6, 9));

		List<Tree> trees = new ArrayList<>();
		trees.add(new Tree(4, 2));
		trees.add(new Tree(15, 2));
		trees.add(new Tree(20, 8));
		trees.add(new Tree(8, 10));

		FeedingStation feedingStation = new FeedingStation(1, 1, 8);
		WaterStation waterStation = new WaterStation(WIDTH - 2, HEIGHT - 2, 10);

		System.out.println("EcoZoo startet: Wetter, Tierwohl und deine Entscheidungen veraendern den Zoo.");
		System.out.println("Symbole: A Alpaka, E Elefant, P Pinguin, L Loewe, T Baum, F Futter, W Wasser");

		for (int round = 1; round <= MAX_ROUNDS; round++) {
			Weather weather = Weather.random(random);
			for (WalkingMammal animal : animals) {
				animal.liveOneHour(weather, random, WIDTH, HEIGHT);
			}

			printRound(round, weather, animals, trees, feedingStation, waterStation);
			triggerEvent(random, weather, animals, trees, feedingStation, waterStation);

			if (!askForAction(scanner, animals, feedingStation, waterStation)) {
				break;
			}
		}

		printFinalScore(animals, feedingStation, waterStation);
		scanner.close();
	}

	private static WalkingMammal place(WalkingMammal animal, int x, int y) {
		animal.setX(x);
		animal.setY(y);
		return animal;
	}

	private static void printRound(int round, Weather weather, List<WalkingMammal> animals, List<Tree> trees,
			FeedingStation feedingStation, WaterStation waterStation) {
		AsciiImage image = new AsciiImage(WIDTH, HEIGHT);
		image.dot(feedingStation.getX(), feedingStation.getY(), feedingStation);
		image.dot(waterStation.getX(), waterStation.getY(), waterStation);
		for (Tree tree : trees) {
			image.dot(tree.getX(), tree.getY(), tree);
		}
		for (WalkingMammal animal : animals) {
			image.dot(animal.getX(), animal.getY(), animal);
		}

		System.out.println();
		System.out.println("=== Runde " + round + " | Wetter: " + weather.getLabel() + " ===");
		System.out.println(image.toString());
		System.out.println("Futterportionen: " + feedingStation.getPortions() + " | Wasserliter: " + waterStation.getLiters());
		for (int i = 0; i < animals.size(); i++) {
			System.out.println((i + 1) + ": " + animals.get(i).statusLine());
		}
		System.out.println("Tierwohl-Score: " + averageWelfare(animals) + "/100");
	}

	private static void triggerEvent(Random random, Weather weather, List<WalkingMammal> animals, List<Tree> trees,
			FeedingStation feedingStation, WaterStation waterStation) {
		if (weather == Weather.HEATWAVE) {
			waterStation.refill(-Math.min(2, waterStation.getLiters()));
			System.out.println("Event: Die Hitzewelle senkt den Wasservorrat.");
		}
		if (random.nextInt(100) < 18) {
			feedingStation.refill(2);
			System.out.println("Event: Das Zoo-Team liefert zwei Futterportionen nach.");
		}
		for (WalkingMammal animal : animals) {
			if (animal.needsVet()) {
				System.out.println("Warnung: " + animal.getDisplayName() + " braucht bald den Tierarzt.");
			}
		}
		if (weather == Weather.STORM && trees.size() > 2 && random.nextBoolean()) {
			trees.remove(trees.size() - 1);
			System.out.println("Event: Ein Sturm hat einen Baum beschaedigt.");
		}
	}

	private static boolean askForAction(Scanner scanner, List<WalkingMammal> animals, FeedingStation feedingStation,
			WaterStation waterStation) {
		System.out.println();
		System.out.println("Aktion: [f]uettern, [w]asser auffuellen, [s]treicheln, [v]eterinaer, [q]uit, Enter = weiter");
		if (!scanner.hasNextLine()) {
			return false;
		}
		String action = scanner.nextLine().trim().toLowerCase();
		if (action.isEmpty()) {
			return true;
		}
		if (action.equals("q")) {
			return false;
		}

		switch (action) {
		case "f":
			feedAnimal(scanner, animals, feedingStation);
			break;
		case "w":
			waterStation.refill(5);
			System.out.println("Die Wasserstelle wurde aufgefuellt.");
			break;
		case "s":
			petAnimal(scanner, animals);
			break;
		case "v":
			treatAnimal(scanner, animals);
			break;
		default:
			System.out.println("Unbekannte Aktion. Die Runde laeuft weiter.");
			break;
		}
		return true;
	}

	private static void feedAnimal(Scanner scanner, List<WalkingMammal> animals, FeedingStation feedingStation) {
		WalkingMammal animal = chooseAnimal(scanner, animals);
		if (animal == null) {
			return;
		}
		if (feedingStation.takePortion()) {
			animal.feed();
			System.out.println(animal.getDisplayName() + " wurde gefuettert.");
		} else {
			System.out.println("Die Futterstation ist leer.");
		}
	}

	private static void petAnimal(Scanner scanner, List<WalkingMammal> animals) {
		WalkingMammal animal = chooseAnimal(scanner, animals);
		if (animal != null) {
			animal.pet();
			System.out.println(animal.getDisplayName() + " wurde gestreichelt.");
		}
	}

	private static void treatAnimal(Scanner scanner, List<WalkingMammal> animals) {
		WalkingMammal animal = chooseAnimal(scanner, animals);
		if (animal != null) {
			animal.treatByVet();
			System.out.println("Der Tierarzt behandelt " + animal.getDisplayName() + ".");
		}
	}

	private static WalkingMammal chooseAnimal(Scanner scanner, List<WalkingMammal> animals) {
		System.out.println("Tiernummer eingeben:");
		if (!scanner.hasNextLine()) {
			return null;
		}
		try {
			int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
			if (index >= 0 && index < animals.size()) {
				return animals.get(index);
			}
		} catch (NumberFormatException e) {
			System.out.println("Das war keine gueltige Nummer.");
		}
		return null;
	}

	private static void printFinalScore(List<WalkingMammal> animals, FeedingStation feedingStation,
			WaterStation waterStation) {
		System.out.println();
		System.out.println("=== Abschlussbericht ===");
		System.out.println("Tierwohl: " + averageWelfare(animals) + "/100");
		System.out.println("Artenvielfalt: " + animals.size() + " Tierarten");
		System.out.println("Nachhaltigkeit: " + Math.min(100, feedingStation.getPortions() * 8 + waterStation.getLiters() * 5)
				+ "/100");
		System.out.println("Fazit: Je hoeher Tierwohl und Vorrat, desto stabiler ist der EcoZoo.");
	}

	private static int averageWelfare(List<WalkingMammal> animals) {
		int sum = 0;
		for (WalkingMammal animal : animals) {
			sum += animal.welfareScore();
		}
		return sum / animals.size();
	}
}
