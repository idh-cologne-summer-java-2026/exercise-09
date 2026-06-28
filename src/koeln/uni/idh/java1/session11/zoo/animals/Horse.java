package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;

public class Horse extends WalkingMammal {

    private static final Random random = new Random();

    private String furColor;

    public Horse(String name, int x, int y, String furColor) {
        super(name, x, y);
        this.furColor = furColor;
    }

    @Override
    public char getSymbol() {
        return 'H';
    }

    public Horse mate(Horse other) {
        if (other == null) {
            return null;
        }

        String childName = this.name + "-" + other.getName() + "-Junior";
        String childFurColor = mixFurColors(this.furColor, other.getFurColor());

        return new Horse(childName, this.x, this.y, childFurColor);
    }

    public String mixFurColors(String firstColor, String secondColor) {
        if (firstColor.equals(secondColor)) {
            return firstColor;
        }

        if (random.nextBoolean()) {
            return firstColor + "-" + secondColor;
        }

        return secondColor + "-" + firstColor;
    }

    @Override
    public void interact(WalkingMammal other) {
        super.interact(other);

        if (other instanceof Horse && isAt(other.getX(), other.getY())) {
            System.out.println(name + " wiehert freundlich zu " + other.getName() + ".");
        }
    }

    @Override
    protected int getHungerIncrease() {
        return 3;
    }

    public String getFurColor() {
        return furColor;
    }

    public void setFurColor(String furColor) {
        this.furColor = furColor;
    }
}