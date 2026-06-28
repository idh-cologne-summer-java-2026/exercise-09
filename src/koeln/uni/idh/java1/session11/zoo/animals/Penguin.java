package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Weather;

public class Penguin extends WalkingMammal {

    public Penguin(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public void walk(int width, int height) {
        if (!alive) {
            return;
        }

        /*
         * Pinguine watscheln langsamer:
         * Sie bewegen sich nicht in jeder Runde.
         */
        if (age % 2 == 0) {
            super.walk(width, height);
        }
    }

    @Override
    protected int getThirstIncrease(Weather weather) {
        if (weather == Weather.HEATWAVE) {
            return 8;
        }

        if (weather == Weather.RAIN) {
            return 1;
        }

        return 4;
    }

    public void coolDown() {
        if (!alive) {
            return;
        }

        thirst -= 40;

        if (thirst < 0) {
            thirst = 0;
        }

        System.out.println(name + " der Pinguin kühlt sich am Wasser ab.");
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other != null && isAt(other.getX(), other.getY())) {
            System.out.println(name + " watschelt neugierig um " + other.getName() + " herum.");
        }
    }
}