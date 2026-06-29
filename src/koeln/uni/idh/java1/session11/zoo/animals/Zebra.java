package koeln.uni.idh.java1.session11.zoo.animals;

public class Zebra extends WalkingMammal {

    static int numberOfZebras = 0;

    String name;

    public Zebra(String name) {
        numberOfZebras++;
        this.name = name;
        System.out.println("Zebra " + name + " has been born. There are now "
                + numberOfZebras + " zebras in the world.");
    }

    @Override
    public char getSymbol() {
        return 'Z';
    }
}