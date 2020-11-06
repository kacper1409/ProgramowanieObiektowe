// pakiet

import agh.cs.lab2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrassFieldTest {   // ubogie testy; można by sprawdzić, czy zwierzęta się prawidłowo przemieszczają po mapie
    /**
     * checks if grass is located specified range
     */
    @Test
    public void grassPlaceTest() {

        int grassPoints = 10;

        GrassField grassField = new GrassField(grassPoints);
        grassField.place(new Animal(grassField, new Vector2d(15, 16)));
        grassField.place(new Animal(grassField, new Vector2d(10, 20)));

        for (int i = 0; i < 10; i++) {
            assertTrue(grassField.getGrass(i).getPosition().precedes(new Vector2d(10, 10)));
        }
    }

    @Test
    public void moveTest() {

        int grassPoints = 10;

        GrassField grassField = new GrassField(grassPoints);
        grassField.place(new Animal(grassField, new Vector2d(30, 60)));
        for (int i = 0; i < 10; i++) {
            assertTrue(grassField.canMoveTo(grassField.getGrass(i).getPosition()));
        }
    }

}
