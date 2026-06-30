package koeln.uni.idh.java1.session11.zoo.areas;

import java.util.ArrayList;
import java.util.List;

import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;

public class Habitat {
	private final String name;
	private final String biome;
	private final List<WalkingMammal> animals;
	private int x;
	private int y;

	public Habitat(String name, String biome) {
		this.name = name;
		this.biome = biome;
		this.animals = new ArrayList<>();
	}

	public void addAnimal(WalkingMammal animal) {
		animals.add(animal);
		animal.setHabitat(this);
	}

	public String getName() {
		return name;
	}

	public String getBiome() {
		return biome;
	}

	public List<WalkingMammal> getAnimals() {
		return animals;
	}

	public int getAnimalCount() {
		return animals.size();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return name + " (" + biome + ") at (" + x + ", " + y + ") with " + getAnimalCount() + " animal(s)";
	}
}
