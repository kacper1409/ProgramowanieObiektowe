package agh.cs.lab2;

public class World {

    static final int N = 10;

    public static void main(String[] args) {

//        MoveDirection[] directions = new MoveDirection[] {
//                MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.RIGHT };

//        RectangularMap map = new RectangularMap(5, 5);
//        map.place(new Animal(map, new Vector2d(3, 3)));
//        map.place(new Animal(map, new Vector2d(5, 3)));
//        map.run(directions);

        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(3, 5);
        map.place(new Animal(map));
        map.place(new Animal(map, new Vector2d(3,3)));
        map.place(new Animal(map, new Vector2d(3,4)));
        map.run(directions);
        System.out.print(map.toString());

//        RectangularMap map = new RectangularMap(10, 10);
//
//        Animal cat = new Animal(map, new Vector2d(0, 0));
//        Animal dog = new Animal(map, new Vector2d(2, 2));
//
//        //OptionsParser parser = new OptionsParser();
//        String[] testArray = new String[]{"f", "f", "f", "r", "f", "f", "f", "r", "b", "r", "b", "f", "f", "f", "f", "f", "f", "r", "b", "b", "b", "b", "b"};

//        for (MoveDirection direction : new OptionsParser().parse(testArray)) {
//            cat.move(direction);
//            System.out.println(cat);
//        }





    }




}


