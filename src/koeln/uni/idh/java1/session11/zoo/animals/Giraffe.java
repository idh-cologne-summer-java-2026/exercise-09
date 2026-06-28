package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Weather;

public class Giraffe extends WalkingMammal {

    public Giraffe(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public char getSymbol() {
        return 'G';
    }

    @Override
    protected int getHungerIncrease() {
        return 2;
    }

    @Override
    protected int getThirstIncrease(Weather weather) {
        if (weather == Weather.HEATWAVE) {
            return 6;
        }

        return super.getThirstIncrease(weather);
    }

    public void eatLeaves() {
        if (!alive) {
            return;
        }

        hunger -= 40;

        if (hunger < 0) {
            hunger = 0;
        }

        System.out.println(name + " die Giraffe frisst Blätter von einem Baum.");
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other != null && isAt(other.getX(), other.getY())) {
            System.out.println(name + " schaut neugierig von oben auf " + other.getName() + ".");
        }
    }
}