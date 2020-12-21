package agh.cs.lab8.graph;

import agh.cs.lab8.engine.Animal;
import agh.cs.lab8.engine.Engine;
import agh.cs.lab8.engine.Plant;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;

/**
 * Panel - tu są funkcje odrysowywania mapy
 */
public class EGPanel extends JPanel {
    static final int CELLSIZE_W = 10;       // rozmiar komórki do wizualizacji W
    static final int CELLSIZE_H = 10;       // rozmiar komórki do wizualizacji H

    private Engine engine;                  // silnik symulacji obsługujacy ten panel mapy stepu / dżungli

    private Graphics2D g2d;                 // API grafiki Swing

    private Animal selectedAnimal;          // zaznaczone zwierzę

    public Animal getSelectedAnimal() {
        return selectedAnimal;
    }

    public void setSelectedAnimal(Animal selectedAnimal) {
        this.selectedAnimal = selectedAnimal;
    }

    private Map<String, List<Animal>> dominantGenomeAnimals;

    public Map<String, List<Animal>> getDominantGenomeAnimals() {
        return dominantGenomeAnimals;
    }

    public void setDominantGenomeAnimals(Map<String, List<Animal>> dominantGenomeAnimals) {
        this.dominantGenomeAnimals = dominantGenomeAnimals;
    }

    public EGPanel(Engine engine) {
        this.engine = engine;
        selectedAnimal = null;
        dominantGenomeAnimals = null;

        setPreferredSize(new Dimension(engine.getWidth() * CELLSIZE_W + 1, engine.getHeight() * CELLSIZE_H + 1));
    }

    private double xToXd(int x) {
        return x * CELLSIZE_W;
    }

    private double yToYd(int y) {
        return y * CELLSIZE_H;
    }

    private double wToWd(int w) {
        return w * CELLSIZE_W;
    }

    private double hToHd(int h) {
        return h * CELLSIZE_H;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;    // żeby nie przekazywać cały czas przez argumenty podfunkcji

        paintScene();
        paintAnimals();
        paintPlants();
    }

    protected void paintScene() {
        // brzeg mapy i brzeg dżungli

        Rectangle2D mapBorder = new Rectangle2D.Double(xToXd(0), yToYd(0), wToWd(engine.getWidth()), hToHd(engine.getHeight()));
        Rectangle2D jungleBorder = new Rectangle2D.Double(
                xToXd((engine.getWidth() - engine.getJungleWidth()) / 2),
                yToYd((engine.getHeight() - engine.getJungleHeight()) / 2),
                wToWd(engine.getJungleWidth()),
                hToHd(engine.getJungleHeight()));

        g2d.draw(mapBorder);
        g2d.draw(jungleBorder);
    }

    protected void paintAnimals() {
        for (Animal animal : engine.getAnimals()) {
            drawAnimal(animal);
        }
    }

    protected void paintPlants() {
        for (Plant plant : engine.getPlants()) {
            drawPlant(plant);
        }
    }

    /**
     * Zwierze na współrzędnych xd, yd (w jednostkach grafiki)
     *
     * @param xd
     * @param yd
     * @return
     */
    public Animal animalAt(double xd, double yd) {
        Animal maxEnergyAnimalAt = null;

        for (Animal animal : engine.getAnimals()) {
            double xdAnimal = xToXd(animal.getPosition().getX());
            double ydAnimal = yToYd(animal.getPosition().getY());
            if (xd > xdAnimal && xd < xdAnimal + CELLSIZE_W && yd > ydAnimal && yd < ydAnimal + CELLSIZE_H) {
                if (maxEnergyAnimalAt == null || animal.getEnergy() > maxEnergyAnimalAt.getEnergy())
                    maxEnergyAnimalAt = animal;
            }
        }

        return maxEnergyAnimalAt;
    }

    protected void drawAnimal(Animal animal) {
        double xd = xToXd(animal.getPosition().getX());
        double yd = yToYd(animal.getPosition().getY());

        Ellipse2D ellipse = new Ellipse2D.Double(xd, yd, CELLSIZE_W - 0, CELLSIZE_H - 0);
        g2d.setColor(energyToColor(animal.getEnergy()));
        g2d.fill(ellipse);

        if (dominantGenomeAnimals != null)
            for (List<Animal> subList : dominantGenomeAnimals.values())
                if (subList.contains(animal)) {
                    ellipse = new Ellipse2D.Double(xd - 1.0, yd - 1.0, CELLSIZE_W + 2.0, CELLSIZE_H + 2.0);
                    g2d.setColor(Color.BLACK);
                    Stroke stroke = g2d.getStroke();
                    g2d.setStroke(new BasicStroke(3));
                    g2d.draw(ellipse);
                    g2d.setStroke(stroke);
                }

        if (animal == selectedAnimal) {
            xd = xToXd(animal.getPosition().getX()) + CELLSIZE_W / 2;
            yd = yToYd(animal.getPosition().getY()) + CELLSIZE_H / 2;

            Line2D lineV = new Line2D.Double(xd, yToYd(0), xd, yToYd(engine.getHeight()));
            Line2D lineH = new Line2D.Double(xToXd(0), yd, xToXd(engine.getWidth()), yd);
            Stroke stroke = g2d.getStroke();
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2f, 0f, 2f}, 0));
            g2d.draw(lineH);
            g2d.draw(lineV);
            g2d.setStroke(stroke);
        }
    }

    protected void drawPlant(Plant plant) {
        Rectangle2D rectangle = new Rectangle2D.Double(xToXd(plant.getPosition().getX()) + 1.0, yToYd(plant.getPosition().getY()) + 1.0, CELLSIZE_W - 1.0, CELLSIZE_H - 1.0);
        g2d.setColor(new Color(0, 170, 0));
        g2d.fill(rectangle);
    }

    private Color energyToColor(double energy) {
        int daysOk = 30;
        double energyOk = daysOk * engine.getMoveEnergy();

        if (energy >= energyOk) return new Color(0, 120, 255);  // jeśli energii na > miesiąc to niebieski
        if (energy <= 0.0)
            return new Color(0, 0, 0);      // jeśli zmarło => czarny (wyjątek widoczny tylko w jednym dniu)

        int r = (int) (((energyOk - energy) / energyOk) * 255);
        int g = 0;
        int b = (int) ((energy / energyOk) * 255);

        return new Color(r, g, b);                                      // 0 < energia < energiaOk => kolor pomiędzy niebieskim a czerwonym

        /*
        if (energy >= 7 * engine.getMoveEnergy())   return new Color(0x0088DD);
        if (energy >= 5 * engine.getMoveEnergy())   return new Color(0xFFD301);
        if (energy >= 3 * engine.getMoveEnergy())   return new Color(0xFF8B01);
        if (energy >= 1 * engine.getMoveEnergy())   return new Color(0xC23B21);
                                                    return new Color(0xA40001);
        */
    }
}
