package koeln.uni.idh.java1.session11.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.Lion;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

/**
 * A tiny zoo simulation: a handful of animals wander around a fenced field for
 * a number of time steps, while a few trees stay put. After every step the
 * whole field is printed as ASCII art.
 */
public class Main {

	private static final int WIDTH = 20;
	private static final int HEIGHT = 10;
	private static final int STEPS = 8;

	public static void main(String[] args) throws InterruptedException {

		// Random with a fixed seed => the "wandering" is reproducible. Change the
		// seed (or use "new Random()") if you want a different walk every time.
		Random random = new Random(42);

		// Our animals. They all share the WalkingMammal type, so we can keep them
		// in one list and treat them uniformly - that is the whole point of the
		// abstract base class.
		List<WalkingMammal> animals = new ArrayList<>();

		Alpaca alpaca = new Alpaca();
		alpaca.setName("Pedro");
		animals.add(alpaca);

		Elephant elephant = new Elephant();
		elephant.setName("Dumbo");
		animals.add(elephant);

		animals.add(new Lion("Simba"));

		// Give every animal a random starting position inside the field.
		for (WalkingMammal animal : animals) {
			animal.setX(random.nextInt(WIDTH));
			animal.setY(random.nextInt(HEIGHT));
		}

		// A few stationary trees. Tree is Drawable but NOT a WalkingMammal, which
		// is exactly why AsciiImage.dot now accepts Drawable instead of an animal.
		Tree[] trees = { new Tree(), new Tree(), new Tree() };
		int[][] treePositions = { { 3, 2 }, { 15, 7 }, { 9, 4 } };

		for (int step = 0; step <= STEPS; step++) {
			AsciiImage field = new AsciiImage(WIDTH, HEIGHT);

			// Draw the trees first (animals may walk "in front of" them).
			for (int t = 0; t < trees.length; t++) {
				field.dot(treePositions[t][0], treePositions[t][1], trees[t]);
			}

			// Draw every animal at its current position.
			for (WalkingMammal animal : animals) {
				field.dot(animal.getX(), animal.getY(), animal);
			}

			System.out.println("=== Step " + step + " ===");
			System.out.println(field.toString());

			// Let each animal turn a random way and take one step, then make sure
			// it did not leave the field.
			for (WalkingMammal animal : animals) {
				animal.turn(random.nextBoolean() ? 1 : -1);
				animal.walk();
				animal.keepInside(WIDTH, HEIGHT);
			}

			// Small pause so the steps are readable as a little "animation".
			Thread.sleep(200);
		}
	}

}
