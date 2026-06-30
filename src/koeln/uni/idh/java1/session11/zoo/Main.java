package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Camel;
import koeln.uni.idh.java1.session11.zoo.animals.Giraffe;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.Monkey;
import koeln.uni.idh.java1.session11.zoo.animals.Sheep;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.animals.Zebra;
import koeln.uni.idh.java1.session11.zoo.areas.Habitat;
import koeln.uni.idh.java1.session11.zoo.areas.ZooMap;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		WalkingMammal alpaca = new Alpaca();
		WalkingMammal giraffe = new Giraffe("Mabel");
		WalkingMammal lion = new Lion("Simba");
		WalkingMammal zebra = new Zebra("Ziggy");
		WalkingMammal monkey = new Monkey("Bongo");
		WalkingMammal camel = new Camel("Sandy");
		WalkingMammal sheep = new Sheep("Woolly");
		AsciiImage ai = new AsciiImage(10, 10);
		Habitat savanna = new Habitat("Savanna", "grassland");
		Habitat jungle = new Habitat("Jungle", "rainforest");
		Habitat pasture = new Habitat("Pasture", "meadow");
		ZooMap zooMap = new ZooMap(3, 3);
		
		savanna.addAnimal(lion);
		savanna.addAnimal(zebra);
		savanna.addAnimal(giraffe);
		jungle.addAnimal(monkey);
		jungle.addAnimal(alpaca);
		pasture.addAnimal(sheep);
		pasture.addAnimal(camel);
		zooMap.addHabitat(savanna, 0, 0);
		zooMap.addHabitat(jungle, 1, 0);
		zooMap.addHabitat(pasture, 0, 1);
		
		ai.dot(2, 2, alpaca);
		ai.dot(5, 3, giraffe);
		ai.dot(7, 6, lion);
		ai.dot(4, 8, zebra);
		ai.dot(1, 7, monkey);
		ai.dot(8, 2, camel);
		ai.dot(6, 5, sheep);
		
		System.out.println("Zoo map:");
		System.out.println(zooMap);
		
		System.out.println("Seasonal reproduction:");
		alpaca.reproduce(Season.SPRING);
		lion.reproduce(Season.SPRING);
		zebra.reproduce(Season.AUTUMN);
		sheep.reproduce(Season.WINTER);
		
		Visitor visitor = new Visitor("Nina");
		visitor.feedAnimal(lion);
		visitor.feedAnimal(monkey);
		visitor.feedAnimal(sheep);
		
		System.out.println("Stats after feeding:");
		System.out.println("Lion hunger=" + lion.getHunger() + " thirst=" + lion.getThirst());
		System.out.println("Monkey hunger=" + monkey.getHunger() + " thirst=" + monkey.getThirst());
		System.out.println("Sheep hunger=" + sheep.getHunger() + " thirst=" + sheep.getThirst());
		
		System.out.println(ai.toString());
	}

}
