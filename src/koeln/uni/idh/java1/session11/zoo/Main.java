package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Giraffe;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;

public class Main {
	public static void main(String[] args) {
		Zoo zoo = new Zoo("OOP Safari Park");

		Enclosure savanna = new Enclosure("Savanne", 1, 1, 22, 8);
		savanna.addAnimal(new Elephant("Ella"), 2, 2);
		savanna.addAnimal(new Giraffe("Gisela"), 8, 3);
		savanna.addAnimal(new Lion("Loki"), 15, 4);

		Enclosure meadow = new Enclosure("Streichelwiese", 26, 1, 22, 8);
		meadow.addAnimal(new Alpaca("Alfi"), 2, 2);
		Horse bella = new Horse("Bella", "brown");
		Horse max = new Horse("Max", "white");
		meadow.addAnimal(bella, 8, 3);
		meadow.addAnimal(max.mate(bella), 13, 5);

		zoo.addEnclosure(savanna);
		zoo.addEnclosure(meadow);
		zoo.addTree(new Tree(4, 12));
		zoo.addTree(new Tree(9, 14));
		zoo.addTree(new Tree(38, 13));

		System.out.println("=== Morgens im Zoo ===");
		System.out.println(zoo.draw());
		System.out.println(zoo.statusReport());

		zoo.setHeatWave(true);
		zoo.simulateDay();
		zoo.feedAllAnimals();

		System.out.println("=== Nach Hitzewelle und Fuetterung ===");
		System.out.println(zoo.draw());
		System.out.println(zoo.statusReport());
	}
}
