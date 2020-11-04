package agh.cs.lab2;

import java.util.Random;

public class World {

    static final int N = 10;

    public static void main(String[] args) {

//        MoveDirection[] directions = new OptionsParser().parse(args);
//        IWorldMap map = new RectangularMap(3, 5);
//        map.place(new Animal(map));
//        map.place(new Animal(map, new Vector2d(3,3)));
//        map.place(new Animal(map, new Vector2d(3,4)));
//        map.run(directions);
//        System.out.print(map.toString());

        IWorldMap grassField = new GrassField(10);
        grassField.place(new Animal(grassField, new Vector2d(15, 16)));
        grassField.place(new Animal(grassField, new Vector2d(10, 20)));

        System.out.print(grassField.toString());

//        Random intGenerator = new Random();
//        for (int i = 0; i < 10; i++) {
//            System.out.println(intGenerator.nextInt(10));
//        }


    }
}


