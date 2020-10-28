package agh.cs.lab2;

import java.util.LinkedList;
import java.util.List;

public class RectangularMap implements IWorldMap {

    public int width;
    public int height;
    public List<Animal> animalList;

    public RectangularMap (int width, int height) {
        this.width = width;
        this.height = height;
        this.animalList = new LinkedList<Animal>();
    }

    public String toString() {
        return new MapVisualizer(this).draw(new Vector2d(0, 0), new Vector2d(width, height));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (
            position.x <= width &&
            position.x >= 0 &&
            position.y <= width &&
            position.y >= 0 &&
            !isOccupied(position)
           ) { return true; }

        return false;
    }

    @Override
    public boolean place(Animal animal) {
        if (!isOccupied(animal.position)) {
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
    public boolean isOccupied(Vector2d position) {
        for (Animal animal : animalList) {
            if (position.equals(animal.position)) return true;
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        for (Animal animal : animalList) {
                if (position.equals(animal.position)) return animal;
        }
        return null;
    }
}
