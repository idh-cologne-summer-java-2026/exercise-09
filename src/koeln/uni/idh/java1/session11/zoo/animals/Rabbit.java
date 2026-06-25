package koeln.uni.idh.java1.session11.zoo.animals;

/**
 * This class represents a rabbit (Kaninchen).
 * Male rabbits are represented by 'R', female rabbits by 'r'.
 * Rabbits can move around and reproduce.
 * 
 * @author Exercise
 */
public class Rabbit extends WalkingMammal {
	
	/**
	 * True if the rabbit is male, false if female
	 */
	private boolean isMale;
	
	/**
	 * True if the female rabbit is pregnant
	 */
	private boolean isPregnant = false;
	
	/**
	 * Creates a new rabbit with a random sex
	 */
	public Rabbit() {
		this.isMale = Math.random() < 0.5;
		this.name = "Rabbit_" + (isMale ? "M" : "F");
		System.out.println("A new rabbit has been born: " + this.name);
	}
	
	/**
	 * Creates a new rabbit with a specified sex
	 * @param isMale true for male, false for female
	 */
	public Rabbit(boolean isMale) {
		this.isMale = isMale;
		this.name = "Rabbit_" + (isMale ? "M" : "F");
		System.out.println("A new rabbit has been born: " + this.name);
	}

	@Override
	public char getSymbol() {
		return isMale ? 'R' : 'r';
	}
	
	public boolean isMale() {
		return isMale;
	}
	
	public boolean isFemale() {
		return !isMale;
	}
	
	public boolean isPregnant() {
		return isPregnant;
	}
	
	public void setPregnant(boolean pregnant) {
		this.isPregnant = pregnant;
		if (pregnant) {
			System.out.println("Rabbit " + this.name + " is now pregnant and will give birth to 6 babies!");
		}
	}
}
