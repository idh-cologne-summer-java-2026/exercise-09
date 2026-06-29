package koeln.uni.idh.java1.session11.zoo.animals;

public class Tiger extends WalkingMammal {

    static int numberOfTigers = 0;

    String name;

    public Tiger(String name) {
        numberOfTigers++;
        this.name = name;
        System.out.println("Tiger " + name + " has been born. There are now "
                + numberOfTigers + " tigers in the world.");
    }

    @Override
    public char getSymbol() {
        return 'T';
    }
}