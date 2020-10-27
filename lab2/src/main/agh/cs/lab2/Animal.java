package agh.cs.lab2;

import java.util.Vector;

public class Animal {

    public IWorldMap map;
    public Vector2d initialPosition;
    public MapDirection orientation;
    public Vector2d position;

    static final int MIN_X = 0;
    static final int MAX_X = 4;
    static final int MIN_Y = 0;
    static final int MAX_Y = 4;

    public Animal (IWorldMap map) {
        this.map = map;
        this.initialPosition = new Vector2d(0, 0);
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
    }

    public Animal (IWorldMap map, Vector2d initialPosition) {
        this.map = map;
        this.initialPosition = initialPosition;
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
    }




    public String toString() {
        return "orient: " + orientation + ", pos: x: " + position.x + ", y: " + position.y;
    }

    boolean inField(Vector2d currentPosition) {     // map from (0, 0) to (4, 4)

        Vector2d upperRightCorner = new Vector2d(MAX_X, MAX_Y);
        Vector2d lowerLeftCorner = new Vector2d(MIN_X, MIN_Y);

        if (    (currentPosition.upperRight(upperRightCorner).follows(upperRightCorner) && !(currentPosition.upperRight(upperRightCorner).equals(upperRightCorner)))  ||
                (currentPosition.lowerLeft(lowerLeftCorner).precedes(lowerLeftCorner) && !(currentPosition.lowerLeft(lowerLeftCorner).equals(lowerLeftCorner)))
        ) return false;

        return true;
    }

    public void move(MoveDirection direction) {
        if (direction == MoveDirection.RIGHT) {
            orientation = orientation.next();
        }

        else if (direction == MoveDirection.LEFT) {
            orientation = orientation.previous();
        }

        else if (direction == MoveDirection.FORWARD && inField(position.add(orientation.toUnitVector()))) {
            position = position.add(orientation.toUnitVector());
        }

        else if (direction == MoveDirection.BACKWARD && inField(position.add(orientation.toUnitVector().opposite()))) {
            position = position.add(orientation.toUnitVector().opposite());
        }
    }
}
