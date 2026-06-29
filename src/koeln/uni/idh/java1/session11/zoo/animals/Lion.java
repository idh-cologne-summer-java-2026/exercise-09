package koeln.uni.idh.java1.session11.zoo.animals;

public class Lion extends WalkingMammal {

    static int numberOfLions = 0;

    String name;

    public Lion(String name) {
        numberOfLions++;
        this.name = name;
        System.out.println("Lion " + name + " has been born. There are now "
                + numberOfLions + " lions in the world.");
    }

    @Override
    public char getSymbol() {
        return 'L';
    }
}