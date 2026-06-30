package koeln.uni.idh.java1.session11.zoo;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.Tiger;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		Charset consoleCharset = Charset.forName("windows-1252");
		System.setOut(new PrintStream(System.out, true, consoleCharset));
		System.setErr(new PrintStream(System.err, true, consoleCharset));

		List<WalkingMammal> animals = new ArrayList<>();
		Alpaca alpaca = new Alpaca();
		alpaca.setX(2);
		alpaca.setY(2);
		Horse horse = new Horse("Spirit", "brown");
		horse.setX(5);
		horse.setY(2);
		Tiger tiger = new Tiger("Sascha");
		tiger.setX(8);
		tiger.setY(2);
		animals.add(alpaca);
		animals.add(horse);
		animals.add(tiger);

		Scanner scanner = new Scanner(System.in, consoleCharset);
		String input = "";
		while (!"ende".equalsIgnoreCase(input)) {
			AsciiImage ai = new AsciiImage(12, 8);
			for (WalkingMammal animal : animals) {
				ai.dot(animal.getX(), animal.getY(), animal);
			}
			System.out.println("\nZoo-Simulation");
			System.out.println(ai.toString());
			for (int i = 0; i < animals.size(); i++) {
				System.out.println((i + 1) + ") " + animals.get(i).getStatus());
			}
			System.out.print("Waehle Aktion [fuettern/wasser/streicheln/weiter/ende]: ");
			input = scanner.nextLine();

			if ("ende".equalsIgnoreCase(input)) {
				break;
			}
			if ("weiter".equalsIgnoreCase(input)) {
				for (WalkingMammal animal : animals) {
					animal.tick();
				}
				continue;
			}

			System.out.print("Welches Tier? (1-" + animals.size() + "): ");
			String choice = scanner.nextLine();
			int index = Integer.parseInt(choice) - 1;
			if (index >= 0 && index < animals.size()) {
				WalkingMammal selected = animals.get(index);
				if ("fuettern".equalsIgnoreCase(input)) {
					selected.feed();
				} else if ("wasser".equalsIgnoreCase(input)) {
					selected.giveWater();
				} else if ("streicheln".equalsIgnoreCase(input)) {
					selected.pet();
				}
			}
			for (WalkingMammal animal : animals) {
				animal.tick();
			}
		}
		scanner.close();
	}

}
