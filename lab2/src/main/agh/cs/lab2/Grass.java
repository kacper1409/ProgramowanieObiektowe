package agh.cs.lab2;

public class Grass {

    private Vector2d position;  // może być finalne

    public Grass (Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() { return this.position; }

    public String toString() { return "*"; }


}
