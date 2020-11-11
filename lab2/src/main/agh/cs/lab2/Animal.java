package agh.cs.lab2;

import java.util.Vector;

public class Animal {

    private IWorldMap map; // private
    private Vector2d initialPosition;
    private MapDirection orientation;
    private Vector2d position;


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

    public MapDirection getOrientation() { return orientation; }

    public Vector2d getPosition() { return position; }

    public String toString() {
        //return "orient: " + orientation + ", pos: x: " + position.x + ", y: " + position.y;
        switch (orientation) {
            case NORTH: return "N";
            case SOUTH: return "S";
            case EAST: return "E";
            case WEST: return "W";
        }
        return null;
    }

    public String positionToString() {
        return "x: " + position.x + "; y: " + position.y;
    }

//    boolean inField(Vector2d currentPosition) {     // map from (0, 0) to (4, 4)
//
//        Vector2d upperRightCorner = new Vector2d(MAX_X, MAX_Y);
//        Vector2d lowerLeftCorner = new Vector2d(MIN_X, MIN_Y);
//
//        if (    (currentPosition.upperRight(upperRightCorner).follows(upperRightCorner) && !(currentPosition.upperRight(upperRightCorner).equals(upperRightCorner)))  ||
//                (currentPosition.lowerLeft(lowerLeftCorner).precedes(lowerLeftCorner) && !(currentPosition.lowerLeft(lowerLeftCorner).equals(lowerLeftCorner)))
//        ) return false;
//
//        return true;
//    }

    public void move(MoveDirection direction) { // ultimately implements canMoveTo method from IWorldMap interface
        if (direction == MoveDirection.RIGHT) {
            orientation = orientation.next();
        }

        else if (direction == MoveDirection.LEFT) {
            orientation = orientation.previous();
        }

        else if (direction == MoveDirection.FORWARD && map.canMoveTo(position.add(orientation.toUnitVector()))) {     // inField(position.add(orientation.toUnitVector()))) {
            position = position.add(orientation.toUnitVector());
        }

        else if (direction == MoveDirection.BACKWARD && map.canMoveTo(position.add(orientation.toUnitVector().opposite()))) {   // inField(position.add(orientation.toUnitVector().opposite())))
            position = position.add(orientation.toUnitVector().opposite());
        }
    }


}
