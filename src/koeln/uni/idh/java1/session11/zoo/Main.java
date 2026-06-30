package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.Cat;
import koeln.uni.idh.java1.session11.zoo.animals.Frog;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		WalkingMammal wm = new Alpaca();
		AsciiImage ai = new AsciiImage(10, 10);
		
		ai.dot(5,5,wm);
		
		Cat cat = new Cat("Milo");
		Frog frog = new Frog("Kermit");
		cat.tickHunger();
		cat.tickHunger();
		frog.tickHunger();
		
		System.out.println(ai.toString());
		System.out.println(cat.getName() + " is " + cat.getHungerStatus() + " (" + cat.getHunger() + "/100)");
		System.out.println(frog.getName() + " is " + frog.getHungerStatus() + " (" + frog.getHunger() + "/100)");
		cat.feed();
		System.out.println(cat.getName() + " has been fed. Hunger: " + cat.getHunger());
	}

}
