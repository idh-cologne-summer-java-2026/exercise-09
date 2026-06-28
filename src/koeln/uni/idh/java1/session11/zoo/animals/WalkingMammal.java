package koeln.uni.idh.java1.session11.zoo.animals;

import java.util.Random;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;
import koeln.uni.idh.java1.session11.zoo.Weather;

public abstract class WalkingMammal implements Drawable {

    private static final Random random = new Random();

    protected String name;

    protected int x;
    protected int y;

    protected int dx;
    protected int dy;

    protected int hunger;
    protected int thirst;
    protected int age;

    protected boolean alive;

    public WalkingMammal(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;

        this.dx = randomDirection();
        this.dy = randomDirection();

        this.hunger = 0;
        this.thirst = 0;
        this.age = 0;
        this.alive = true;
    }

    private int randomDirection() {
        return random.nextInt(3) - 1;
    }

    public void walk(int width, int height) {
        if (!alive) {
            return;
        }

        // Manchmal zufällig die Richtung ändern
        if (random.nextInt(100) < 30) {
            turn();
        }

        int nextX = x + dx;
        int nextY = y + dy;

        // Wenn das Tier den Rand erreicht, dreht es um
        if (nextX < 0 || nextX >= width) {
            dx = -dx;
            nextX = x + dx;
        }

        if (nextY < 0 || nextY >= height) {
            dy = -dy;
            nextY = y + dy;
        }

        // Falls dx und dy beide 0 sind, bleibt das Tier stehen.
        // Das ist okay, wirkt natürlicher.
        if (nextX >= 0 && nextX < width) {
            x = nextX;
        }

        if (nextY >= 0 && nextY < height) {
            y = nextY;
        }
    }

    public void turn() {
        dx = randomDirection();
        dy = randomDirection();
    }

    public void tick(Weather weather) {
        if (!alive) {
            return;
        }

        age++;

        hunger += getHungerIncrease();
        thirst += getThirstIncrease(weather);

        if (hunger > 100 || thirst > 100) {
            alive = false;
        }
    }

    protected int getHungerIncrease() {
        return 2;
    }

    protected int getThirstIncrease(Weather weather) {
        if (weather == Weather.HEATWAVE) {
            return 5;
        }

        if (weather == Weather.RAIN) {
            return 1;
        }

        return 3;
    }

    public void eat() {
        if (!alive) {
            return;
        }

        hunger -= 30;

        if (hunger < 0) {
            hunger = 0;
        }
    }

    public void drink() {
        if (!alive) {
            return;
        }

        thirst -= 30;

        if (thirst < 0) {
            thirst = 0;
        }
    }

    public void pet() {
        if (!alive) {
            return;
        }

        // Streicheln macht das Tier etwas entspannter:
        // Hunger und Durst sinken minimal.
        hunger -= 5;
        thirst -= 5;

        if (hunger < 0) {
            hunger = 0;
        }

        if (thirst < 0) {
            thirst = 0;
        }
    }

    public boolean isHungry() {
        return hunger >= 50;
    }

    public boolean isThirsty() {
        return thirst >= 50;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isAt(int otherX, int otherY) {
        return x == otherX && y == otherY;
    }

    public boolean isNear(int otherX, int otherY) {
        int distanceX = Math.abs(x - otherX);
        int distanceY = Math.abs(y - otherY);

        return distanceX <= 1 && distanceY <= 1;
    }

    public void interact(WalkingMammal other) {
        if (!alive || other == null || !other.isAlive()) {
            return;
        }

        if (this == other) {
            return;
        }

        if (isAt(other.getX(), other.getY())) {
            // Begegnung macht Tiere ein kleines bisschen aktiver.
            turn();
        }
    }

    public String getStatus() {
        String status = name
                + " (" + getClass().getSimpleName() + ")"
                + " | Position: (" + x + ", " + y + ")"
                + " | Hunger: " + hunger
                + " | Durst: " + thirst
                + " | Alter: " + age;

        if (!alive) {
            status += " | Status: nicht mehr aktiv";
        } else if (isHungry() && isThirsty()) {
            status += " | Status: hungrig und durstig";
        } else if (isHungry()) {
            status += " | Status: hungrig";
        } else if (isThirsty()) {
            status += " | Status: durstig";
        } else {
            status += " | Status: zufrieden";
        }

        return status;
    }

    public String getName() {
        return name;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public int getAge() {
        return age;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}