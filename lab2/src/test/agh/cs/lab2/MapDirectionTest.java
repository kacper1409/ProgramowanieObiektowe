package agh.cs.lab2;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapDirectionTest {
    @Test
    public void nextTest() {
        assertTrue(MapDirection.NORTH == MapDirection.WEST.next());
        assertTrue(MapDirection.EAST == MapDirection.NORTH.next());
        assertTrue(MapDirection.SOUTH == MapDirection.EAST.next());
        assertTrue(MapDirection.WEST == MapDirection.SOUTH.next());
    }

    @Test
    public void previousTest() {
        assertTrue(MapDirection.NORTH == MapDirection.EAST.previous());
        assertTrue(MapDirection.WEST == MapDirection.NORTH.previous());
        assertTrue(MapDirection.SOUTH == MapDirection.WEST.previous());
        assertTrue(MapDirection.EAST == MapDirection.SOUTH.previous());

        assertFalse(MapDirection.EAST == MapDirection.EAST.next());
    }




}
