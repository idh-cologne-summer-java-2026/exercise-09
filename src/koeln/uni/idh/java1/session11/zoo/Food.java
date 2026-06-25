package koeln.uni.idh.java1.session11.zoo;

import koeln.uni.idh.java1.session11.zoo.ui.Drawable;

public class Food implements Drawable {

    private int x;
    private int y;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public char getSymbol() {
        return '*';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}