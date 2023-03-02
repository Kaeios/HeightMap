package fr.guillaume.math;

public class IntVector2D implements Cloneable
{
    private int x;
    private int y;

    public IntVector2D(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public void subtract(final IntVector2D vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
    }

    public void add(final IntVector2D vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public IntVector2D multiply(final int value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(final IntVector2D vector2D) {
        return vector2D.getX() == this.x && vector2D.getY() == this.y;
    }

    public int getArea() {
        return this.x * this.y;
    }

    public IntVector2D clone() {
        return new IntVector2D(x, y);
    }

}
