package agh.cs.lab2;

import java.util.LinkedList;
import java.util.List;

public class RectangularMap extends AbstractWorldMap {

    private List<Animal> animalList; // private!

    public RectangularMap (int width, int height) {
        this.limit = new Vector2d(width, height);   // width - 1
        this.animalList = new LinkedList<Animal>();
    }

    @Override
    public Vector2d getLimit() {
        return limit;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (
            position.x <= limit.x && // optionally precedes, follows
            position.x >= 0 &&
            position.y <= limit.y &&
            position.y >= 0 &&
            !isOccupied(position)
           ) { return true; }

        return false;
    }

    @Override
    public boolean place(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
            animalList.add(animal);
            return true;
        }
        return false;
    }

    @Override
    public void run(MoveDirection[] directions) {
        for (int i = 0; i < directions.length; i++)
            animalList.get(i % animalList.size()).move(directions[i]);
    }

    @Override
    public Object objectAt(Vector2d position) {
        for (Animal animal : animalList) {
                if (position.equals(animal.getPosition())) return animal;
        }
        return null;
    }


    public Animal getAnimal(int animalIndex) {
        return animalList.get(animalIndex);
    }
}