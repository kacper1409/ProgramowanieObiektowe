package agh.cs.lab2;

import java.util.*;

public class RectangularMap extends AbstractWorldMap {

    private List<Animal> animalList;
    private Map<Vector2d,Animal> animals;


    public RectangularMap (int width, int height) {
        this.limit = new Vector2d(width, height);
        this.animalList = new LinkedList<Animal>();
        this.animals = new HashMap<>();
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
            animals.put(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException("cannot place an object at " + animal.positionToString());
    }

    /**
     * podjąłem próbę implementacji metody run przy pomocy kolekcji
     * animals.values() zgodnie z poleceniem w zad. 4, ale wprowadzenie
     * values() utrudnia implementacje, bo trudniej odwołać się do konkretnych
     * elementów niż w liście, a (for (Animal animal : animals.values()) jest ciężkie
     * do zrealizowania, gdy liczba ruchow nie jest podzielna przez liczbe zwierząt)
     */
    @Override
    public void run(MoveDirection[] directions) {
        for (int i = 0; i < directions.length; i++) {
            Animal prevAnimal = new Animal(this, new Vector2d(animalList.get(i % animalList.size()).getPosition().x, animalList.get(i % animalList.size()).getPosition().y));
            animalList.get(i % animalList.size()).move(directions[i]);
            if (!animalList.get(i % animalList.size()).getPosition().equals(prevAnimal.getPosition())) {
                animals.remove(prevAnimal.getPosition());
                animals.put(animalList.get(i % animalList.size()).getPosition(), animalList.get(i % animalList.size()));

            }
        }
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(animals.containsKey(position)) return animals.get(position);

//        for (Animal animal : animalList) {
//                if (position.equals(animal.getPosition())) return animal;
//        }
        return null;

    }


    public Animal getAnimal(int animalIndex) {
        return animalList.get(animalIndex);
    }
}