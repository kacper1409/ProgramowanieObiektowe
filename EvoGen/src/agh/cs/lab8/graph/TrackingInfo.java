package agh.cs.lab8.graph;

import agh.cs.lab8.engine.Animal;

/**
 * Pomocnicza klasa grupująca dane śledzenia
 */
public class TrackingInfo {
    TrackingInfo() {
        reset();
    }

    public void reset() {
        animal = null;
        nTrack0 = 0;
        maxNTrack = 0;
        nTrack = 0;
        numberOfChildren0 = 0;
        numberOfDescendants0 = 0;
    }

    private Animal animal;                              // śledzone zwierzę

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    private int nTrack0;                                // numer dnia początku śledzenia

    public int getNTrack0() {
        return nTrack0;
    }

    public void setNTrack0(int nTrack0) {
        this.nTrack0 = nTrack0;
    }

    private int maxNTrack;                              // numer dnia planowo kończącego śledzenie

    public int getMaxNTrack() {
        return maxNTrack;
    }

    public void setMaxNTrack(int maxNTrack) {
        this.maxNTrack = maxNTrack;
    }

    private int nTrack;                                 // numer dnia faktycznie kończącego śledzenie

    public int getNTrack() {
        return nTrack;
    }

    public void setNTrack(int nTrack) {
        this.nTrack = nTrack;
    }

    private int numberOfChildren0;                      // liczba dzieci na starcie śledzenia

    public int getNumberOfChildren0() {
        return numberOfChildren0;
    }

    public void setNumberOfChildren0(int numberOfChildren0) {
        this.numberOfChildren0 = numberOfChildren0;
    }

    private int numberOfDescendants0;                  // liczba potomków na starcie śledzenia

    public int getNumberOfDescendants0() {
        return numberOfDescendants0;
    }

    public void setNumberOfDescendants0(int numberOfDescendants0) {
        this.numberOfDescendants0 = numberOfDescendants0;
    }
}
