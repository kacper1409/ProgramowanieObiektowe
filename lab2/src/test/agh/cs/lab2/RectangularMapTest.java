package agh.cs.lab2;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularMapTest {
    @Test
    public void movingTest() {

        MoveDirection[] directions = new MoveDirection[] {
                MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.RIGHT, MoveDirection.LEFT };

        RectangularMap map = new RectangularMap(5, 5);
        map.place(new Animal(map, new Vector2d(3, 3)));
        map.place(new Animal(map, new Vector2d(5, 3)));
        map.run(directions);

        assertEquals(new Vector2d(3, 5), map.getAnimal(0).getPosition());
        assertEquals(new Vector2d(5, 5), map.getAnimal(1).getPosition());

        assertEquals(MapDirection.WEST, map.getAnimal(0).getOrientation());
        assertEquals(MapDirection.EAST, map.getAnimal(1).getOrientation());

    }

    @Test
    public void conflictTest() {

        MoveDirection[] directions = new MoveDirection[] {
                MoveDirection.RIGHT, MoveDirection.LEFT, MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.FORWARD };

        RectangularMap map = new RectangularMap(10, 5);
        map.place(new Animal(map, new Vector2d(5, 2)));
        map.place(new Animal(map, new Vector2d(7, 2)));
        map.place(new Animal(map, new Vector2d(9, 2)));

        map.run(directions);

        assertEquals(new Vector2d(6, 2), map.getAnimal(0).getPosition());
        assertEquals(new Vector2d(7, 2), map.getAnimal(1).getPosition());
        assertEquals(new Vector2d(8, 2), map.getAnimal(2).getPosition());

        assertEquals(MapDirection.EAST, map.getAnimal(0).getOrientation());
        assertEquals(MapDirection.EAST, map.getAnimal(1).getOrientation());
        assertEquals(MapDirection.WEST, map.getAnimal(2).getOrientation());


    }

}
