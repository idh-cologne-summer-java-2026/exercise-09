package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Elephant;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.animals.Zebra;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		WalkingMammal wm = new Zebra();
		AsciiImage ai = new AsciiImage(10, 10);
		
		ai.dot(5,4,wm);
		
	

		WalkingMammal wm2 = new Elephant();
		wm2.walk();
		wm2.drinkWater();
		ai.dot(5,5,wm2);

		wm2.walk();
	
	System.out.println(ai.toString());
	}

}
