package agh.cs.lab8.engine;

import agh.cs.lab8.util.Vector2d;

public class Animal {
    private int birthDay;
    public int getBirthDay() { return birthDay; }
    public void setBirthDay(int birthDay) { this.birthDay = birthDay; }

    private Vector2d position;
    public Vector2d getPosition() { return position; }
    public void setPosition(Vector2d position) { this.position = position; }    // teleport

    private double energy;
    public double getEnergy() { return energy; }
    public void setEnergy(double energy) { this.energy = energy; }

    private Genome genome;
    public Genome getGenome() { return genome; }
    public void setGenome(Genome genome) { this.genome = genome; }

    private int direction;
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    private Animal parentA;
    public Animal getParentA() { return parentA; }
    public void setParentA(Animal parentA) { this.parentA = parentA; }  // czy rodzica można ustawić w dowolnym momencie?

    private Animal parentB;
    public Animal getParentB() { return parentB; }
    public void setParentB(Animal parentB) { this.parentB = parentB; }

    private int numberOfChildren;
    public int getNumberOfChildren() { return numberOfChildren; }
    public void incrNumberOfChildren() { numberOfChildren++; }

    private int numberOfDescendants;
    public int getNumberOfDescendants() { return numberOfDescendants; }
    public void incrNumberOfDescendants() {
        numberOfDescendants++;
        if (getParentA() != null) getParentA().incrNumberOfDescendants();
        if (getParentA() != null) getParentB().incrNumberOfDescendants();
    }

    private int mapWidth;
    private int mapHeight;

    public Animal(int mapWidth, int mapHeight, int birthDay) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.birthDay = birthDay;

        position = new Vector2d(0, 0);
        genome = new Genome();
        direction = 0;
        energy = 0.0;
        parentA = null;
        parentB = null;
        numberOfChildren = 0;
        numberOfDescendants = 0;
    }

    public void turn() {
        int rotation = genome.generateRotation();
        direction = (direction + rotation) % Genome.NUMBER_OF_GENES;
    }

    public void move() {
        position = position.add(position.getUnitMotion(direction));
        position.normalizePosition(mapWidth, mapHeight);
    }
}
