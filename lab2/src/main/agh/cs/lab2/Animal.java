package agh.cs.lab2;

public class Animal {

    static final int MIN_X = 0;
    static final int MAX_X = 4;
    static final int MIN_Y = 0;
    static final int MAX_Y = 4;

    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position = new Vector2d(2, 2);

    public String toString() {
        return "orient: " + orientation + ", pos: x: " + position.x + ", y: " + position.y;
    }

    boolean inField(MoveDirection direction) {     // map from (0, 0) to (4, 4)

        if (direction == MoveDirection.FORWARD) {
            if (orientation == MapDirection.NORTH && position.y >= MAX_Y)
                return false;
            if (orientation == MapDirection.SOUTH && position.y <= MIN_Y)
                return false;
            if (orientation == MapDirection.EAST && position.x >= MAX_X)
                return false;
            if (orientation == MapDirection.WEST && position.x >= MIN_X)
                return false;
        }

        else if (direction == MoveDirection.BACKWARD) {
            if (orientation == MapDirection.SOUTH && position.y >= MAX_Y)
                return false;
            if (orientation == MapDirection.NORTH && position.y <= MIN_Y)
                return false;
            if (orientation == MapDirection.WEST && position.x >= MAX_X)
                return false;
            if (orientation == MapDirection.EAST && position.x >= MIN_X)
                return false;
        }


        return true;
    }

    public void move(MoveDirection direction) {
        if (direction == MoveDirection.RIGHT) {
            orientation = orientation.next();
        }

        else if (direction == MoveDirection.LEFT) {
            orientation = orientation.previous();
        }

        else if (direction == MoveDirection.FORWARD && inField(direction)) {
            position = position.add(orientation.toUnitVector());
        }

        else if (direction == MoveDirection.BACKWARD && inField(direction)) {
            position = position.add(orientation.toUnitVector().opposite());
        }
    }
}
