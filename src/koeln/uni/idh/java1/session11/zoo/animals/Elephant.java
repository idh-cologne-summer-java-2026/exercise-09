package koeln.uni.idh.java1.session11.zoo.animals;

import koeln.uni.idh.java1.session11.zoo.Weather;

public class Elephant extends WalkingMammal {

    public Elephant(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public char getSymbol() {
        return 'E';
    }

    @Override
    public void drink() {
        if (!alive) {
            return;
        }

        thirst -= 45;

        if (thirst < 0) {
            thirst = 0;
        }

        System.out.println(name + " der Elefant trinkt eine große Menge Wasser.");
    }

    @Override
    protected int getThirstIncrease(Weather weather) {
        if (weather == Weather.HEATWAVE) {
            return 7;
        }

        return 4;
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other != null && isAt(other.getX(), other.getY())) {
            System.out.println(name + " trompetet freundlich.");
        }
    }
}