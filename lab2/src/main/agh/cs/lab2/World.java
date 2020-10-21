package agh.cs.lab2;

public class World {

    static final int N = 10;

    public static void main(String[] args) {
        Animal cat = new Animal();

        //OptionsParser parser = new OptionsParser();
        String[] testArray = new String[]{"b", "l", "forward", "r", "backward", "b", "b"};

        for (MoveDirection direction : new OptionsParser().parse(testArray)) {
            cat.move(direction);
            System.out.println(cat);
        }
    }
}

