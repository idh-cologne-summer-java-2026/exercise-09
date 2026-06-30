package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Food;
import koeln.uni.idh.java1.session11.zoo.animals.Horse;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Kangaroo;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		// Create animals
		Horse horse = new Horse("Thunder", "brown");
		Alpaca alpaca = new Alpaca();
		alpaca.setName("Fuzzy");
		Elephant elephant = new Elephant();
		elephant.setName("Dumbo");
		Kangaroo kangaroo = new Kangaroo();
		kangaroo.setName("Skippy");
		
		// Display feeding information for all animals
		System.out.println("\n🐾 === ZOO FEEDING SYSTEM ===\n");
		horse.displayFeedingInfo();
		alpaca.displayFeedingInfo();
		elephant.displayFeedingInfo();
		kangaroo.displayFeedingInfo();
		
		// Display initial needs status
		System.out.println("\n--- Initial Status ---");
		System.out.println(horse.getNeedsStatus());
		System.out.println(alpaca.getNeedsStatus());
		System.out.println(elephant.getNeedsStatus());
		System.out.println(kangaroo.getNeedsStatus());
		
		// Check hunger status - should trigger notifications if hungry enough
		System.out.println("\n--- Checking Health Status ---");
		horse.checkHungerStatus();
		alpaca.checkHungerStatus();
		elephant.checkHungerStatus();
		kangaroo.checkHungerStatus();
		
		// Display detailed food information
		System.out.println("\n--- Available Food Types ---");
		System.out.println(Food.GRAIN);
		System.out.println("\n" + Food.GRASS);
		System.out.println("\n" + Food.VEGETABLES);
		
		// Feed the animals
		System.out.println("\n--- Feeding Time ---");
		horse.feed(Food.GRAIN);
		alpaca.feed(Food.GRASS);
		elephant.feed(Food.VEGETABLES);
		kangaroo.feed(Food.GRASS);
		
		// Check hunger again after feeding
		System.out.println("\n--- After Feeding ---");
		horse.checkHungerStatus();
		alpaca.checkHungerStatus();
		elephant.checkHungerStatus();
		kangaroo.checkHungerStatus();
		
		// Let them drink
		System.out.println("\n--- Drinking Time ---");
		horse.drink();
		alpaca.drink();
		elephant.drink();
		kangaroo.drink();
		
		// Show updated status
		System.out.println("\n--- Status After Feeding and Drinking ---");
		System.out.println(horse.getNeedsStatus());
		System.out.println(alpaca.getNeedsStatus());
		System.out.println(elephant.getNeedsStatus());
		System.out.println(kangaroo.getNeedsStatus());
		
		// Demonstrate kangaroo's special hopping ability
		System.out.println("\n--- Kangaroo's Special Ability ---");
		kangaroo.setX(2);
		kangaroo.setY(2);
		System.out.println("Kangaroo starting position: X=" + kangaroo.getX() + ", Y=" + kangaroo.getY());
		kangaroo.turn(1); // Turn right
		kangaroo.hop();
		kangaroo.hop();
		System.out.println("After 2 hops: X=" + kangaroo.getX() + ", Y=" + kangaroo.getY());
		
		// Simulate passage of time
		System.out.println("\n--- After 2 seconds of simulated time ---");
		try {
			Thread.sleep(2000); // Sleep for 2 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		horse.updateNeeds();
		alpaca.updateNeeds();
		elephant.updateNeeds();
		kangaroo.updateNeeds();
		
		System.out.println(horse.getNeedsStatus());
		System.out.println(alpaca.getNeedsStatus());
		System.out.println(elephant.getNeedsStatus());
		System.out.println(kangaroo.getNeedsStatus());
		
		// Check hunger status again
		System.out.println("\n--- Health Check After Time Passage ---");
		horse.checkHungerStatus();
		alpaca.checkHungerStatus();
		elephant.checkHungerStatus();
		kangaroo.checkHungerStatus();
		
		// Demonstrate the ASCII visualization
		System.out.println("\n--- Zoo Visualization ---");
		horse.setX(2);
		horse.setY(2);
		alpaca.setX(5);
		alpaca.setY(5);
		elephant.setX(8);
		elephant.setY(8);
		kangaroo.setX(4);
		kangaroo.setY(8);
		
		WalkingMammal[] animals = {horse, alpaca, elephant, kangaroo};
		AsciiImage ai = new AsciiImage(10, 10);
		
		for (WalkingMammal animal : animals) {
			ai.dot(animal.getX(), animal.getY(), animal);
		}
		
		System.out.println(ai.toString());
		
		// Final summary
		System.out.println("\n🐾 === FINAL SUMMARY ===");
		horse.displayFeedingInfo();
		alpaca.displayFeedingInfo();
		elephant.displayFeedingInfo();
		kangaroo.displayFeedingInfo();
	}

}
