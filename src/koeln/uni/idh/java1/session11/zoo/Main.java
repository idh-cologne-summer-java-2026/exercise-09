package koeln.uni.idh.java1.session11.zoo;

import java.util.Scanner;
import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		// Gehege für Alpaka
		Alpaca alpaca = new Alpaca();
		AsciiImage alpacaEnclosure = new AsciiImage(10, 10);
		alpacaEnclosure.dot(5, 5, alpaca);
		System.out.println("=== Alpaca Gehege ===");
		System.out.println(alpacaEnclosure.toString());
		
		// Gehege für Pferd
		Horse horse = new Horse("Thunder", "black");
		AsciiImage horseEnclosure = new AsciiImage(10, 10);
		horseEnclosure.dot(3, 3, horse);
		System.out.println("=== Horse Gehege ===");
		System.out.println(horseEnclosure.toString());
		
		// Gehege für Elefant
		Elephant elephant = new Elephant();
		AsciiImage elephantEnclosure = new AsciiImage(10, 10);
		elephantEnclosure.dot(7, 7, elephant);
		System.out.println("=== Elephant Gehege ===");
		System.out.println(elephantEnclosure.toString());
		
		// Nutzer-Interaktion zum Füttern
		Scanner scanner = new Scanner(System.in);
		boolean keepFeeding = true;
		
		while (keepFeeding) {
			System.out.println("\n--- Welches Tier möchtest du füttern? ---");
			System.out.println("1. Alpaca");
			System.out.println("2. Horse (Thunder)");
			System.out.println("3. Elephant");
			System.out.println("4. Beenden");
			System.out.print("Deine Wahl (1-4): ");
			
			int choice = scanner.nextInt();
			scanner.nextLine(); // Zeilenumbruch lesen
			
			if (choice == 4) {
				keepFeeding = false;
				System.out.println("Auf Wiedersehen!");
			} else if (choice >= 1 && choice <= 3) {
				System.out.print("Was möchtest du dem Tier zu essen geben? ");
				String food = scanner.nextLine();
				
				switch (choice) {
					case 1:
						alpaca.eat(food);
						break;
					case 2:
						horse.eat(food);
						break;
					case 3:
						elephant.eat(food);
						break;
				}
			} else {
				System.out.println("Ungültige Eingabe!");
			}
		}
		
		scanner.close();
	}

}
