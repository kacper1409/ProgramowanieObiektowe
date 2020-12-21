package agh.cs.lab8;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Klasa do wczytywania parametrów narzędziem do obsługi JSON
 * Parametry wg specyfikacji, opisy w Engine.java, dopisałem możliwość regulowania szybkości symulacji i czy autostart
 */
public class Parameters {
    public static Parameters read(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Parameters parameters = mapper.readValue(new File(fileName), Parameters.class);

        return parameters;
    }

    private int width = 100;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private int height = 100;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private double startEnergy = 20.0;

    public double getStartEnergy() {
        return startEnergy;
    }

    public void setStartEnergy(double startEnergy) {
        this.startEnergy = startEnergy;
    }

    private double moveEnergy = 1.0;

    public double getMoveEnergy() {
        return moveEnergy;
    }

    public void setMoveEnergy(double moveEnergy) {
        this.moveEnergy = moveEnergy;
    }

    private int numAnimals = 10;

    public int getNumAnimals() {
        return numAnimals;
    }

    public void setNumAnimals(int numAnimals) {
        this.numAnimals = numAnimals;
    }

    private double plantEnergy = 5.0;

    public double getPlantEnergy() {
        return plantEnergy;
    }

    public void setPlantEnergy(double plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    private double jungleRatio = 0.2;

    public double getJungleRatio() {
        return jungleRatio;
    }

    public void setJungleRatio(double jungleRatio) {
        this.jungleRatio = jungleRatio;
    }

    private double engineSpeedPerSec = 10.0;    // liczba kroków (dni symulacji) na sekundę

    public double getEngineSpeedPerSec() {
        return engineSpeedPerSec;
    }

    public void setEngineSpeedPerSec(double engineSpeedPerSec) {
        this.engineSpeedPerSec = engineSpeedPerSec;
    }

    private int autoStart = 1;

    public int getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(int autoStart) {
        this.autoStart = autoStart;
    }
}
