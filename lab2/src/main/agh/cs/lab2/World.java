package agh.cs.lab2;

public class World {

    static final int N = 10;

    public static void main(String[] args) {
        Animal cat = new Animal();

        //OptionsParser parser = new OptionsParser();
        String[] testArray = new String[]{"f", "f", "f", "r", "f", "f", "f", "r", "b", "r", "b", "f", "f", "f", "f", "f", "f", "r", "b", "b", "b", "b", "b"};

        for (MoveDirection direction : new OptionsParser().parse(testArray)) {
            cat.move(direction);
            System.out.println(cat);
        }

        Math.abs();



    }




}


