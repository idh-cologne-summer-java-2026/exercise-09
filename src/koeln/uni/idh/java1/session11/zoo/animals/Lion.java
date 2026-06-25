package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;

public class Lion extends WalkingMammal {

    private Random random = new Random();

    public Lion() {
        this.stepsize = 2;
    }

    @Override
    public char getSymbol() {
        return 'L';
    }

    // 🦁 Chance auszubrechen
    public boolean triesToEscape() {
        return random.nextInt(100) < 15; // 15% pro Tick
    }
}