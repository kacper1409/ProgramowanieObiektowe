package agh.cs.lab2;

import org.junit.Test;

import static org.junit.Assert.*;

public class Vector2dTest {
    @Test
    public void equalsTest() {
        assertEquals(new Vector2d(2, 1), new Vector2d(2, 1));
    }

    @Test
    public void toStringTest() {
        Vector2d vector = new Vector2d(3, 3);
        assertEquals("(3, 3)", vector.toString());
    }

    @Test
    public void precedesTest() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 4);

        assertTrue(v1.precedes(v2));
    }

    @Test
    public void followsTest() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(2, 3);

        assertTrue(v1.follows(v2));
    }

    @Test
    public void upperRightTest() {
        Vector2d v1 = new Vector2d(3, 3);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 3);

        assertEquals(v1.upperRight(v2), v3);
    }

    @Test
    public void lowerLeftTest() {
        Vector2d v1 = new Vector2d(1, 5);
        Vector2d v2 = new Vector2d(4, 2);
        Vector2d v3 = new Vector2d(1, 2);

        assertEquals(v1.lowerLeft(v2), v3);
    }

    @Test
    public void addTest() {
        Vector2d v1 = new Vector2d(3, 3);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(5, 6);

        assertEquals(v1.add(v2), v3);
    }

    @Test
    public void subtractTest() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(3, 3);
        Vector2d v3 = new Vector2d(-1, 0);

        assertEquals(v1.subtract(v2), v3);
    }

    @Test
    public void oppositeTest() {
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(-2, -3);

        assertEquals(v1.opposite(), v2);
    }

}
