package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Tiger;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class Main {

	/*Kommentar: Leider kann ich Claude in EClipse nicht benutzen, da die Verbindung zwischen EClipse und Claude nur mit einem 
	 * Max oder Pro ABo möglich ist*/
	
	// Mit GitHub Copilot hat es dann funktioniert. 
	public static void main(String[] args) {
		// Create the zoo with separate areas
		Zoo zoo = new Zoo(10, 10);
		
		// Create animals
		WalkingMammal alpaca = new Alpaca();
		Tiger tiger = new Tiger("Joris", 11);
		
		// Create a bathing lake for the animals
		BathingLake lake = new BathingLake("Crystal Lake");
		
		// Create visitors for the zoo
		Visitor visitor1 = new Visitor("Anna", 25);
		Visitor visitor2 = new Visitor("Bob", 8);
		
		// Place animals in the animal area
		zoo.placeAnimal(5, 7, alpaca);
		zoo.placeAnimal(3, 8, tiger);
		
		// Place the bathing lake in the animal area
		zoo.placeInAnimalArea(7, 3, lake);
		
		// Place visitors in the visitor area
		zoo.placeVisitor(1, 2, visitor1);
		zoo.placeVisitor(8, 9, visitor2);

		// Interactions
		tiger.roar();
		visitor1.viewAnimal("Tiger");
		visitor2.viewAnimal("Alpaca");
		
		// Animals bathe in the lake
		System.out.println("\n--- Animals cool off at the lake ---");
		lake.letAnimalBathe("Alpaca");
		lake.letAnimalBathe("Tiger");
		
		// Tiger jumps into the visitor area
		System.out.println("\n--- Tiger attacks! ---");
		zoo.tigerJumps(tiger, 5, 5);
		
		// Display the zoo
		System.out.println(zoo.toString());
	}

}
