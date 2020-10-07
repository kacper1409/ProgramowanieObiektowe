package agh.cs.lab1;

import java.util.Arrays;

public class World {
    public static void main(String[] args) {
        System.out.println("start");

        Arrays.stream(args)
                .map(str -> Direction.valueOf(str.toUpperCase()))
                .forEach(direction -> {
                    if (direction == Direction.F)
                        System.out.println("zwierzak idzie do przodu");
                    else if (direction== Direction.B)
                        System.out.println("zwierzak idzie do tyłu");
                    else if (direction == Direction.R)
                        System.out.println("zwierzak idzie W prawo");
                    else if (direction == Direction.L)
                        System.out.println("zwierzak idzie w lewo");
                });

//        Direction[] directions = new Direction[args.length];
//
//        for (int i = 0; i < args.length; i++) {
//            if (args[i].equals("f"))
//                directions[i] = Direction.F;
//            else if (args[i].equals("b"))
//                directions[i] = Direction.B;
//            else if (args[i].equals("r"))
//                directions[i] = Direction.R;
//            else if (args[i].equals("l"))
//                directions[i] = Direction.L;
//        }
//
//        run(directions);
        System.out.println("stop");
    }
    static void run(Direction[] directions) {
//        System.out.println("zwierzak idzie do przodu");
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == Direction.F)
                System.out.println("zwierzak idzie do przodu");
            else if (directions[i] == Direction.B)
                System.out.println("zwierzak idzie do tyłu");
            else if (directions[i] == Direction.R)
                System.out.println("zwierzak idzie W prawo");
            else if (directions[i] == Direction.L)
                System.out.println("zwierzak idzie w lewo");
        }

    }
}
