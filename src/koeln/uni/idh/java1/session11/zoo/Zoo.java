package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.animals.Rabbit;
import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

/**
 * Zoo Simulator that manages animals and trees on a field.
 * Handles breeding, movement, and disease simulation.
 */
public class Zoo {
	
	private int width;
	private int height;
	private List<Rabbit> rabbits;
	private List<Tree> trees;
	private Random random;
	private Drawable[][] field; // Track what's on each field
	
	/**
	 * Creates a new Zoo with the given dimensions
	 * @param width The width of the zoo field
	 * @param height The height of the zoo field
	 */
	public Zoo(int width, int height) {
		this.width = width;
		this.height = height;
		this.rabbits = new ArrayList<>();
		this.trees = new ArrayList<>();
		this.random = new Random();
		this.field = new Drawable[height][width];
		
		initializeZoo();
	}
	
	/**
	 * Initializes the zoo with 2 male and 2 female rabbits
	 */
	private void initializeZoo() {
		// Create 2 male rabbits
		for (int i = 0; i < 2; i++) {
			Rabbit rabbit = new Rabbit(true);
			placeRabbitOnFreeField(rabbit);
			rabbits.add(rabbit);
		}
		
		// Create 2 female rabbits
		for (int i = 0; i < 2; i++) {
			Rabbit rabbit = new Rabbit(false);
			placeRabbitOnFreeField(rabbit);
			rabbits.add(rabbit);
		}
		
		System.out.println("Zoo initialized with " + rabbits.size() + " rabbits on a " + width + "x" + height + " field.");
	}
	
	/**
	 * Places a rabbit on a free field
	 * @param rabbit The rabbit to place
	 */
	private void placeRabbitOnFreeField(Rabbit rabbit) {
		int x, y;
		do {
			x = random.nextInt(width);
			y = random.nextInt(height);
		} while (field[y][x] != null);
		
		rabbit.setX(x);
		rabbit.setY(y);
		field[y][x] = rabbit;
		System.out.println(rabbit.name + " placed at (" + x + ", " + y + ")");
	}
	
	/**
	 * Simulates one step of the zoo
	 * - Moves rabbits randomly
	 * - Checks for breeding
	 * - Checks for disease outbreak
	 */
	public void simulateStep() {
		// Move rabbits randomly
		for (Rabbit rabbit : new ArrayList<>(rabbits)) {
			moveRabbitRandomly(rabbit);
		}
		
		// Check for breeding
		checkForBreeding();
		
		// Check if 70% of fields are occupied
		checkForDiseaseOutbreak();
	}
	
	/**
	 * Moves a rabbit in a random direction
	 * @param rabbit The rabbit to move
	 */
	private void moveRabbitRandomly(Rabbit rabbit) {
		// Random new position
		int newX = rabbit.getX() + random.nextInt(3) - 1; // -1, 0, or 1
		int newY = rabbit.getY() + random.nextInt(3) - 1; // -1, 0, or 1
		
		// Check bounds
		if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
			return;
		}
		
		// Check if field is free
		if (field[newY][newX] == null) {
			field[rabbit.getY()][rabbit.getX()] = null;
			rabbit.setX(newX);
			rabbit.setY(newY);
			field[newY][newX] = rabbit;
		}
	}
	
	/**
	 * Checks for breeding opportunities
	 * If a male and female rabbit are on adjacent fields,
	 * the female becomes pregnant and gives birth to 6 babies
	 */
	private void checkForBreeding() {
		List<Rabbit> femalesGivingBirth = new ArrayList<>();
		
		for (Rabbit female : rabbits) {
			if (!female.isFemale() || female.isPregnant()) {
				continue;
			}
			
			// Check for adjacent male rabbits
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					if (dx == 0 && dy == 0) continue;
					
					int checkX = female.getX() + dx;
					int checkY = female.getY() + dy;
					
					if (checkX >= 0 && checkX < width && checkY >= 0 && checkY < height) {
						Drawable obj = field[checkY][checkX];
						if (obj instanceof Rabbit) {
							Rabbit neighbor = (Rabbit) obj;
							if (neighbor.isMale()) {
								female.setPregnant(true);
								femalesGivingBirth.add(female);
								break;
							}
						}
					}
				}
				if (female.isPregnant()) break;
			}
		}
		
		// Birth process - create 6 new rabbits per pregnant female
		for (Rabbit mother : femalesGivingBirth) {
			for (int i = 0; i < 6; i++) {
				Rabbit baby = new Rabbit(); // Random sex
				if (placeRabbitOnFreeFieldIfPossible(baby)) {
					rabbits.add(baby);
				}
			}
			mother.setPregnant(false);
		}
	}
	
	/**
	 * Tries to place a rabbit on a free field
	 * @param rabbit The rabbit to place
	 * @return true if successful, false if no free field available
	 */
	private boolean placeRabbitOnFreeFieldIfPossible(Rabbit rabbit) {
		int attempts = width * height; // Max attempts
		while (attempts > 0) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			
			if (field[y][x] == null) {
				rabbit.setX(x);
				rabbit.setY(y);
				field[y][x] = rabbit;
				return true;
			}
			attempts--;
		}
		return false;
	}
	
	/**
	 * Calculates the occupancy percentage of the field
	 * @return The percentage of occupied fields (0-100)
	 */
	private double getOccupancyPercentage() {
		int totalFields = width * height;
		int occupiedFields = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (field[y][x] != null) {
					occupiedFields++;
				}
			}
		}
		
		return (double) occupiedFields / totalFields * 100;
	}
	
	/**
	 * Checks if the field is at least 70% occupied
	 * If so, 80% of all rabbits die from myxomatosis
	 */
	private void checkForDiseaseOutbreak() {
		double occupancy = getOccupancyPercentage();
		
		if (occupancy >= 70) {
			System.out.println("\n--- DISEASE OUTBREAK ---");
			System.out.println("Field occupancy: " + String.format("%.1f", occupancy) + "%");
			
			int rabbitsBefore = rabbits.size();
			int rabbitsToKill = (int) Math.ceil(rabbitsBefore * 0.8);
			
			// Kill 80% of rabbits randomly
			List<Rabbit> rabbitsCopy = new ArrayList<>(rabbits);
			for (int i = 0; i < rabbitsToKill && !rabbitsCopy.isEmpty(); i++) {
				int index = random.nextInt(rabbitsCopy.size());
				Rabbit deadRabbit = rabbitsCopy.remove(index);
				
				// Remove from field
				field[deadRabbit.getY()][deadRabbit.getX()] = null;
				rabbits.remove(deadRabbit);
			}
			
			System.out.println("80% of all rabbits died from myxomatosis!");
			System.out.println("Rabbits before: " + rabbitsBefore + ", Rabbits after: " + rabbits.size());
			System.out.println("--- END OF OUTBREAK ---\n");
		}
	}
	
	/**
	 * Renders the zoo as ASCII art
	 * @return A string representation of the zoo
	 */
	public String render() {
		StringBuilder sb = new StringBuilder();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (field[y][x] == null) {
					sb.append('.');
				} else {
					sb.append(field[y][x].getSymbol());
				}
			}
			sb.append('\n');
		}
		
		sb.append("Rabbits: ").append(rabbits.size()).append("\n");
		return sb.toString();
	}
	
	/**
	 * Gets the number of rabbits
	 * @return The number of rabbits in the zoo
	 */
	public int getRabbitCount() {
		return rabbits.size();
	}
	
	/**
	 * Gets the list of rabbits
	 * @return The list of rabbits
	 */
	public List<Rabbit> getRabbits() {
		return new ArrayList<>(rabbits);
	}
}
