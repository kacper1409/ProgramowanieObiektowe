package agh.cs.lab8.util;


public class Vector2d {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {   // czy ta metoda jest bezpieczna?
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        int maxX = Math.max(this.x, other.x);
        int maxY = Math.max(this.y, other.y);
        return new Vector2d(maxX, maxY);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int minX = Math.min(this.x, other.x);
        int minY = Math.min(this.y, other.y);
        return new Vector2d(minX, minY);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public Vector2d getUnitMotion(int direction) {  // kierunek jako int? Czy to na pewno jest właściwa klasa dla tej metody?
        switch (direction) {
            case 0:
                return new Vector2d(0, 1);
            case 1:
                return new Vector2d(1, 1);
            case 2:
                return new Vector2d(1, 0);
            case 3:
                return new Vector2d(1, -1);
            case 4:
                return new Vector2d(0, -1);
            case 5:
                return new Vector2d(-1, -1);
            case 6:
                return new Vector2d(-1, 0);
            case 7:
                return new Vector2d(-1, 1);
        }
        return null;
    }

    public void normalizePosition(int mapWidth, int mapHeight) {
        if (getX() >= 0)
            setX(getX() % mapWidth);
        else {
            while (getX() < 0) {
                setX(getX() + mapWidth);
            }
        }

        if (getY() >= 0)
            setY(getY() % mapHeight);
        else {
            while (getY() < 0) {
                setY(getY() + mapHeight);
            }
        }
    }
}