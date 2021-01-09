package agh.cs.lab8.engine;

import agh.cs.lab8.Parameters;
import agh.cs.lab8.util.Vector2d;

import java.util.*;
import java.lang.Math;

public class Engine {

    public static final double REPRODUCTION_ENERGY_LOSS = 0.25;

    private Parameters parameters;

    private List<Vector2d> animalFreeSpots;         // miejsca niezajęte przez zwierzęta
    private List<Vector2d> plantFreeSpotsJungle;    // miejsca w dżungli niezajęte przez rośliny
    private List<Vector2d> plantFreeSpotsStep;      // miejsca w stepie niezajęte przez rośliny

    private int day;                                // nr dnia symulacji
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    private int width;                              // szer. mapy
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    private int height;                             // wys. mapy
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    private Vector2d jungleTopLeftCorner;           // lewy górny wierzchołek dżungli na mapie

    private double reproductionMinEnergy;           // minimalna energia umożliwiająca rozmnażanie
    public double getReproductionMinEnergy() { return reproductionMinEnergy; }
    public void setReproductionMinEnergy(int reproductionMinEnergy) { this.reproductionMinEnergy = reproductionMinEnergy; }

    private int jungleWidth;                        // szer. dżungli
    public int getJungleWidth() { return jungleWidth; }
    public void setJungleWidth(int jungleWidth) { this.jungleWidth = jungleWidth; }

    private int jungleHeight;                       // wys. dżungli
    public int getJungleHeight() { return jungleHeight; }
    public void setJungleHeight(int jungleHeight) { this.jungleHeight = jungleHeight; }

    private double moveEnergy;                      // energia tracona w ciągu dnia
    public double getMoveEnergy() { return moveEnergy; }
    public void setMoveEnergy(double moveEnergy) { this.moveEnergy = moveEnergy; }

    private double plantEnergy;                     // energia zyskiwana przy zjedzenie rośłiny
    public double getPlantEnergy() { return plantEnergy; }
    public void setPlantEnergy(double plantEnergy) { this.plantEnergy = plantEnergy; }

    private boolean paused;                         // do zatrzymywania i wznawiania symulacji
    public void setPaused(boolean paused) { this.paused = paused; }
    public boolean getPaused() { return paused; }

    private List<Animal> animals;                           // lista żywych zwierząt
    public List<Animal> getAnimals() { return animals; }
    public void setAnimals(List<Animal> animals) { this.animals = animals; }

    private List<Plant> plants;                             // lista roślin pozostających na mapie (niezjedzonych)
    public List<Plant> getPlants() { return plants; }   // zniszczona hermetyzacja
    public void setPlants(List<Plant> plants) { this.plants = plants; }

    private List<DayStat> dayStats;                         // lista dziennych statystyk
    public List<DayStat> getDayStats() { return dayStats; }

    private Random random;                                  // generator liczb losowych dla tego silnika

    public Engine(Parameters parameters) {
        this.parameters = parameters;

        width = parameters.getWidth();
        height = parameters.getHeight();
        reproductionMinEnergy = parameters.getStartEnergy() / 2.0;

        // zakładamy, że dżungla jest kwadratem o polu w stosunku do stepu określonym przez proporcję jungleRatio

        if (parameters.getJungleRatio() > 1.0) parameters.setJungleRatio(1.0);  // "Większość świata pokrywają stepy"

        double jungleArea = parameters.getJungleRatio() * width * height / (1.0 + parameters.getJungleRatio());
        jungleWidth = (int) Math.round(Math.sqrt(jungleArea));
        jungleHeight = jungleWidth;

        // korekta gdyby taki kwadrat nie zmieścil się na szerokość lub wysokość - może się zdarzyć przy długich lub wysokich mapach
        // ratujemy się prostokątem

        while (jungleWidth > width) {
            jungleWidth--;
            jungleHeight++;
        }
        while (jungleHeight > height) {
            jungleHeight--;
            jungleWidth++;
        }

        jungleTopLeftCorner = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);

        moveEnergy = parameters.getMoveEnergy();
        plantEnergy = parameters.getPlantEnergy();

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        random = new Random();

        plantFreeSpotsJungle = new ArrayList<>();
        for (int i = jungleTopLeftCorner.getX(); i < jungleTopLeftCorner.getX() + jungleWidth; i++) {
            for (int j = jungleTopLeftCorner.getY(); j < jungleTopLeftCorner.getY() + jungleHeight; j++) {
                plantFreeSpotsJungle.add(new Vector2d(i, j));
            }
        }

        plantFreeSpotsStep = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if (plantFreeSpotsJungle.contains(position)) continue;
                plantFreeSpotsStep.add(position);
            }
        }

        animalFreeSpots = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                animalFreeSpots.add(new Vector2d(i, j));
            }
        }

        dayStats = new ArrayList<>();
    }

    /**
     * Przed rozpoczęciemn symulacji
     */
    public void init() {
        day = 0;

        for (int i = 0; i < parameters.getNumAnimals(); i++) {
            Animal animal = new Animal(getWidth(), getHeight(), day);
            animal.setEnergy(parameters.getStartEnergy());
            animals.add(animal);
        }

        // losujemy położenia zwierząt
        for (Animal animal : animals) {
            int randomIndex = random.nextInt(animalFreeSpots.size());
            Vector2d position = new Vector2d(animalFreeSpots.get(randomIndex).getX(), animalFreeSpots.get(randomIndex).getY());
            animal.setPosition(position);
            animalFreeSpots.remove(position);
            animal.getGenome().initGenome();
        }

        dayStats.add(0, new DayStat(this));     // najnowsze na pocz.
    }

    /**
     * Krok symulacji (dzień, epoka)
     */
    public void step() {
        day++;

        removeDeadAnimals();
        turnAndMoveAnimals();
        feedAnimals();
        reproduceAnimals();
        regeneratePlants();
        reduceEnergy();

        dayStats.add(0, new DayStat(this));     // najnowsze na pocz.
    }

    private void reduceEnergy() {

        for (Animal animal : animals)
            animal.setEnergy(animal.getEnergy() - moveEnergy);
    }

    private void removeDeadAnimals() {

        Iterator<Animal> it = animals.iterator();
        while (it.hasNext()) {
            Animal animal = it.next();
            if (animal.getEnergy() <= 0.0) {
                if (animalsAtCell(animal.getPosition()).size() == 1) animalFreeSpots.add(animal.getPosition());
                it.remove();
            }
        }
    }

    private void turnAndMoveAnimals() {

        for (Animal animal : animals) {
            animal.turn();

            Vector2d prevPosition = animal.getPosition();
            animal.move();
            if (animalsAtCell(prevPosition).size() == 0) animalFreeSpots.add(prevPosition);
            animalFreeSpots.remove(animal.getPosition());
        }
    }

    private void feedAnimals() {

        Iterator<Plant> it = plants.iterator();
        while (it.hasNext()) {
            Plant plant = it.next();
            if (animalsAtCell(plant.getPosition()).size() == 0)
                continue; // brak roslin na danym polu, wiec roslina zostaje nieruszona
            List<Animal> list = getAnimalsWithEnergy(plant.getPosition(), getMaxEnergyAtCell(plant.getPosition())); // zwierzeta odzywiajace sie - te o najwiekszej energii
            double plantRation = Math.floor(plantEnergy / list.size()); // zwierzeta dziela rosline miedzy siebie
            for (Animal animal : list) {
                animal.setEnergy(animal.getEnergy() + plantRation);
            }

            it.remove();
            if (isInJungle(plant.getPosition())) plantFreeSpotsJungle.add(plant.getPosition());
            else plantFreeSpotsStep.add(plant.getPosition());
        }
    }

    private void reproduceAnimals() {

        List<Animal> children = new ArrayList<>();
        List<Vector2d> dirtySpots = new ArrayList<>();  // nieczytelna nazwa

        for (Animal animal : animals) {
            List<Animal> list = animalsAtCell(animal.getPosition());
            if (list.size() < 2) continue;
            if (dirtySpots.contains(animal.getPosition())) continue;
            dirtySpots.add(animal.getPosition());

            List<Animal> parents = findParents(list);
            Animal parentA = parents.get(0);
            Animal parentB = parents.get(1);
            if (parentA.getEnergy() < reproductionMinEnergy || parentB.getEnergy() < reproductionMinEnergy) continue;

            Animal child = new Animal(getWidth(), getHeight(), day);
            children.add(child);

            child.setParentA(parentA);
            parentA.incrNumberOfChildren();
            parentA.incrNumberOfDescendants();
            child.setParentB(parentB);
            parentB.incrNumberOfChildren();
            parentB.incrNumberOfDescendants();

            child.setPosition(childPositionHandler(animal.getPosition()));
            child.getGenome().generateChildGenotype(parentA.getGenome(), parentB.getGenome());
            child.setDirection(random.nextInt(Genome.NUMBER_OF_GENES));
            child.setEnergy(parentA.getEnergy() * REPRODUCTION_ENERGY_LOSS + parentB.getEnergy() * REPRODUCTION_ENERGY_LOSS);

            parentA.setEnergy((1.0 - REPRODUCTION_ENERGY_LOSS) * parentA.getEnergy());
            parentB.setEnergy((1.0 - REPRODUCTION_ENERGY_LOSS) * parentB.getEnergy());
        }

        for (Animal child : children) {
            animals.add(child);
            animalFreeSpots.remove(child.getPosition());
        }
    }

    private void regeneratePlants() {
        List<Vector2d> objectFreeSpotsJungle = getIntersection(animalFreeSpots, plantFreeSpotsJungle);
        List<Vector2d> objectFreeSpotsStep = getIntersection(animalFreeSpots, plantFreeSpotsStep);

        if (objectFreeSpotsJungle.size() != 0) {                                // pojawienie sie rosliny w dzungli
            int randomIndex = random.nextInt(objectFreeSpotsJungle.size());
            Vector2d position = objectFreeSpotsJungle.get(randomIndex);
            plants.add(new Plant(new Vector2d(position.getX(), position.getY())));
            plantFreeSpotsJungle.remove(position);
        }

        if (objectFreeSpotsStep.size() != 0) {                                  // pojawienie sie rosliny w stepie
            int randomIndex = random.nextInt(objectFreeSpotsStep.size());
            Vector2d position = objectFreeSpotsStep.get(randomIndex);
            plants.add(new Plant(new Vector2d(position.getX(), position.getY())));
            plantFreeSpotsStep.remove(position);
        }
    }

    private int normX(int x) {
        while (x < 0) x += width;
        return x % width;
    }

    private int normY(int y) {
        while (y < 0) y += height;
        return y % height;
    }

    private List<Animal> animalsAtCell(Vector2d position) {
        List<Animal> list = new ArrayList<Animal>();    // może warto trzymać zwierzęta w HashMapie?
        for (Animal animal : animals)
            if (animal.getPosition().equals(position)) {
                list.add(animal);
            }
        return list;
    }

    private List<Plant> plantsAtCell(Vector2d position) {
        List<Plant> list = new ArrayList<Plant>();
        for (Plant plant : plants)
            if (plant.getPosition().equals(position)) {
                list.add(plant);
            }
        return list;
    }

    private double getMaxEnergyAtCell(Vector2d position) {
        List<Animal> list = animalsAtCell(position);
        double maxEnergy = list.get(0).getEnergy();
        for (Animal animal : list) {
            if (animal.getEnergy() > maxEnergy) maxEnergy = animal.getEnergy();
        }
        return maxEnergy;
    }

    private List<Animal> getAnimalsWithEnergy(Vector2d position, double energy) {
        List<Animal> list = animalsAtCell(position);
        List<Animal> result = new ArrayList<>();
        for (Animal animal : list) {
            if (animal.getEnergy() == energy) result.add(animal);
        }
        return result;
    }

    private List<Animal> findParents(List<Animal> list) {
        List<Animal> mixedSet = new ArrayList<>();  // myląca nazwa dla listy
        List<Animal> result = new ArrayList<>();
        while (list.size() != 0) {  // Random.shuffle
            int randomIndex = random.nextInt(list.size());
            mixedSet.add(list.get(randomIndex));
            list.remove(randomIndex);   // czy opróżnienie tej listy jest bezpieczne?
        }
        for (int i = 0; i < Genome.NUMBER_OF_PARENTS; i++) {
            int index = getStrongestAnimalIndex(mixedSet);
            result.add(mixedSet.get(index));
            mixedSet.remove(index);
        }

        return result;
    }

    private Vector2d childPositionHandler(Vector2d parentsPosition) {
        List<Vector2d> potentialChildPosition = new ArrayList<>();
        List<Vector2d> availablePosition = new ArrayList<>();       // podzbior potentialChildPosition po jej przefiltrowaniu
        for (int i = 0; i < Genome.NUMBER_OF_GENES; i++) {
            Vector2d position = parentsPosition.add(parentsPosition.getUnitMotion(i));
            position.normalizePosition(getWidth(), getHeight());
            potentialChildPosition.add(position);
        }
        for (Vector2d vector2d : potentialChildPosition) {
            if (animalsAtCell(vector2d).size() == 0 && plantsAtCell(vector2d).size() == 0)
                availablePosition.add(vector2d);
        }

        if (availablePosition.size() == 0) {
            List<Vector2d> plantFreeSpots = mergeLists(plantFreeSpotsJungle, plantFreeSpotsStep);
            List<Vector2d> objectFreeSpots = getIntersection(animalFreeSpots, plantFreeSpots);
            int randomIndex = random.nextInt(objectFreeSpots.size());
            return objectFreeSpots.get(randomIndex);
        } else {
            int randomIndex = random.nextInt(availablePosition.size());
            return availablePosition.get(randomIndex);
        }
    }

    private int getStrongestAnimalIndex(List<Animal> list) {
        double maxEnergy = Double.NEGATIVE_INFINITY;
        int maxIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            Animal animal = list.get(i);
            if (animal.getEnergy() > maxEnergy) {
                maxEnergy = animal.getEnergy();
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private List<Vector2d> mergeLists(List<Vector2d> listA, List<Vector2d> listB) {
        List<Vector2d> copyA = new ArrayList<>(listA);
        List<Vector2d> copyB = new ArrayList<>(listB);

        copyA.addAll(copyB);

        return copyA;
    }

    private List<Vector2d> getIntersection(List<Vector2d> listA, List<Vector2d> listB) {
        List<Vector2d> copyA = new ArrayList<>(listA);
        List<Vector2d> copyB = new ArrayList<>(listB);

        copyA.retainAll(copyB);

        return copyA;
    }

    private boolean isInJungle(Vector2d position) {
        return (position.getX() >= jungleTopLeftCorner.getX()) && (position.getX() < jungleTopLeftCorner.getX() + jungleWidth) &&
                (position.getY() >= jungleTopLeftCorner.getY()) && (position.getY() < jungleTopLeftCorner.getY() + jungleHeight);
    }


    /**
     * Zwraca mapę złożona z elementów: string genomu, lista zwierząt z tym genomem,
     * umieszczając na niej tylko elementy z genomem występującym najczęściej i więcej niż raz
     * (mapa może mieć > 1 element jeżeli będzie kilka genomów jednakowo dominujących).
     * Zakładamy, że każdy genom jest uporządkowany od 0 i do 7.
     */
    public Map<String, List<Animal>> computeDominantGenomeAnimals() {
        Map<String, List<Animal>> tmpMap = new HashMap<>();

        // do mapy pomocniczej wpisujemy listy zwierząt z określonym genomem

        for (Animal animal : animals) {
            if (tmpMap.containsKey(animal.getGenome().toString()))
                tmpMap.get(animal.getGenome().toString()).add(animal);
            else {
                List<Animal> subList = new ArrayList<>();
                subList.add(animal);
                tmpMap.put(animal.getGenome().toString(), subList);
            }
        }

        // maksymalna długość listy w mapie

        int maxSize = 0;
        for (Map.Entry<String, List<Animal>> entry : tmpMap.entrySet())
            if (entry.getValue().size() > maxSize)
                maxSize = entry.getValue().size();

        if (maxSize < 2)
            return null;   // brak powtarzających się genomów (nie traktujemy jako dominujące występujących raz)

        // tworzymy mapę wynikową z jednym (lub kilkoma) genomami dominującymi:

        Map<String, List<Animal>> dominatedMap = new HashMap<>();

        for (Map.Entry<String, List<Animal>> entry : tmpMap.entrySet())
            if (entry.getValue().size() == maxSize)
                dominatedMap.put(entry.getKey(), entry.getValue());

        return dominatedMap;
    }
}   // ta klasa ma chyba za dużo pracy
