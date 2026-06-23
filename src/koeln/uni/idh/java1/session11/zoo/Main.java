package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.animals.Alpaca;
import koeln.uni.idh.java1.session11.zoo.animals.WalkingMammal;
import koeln.uni.idh.java1.session11.zoo.ui.AsciiImage;

public class Main {

	public static void main(String[] args) {
		WalkingMammal wm = new Alpaca();
		AsciiImage ai = new AsciiImage(10, 10);
		
		ai.dot(5,5,wm);
		
		System.out.println(ai.toString());
	}

}
