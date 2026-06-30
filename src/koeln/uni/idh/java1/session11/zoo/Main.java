package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		Clock clock = new Clock();

		WalkingMammal alpaca = new Alpaca();
		WalkingMammal elephant = new Elephant();

		// Tiere bei der Uhr anmelden, damit sie über Zeitänderungen
		// informiert werden
		clock.register(alpaca);
		clock.register(elephant);

		AsciiImage ai = new AsciiImage(10, 10);
		ai.dot(5, 5, alpaca);
		ai.dot(2, 2, elephant);

		System.out.println(ai.toString());

		// Simuliere 20 Stunden, eine "Stunde" pro Schleifendurchlauf
		for (int i = 0; i < 20; i++) {
			clock.tick();

			if (!alpaca.isAsleep()) {
				alpaca.walk();
			}
		}

		System.out.println("\nFinaler Zustand der Uhr: " + clock.getHour() + " Uhr, "
				+ (clock.isDay() ? "Tag" : "Nacht"));
	}
}