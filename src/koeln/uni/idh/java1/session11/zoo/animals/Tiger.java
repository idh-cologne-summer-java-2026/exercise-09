package koeln.uni.idh.java1.session11.zoo.animals;

public class Tiger extends WalkingMammal {
	public Tiger(String name) {
		super(name);
		System.out.println("Ein Tiger namens " + name + " wird im Gehege freigelassen.");
	}

	@Override
	public char getSymbol() {
		return 'T';
	}

	@Override
	public void pet() {
		System.out.println(getName() + " beisst dem Nutzer die Hand ab!");
	}
}
