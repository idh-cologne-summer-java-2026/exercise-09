package koeln.uni.idh.java1.session11.zoo.animals;

public class Lion extends WalkingMammal {

    public Lion(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public char getSymbol() {
        return 'L';
    }

    @Override
    public void walk(int width, int height) {
        if (!alive) {
            return;
        }

        /*
         * Löwen ruhen oft.
         * Deshalb bewegen sie sich nur ungefähr jede zweite Runde.
         */
        if (age % 2 == 1) {
            super.walk(width, height);
        }
    }

    @Override
    protected int getHungerIncrease() {
        return 4;
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other != null && isAt(other.getX(), other.getY())) {
            System.out.println(name + " der Löwe brüllt. " + other.getName() + " wirkt beeindruckt.");
        }
    }
}