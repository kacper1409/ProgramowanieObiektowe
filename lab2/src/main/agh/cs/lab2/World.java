package agh.cs.lab2;

public class World {
    public static void main(String[] args) {
        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        System.out.println(MapDirection.SOUTH.toString());
        System.out.println(MapDirection.NORTH.next());
        System.out.println(MapDirection.EAST.previous());
        System.out.println(MapDirection.WEST.toUnitVector());
    }

}

