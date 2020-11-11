package agh.cs.lab2;

import java.util.*;

public class GrassField extends AbstractWorldMap {

    private int grassPoints;

    private List<Grass> grassPointsArray;
    private Map<Vector2d,Grass> grass;

    private List<Animal> animalList;
    private Map<Vector2d,Animal> animals;

    public GrassField (int grassPoints) {
        this.grassPoints = grassPoints;

        this.animalList = new LinkedList<Animal>();
        this.animals = new HashMap<>();

        this.grassPointsArray = new LinkedList<Grass>();
        this.grass = new HashMap<>();

        this.placeGrass();
    }

    /**
     *
     */
    private void placeGrass() {
        Random grassGenerator = new Random();
        int grassRange = (int)Math.sqrt(grassPoints * 10);          // lower limit is (0, 0) which is default lower limit
        int grassCounter = 0;                                       // of nextInt(int n) (range: 0 - n)
        while (grassCounter < grassPoints) {
            Vector2d currentPosition = new Vector2d(grassGenerator.nextInt(grassRange), grassGenerator.nextInt(grassRange));
            if (!grassOccupied(currentPosition)) {
                grassPointsArray.add(new Grass(currentPosition));
                grass.put(new Vector2d(currentPosition.x, currentPosition.y), new Grass(currentPosition));
                grassCounter++;
            }
        }
        limitSet();
    }

    public Grass getGrass(int grassIndex) {
        return grassPointsArray.get(grassIndex);
    }

    public Animal getAnimal(int animalIndex) {
        return animalList.get(animalIndex);
    }

    /**
     * isOccupied refers to Animals
     * grassOccupied refers to Grass
     * @return
     */
    public boolean grassOccupied(Vector2d position) {
        for(Grass grass : grassPointsArray) {
            if(position.equals(grass.getPosition())) return true;
        }

        return false;
    }

    private void limitSet() {

        int limitX = 0;
        int limitY = 0;

        for (Animal animal : animalList) {
            if (animal.getPosition().x > limitX) limitX = animal.getPosition().x;
            if (animal.getPosition().y > limitY) limitY = animal.getPosition().y;
        }

        for (Grass grass : grassPointsArray) {
            if (grass.getPosition().x > limitX) limitX = grass.getPosition().x;
            if (grass.getPosition().y > limitY) limitY = grass.getPosition().y;
        }

        limit = new Vector2d(limitX, limitY);

    }

    @Override
    public Vector2d getLimit() {
        return limit;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x >= 0 && position.y >= 0 && !isOccupied(position);
    }

    @Override
    public boolean place(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
            animalList.add(animal);
            animals.put(animal.getPosition(), animal);
            limitSet();
            return true;
        }
        throw new IllegalArgumentException("cannot place an object at " + animal.positionToString());
    }

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
        limitSet();

        //        for (int i = 0; i < directions.length; i++)
        //            animalList.get(i % animalList.size()).move(directions[i]);
    }

    @Override
    /**
     * checks only if isOccupied by another Animal
     * grassOccupied refers to Grass
     */
    public boolean isOccupied(Vector2d position) {

        if (!super.isOccupied(position)) return false;
        if (objectAt(position) instanceof Animal) return true;

        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(animals.containsKey(position)) return animals.get(position);
        if(grass.containsKey(position)) return  grass.get(position);

//        for (Grass grass : grassPointsArray) {
//            if (position.equals(grass.getPosition())) return grass;
//        }

        return null;
    }
}
