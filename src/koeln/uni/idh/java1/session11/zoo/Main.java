package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Flamingo;
import koeln.uni.idh.java1.session11.zoo.animals.Koala;

public class Main {

	public static void main(String[] args) {
		// Create animals
		Alpaca alpaca = new Alpaca("Lama");
		Elephant elephant = new Elephant("Dumbo");
		Flamingo flamingo = new Flamingo("Fiona");
		Koala koala = new Koala("Kobe");
		
		// Create caretakers
		Person keeper1 = new Person("Alice", "Senior Keeper");
		Person keeper2 = new Person("Bob", "Junior Keeper");
		Person keeper3 = new Person("Charlie", "Keeper");
		Person keeper4 = new Person("Diana", "Keeper");
		Person keeper5 = new Person("Eve", "Senior Keeper");
		Person keeper6 = new Person("Frank", "Junior Keeper");
		
		// Create enclosures with 2 caretakers per animal
		Enclosure alpacaEnclosure = new Enclosure("Alpaca Valley", keeper1, keeper2, alpaca);
		Enclosure elephantEnclosure = new Enclosure("Elephant House", keeper3, keeper4, elephant);
		Enclosure flamingoEnclosure = new Enclosure("Flamingo Lagoon", keeper5, keeper6, flamingo);
		
		// Create and setup the zoo
		Zoo zoo = new Zoo(25, 20);
		zoo.addEnclosure(alpacaEnclosure);
		zoo.addEnclosure(elephantEnclosure);
		zoo.addEnclosure(flamingoEnclosure);
		
		// Set initial positions
		alpaca.setX(3);
		alpaca.setY(3);
		elephant.setX(15);
		elephant.setY(10);
		flamingo.setX(20);
		flamingo.setY(15);
		
		// Run the simulation for 5 days
		zoo.simulate(5);
		
		System.out.println("✅ Zoo simulation completed successfully!");
	}

}
