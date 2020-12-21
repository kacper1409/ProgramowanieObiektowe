package agh.cs.lab8.engine;

import agh.cs.lab8.util.Vector2d;

public class Plant {
    private Vector2d position;

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    Plant(Vector2d position) {
        this.position = position;
    }
}
