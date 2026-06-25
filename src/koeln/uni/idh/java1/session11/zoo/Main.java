package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Rabbit;

/**
 * The main class for the Rabbit Zoo Simulator.
 */

/* Use this project.
Please create a new mammal species: the rabbit. 
Male rabbits are represented by the symbol R, and female rabbits by the symbol r. 
A field can be occupied by only one animal or one tree.
Place two male rabbits and two female rabbits on free fields within the area. 
The rabbits can move around.
If a male rabbit and a female rabbit are on adjacent fields, the female rabbit becomes pregnant and gives birth to six new rabbits. 
The sex of each newborn rabbit is determined randomly. 
The newborn rabbits are placed randomly on free fields.
If 70% of all fields are occupied, 80% of all rabbits die randomly. 
Then print the following message:
"80% of all rabbits died from myxomatosis!"
 */
public class Main {

	public static void main(String[] args) {
		System.out.println("=== Rabbit Zoo Simulator ===\n");
		
		// Create a 15x15 zoo field
		Zoo zoo = new Zoo(15, 15);
		
		System.out.println("\nInitial state:");
		System.out.println(zoo.render());
		
		// Simulate 30 steps
		for (int step = 1; step <= 30; step++) {
			System.out.println("--- Step " + step + " ---");
			zoo.simulateStep();
			System.out.println(zoo.render());
			
			// Add a small delay for readability
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("=== Simulation Complete ===");
		System.out.println("Final rabbit count: " + zoo.getRabbitCount());
	}

}
