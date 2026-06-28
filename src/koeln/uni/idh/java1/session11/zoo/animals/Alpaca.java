package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.Weather;

public class Alpaca extends WalkingMammal {

    private static final Random random = new Random();

    public Alpaca(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public char getSymbol() {
        return 'A';
    }

    public void spit() {
        System.out.println(name + " das Alpaka spuckt beleidigt in die Gegend.");
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other != null && isAt(other.getX(), other.getY())) {
            if (random.nextInt(100) < 25) {
                spit();
            }
        }
    }

    @Override
    protected int getThirstIncrease(Weather weather) {
        if (weather == Weather.HEATWAVE) {
            return 6;
        }

        return super.getThirstIncrease(weather);
    }
}