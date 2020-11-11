package agh.cs.lab2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {

    static final int N = 10;

    public static void main(String[] args) {
        try {
//            MoveDirection[] directions = new OptionsParser().parse(args); "java.lang.IllegalArgumentException: p is not legal move specification"
//            GrassField grassField = new GrassField(10);
//            grassField.place(new Animal(grassField, new Vector2d(2, 2)));
//            grassField.place(new Animal(grassField, new Vector2d(2, 2)));

            IWorldMap map = new RectangularMap(3, 5);
            Map<Vector2d,Animal> animals = new HashMap<>();
            animals.put(new Vector2d(2, 2), new Animal(map, new Vector2d(2, 2)));
            animals.put(new Vector2d(2, 3), new Animal(map, new Vector2d(2, 3)));
            map.place(new Animal(map, new Vector2d(2, 2)));

        } catch(IllegalArgumentException exception) {
            System.out.print(exception);
            return;



        }






        //        MoveDirection[] directions = new OptionsParser().parse(args);
//        IWorldMap map = new RectangularMap(3, 5);
//        map.place(new Animal(map));
//        map.place(new Animal(map, new Vector2d(3,3)));
//        map.place(new Animal(map, new Vector2d(3,4)));
//        map.run(directions);
//        System.out.print(map.toString());

//        IWorldMap grassField = new GrassField(10);
//        grassField.place(new Animal(grassField, new Vector2d(15, 16)));
//        grassField.place(new Animal(grassField, new Vector2d(10, 20)));
//
//        System.out.print(grassField.toString());

//        Random intGenerator = new Random();
//        for (int i = 0; i < 10; i++) {
//            System.out.println(intGenerator.nextInt(10));
//        }

//         ((RentangularMap)(map)).getAnimal();
    }
}


