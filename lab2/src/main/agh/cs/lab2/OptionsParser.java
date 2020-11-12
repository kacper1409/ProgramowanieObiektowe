package agh.cs.lab2;

import java.util.LinkedList;
import java.util.List;

public class OptionsParser {

    public MoveDirection[] parse(String[] array) {
        List<MoveDirection> result = new LinkedList<>();

        for (String element : array) {  // czy ta wstępna iteracja jest potrzebna, skoro rzuca Pan wyjątek?
            if (!element.equals("f") && !element.equals("forward") &&
                !element.equals("b") && !element.equals("backward") &&
                !element.equals("l") && !element.equals("left") &&
                !element.equals("r") && !element.equals("right")) {

                throw new IllegalArgumentException(element + " is not legal move specification");
            }
        }

        for (String element : array) {
            if (element.equals("f") || element.equals("forward"))
                result.add(MoveDirection.FORWARD);

            if (element.equals("b") || element.equals("backward"))
                result.add(MoveDirection.BACKWARD);

            if (element.equals("l") || element.equals("left"))
                result.add(MoveDirection.LEFT);

            if (element.equals("r") || element.equals("right"))
                result.add(MoveDirection.RIGHT);
        }

        MoveDirection[] res = new MoveDirection[result.size()];

        return result.toArray(res);
    }
}
