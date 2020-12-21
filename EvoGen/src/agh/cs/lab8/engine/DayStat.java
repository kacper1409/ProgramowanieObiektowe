package agh.cs.lab8.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Statystyki po zakończonym dniu uwzględniające cykl dzienny ze specyfikacji:
 * <p>
 * - usunięcie martwych zwierząt z mapy
 * - skręt i przemieszczenie każdego zwierzęcia
 * - jedzenie (roślina jest zjadana przez zwierzę posiadające najwięcej energii ...
 * - rozmnażanie zwierząt (rozmnażają się zawsze dwa zwierzęta o najwyższej energii ...
 * - dodanie nowych roślin do mapy
 * Po tych operacjach odejmowana jest dzienna porcja energii zużyta przez zwierzaka i jeśli jest <= 0 oznacza to, że zwierze zmarło,
 * pozostaje na mapie i będzie posprzątane na początku następnego dnia
 * <p>
 * Niektóre pola Double, a nie double, bo mogą nie dać się wyliczyć (dzielenie przez zero w średnich)
 */
public class DayStat {
    private int day;                                // numer dnia, na wszelki wypadek, żeby po ew. usunięciu z listy to info się zachowało
    private int numAnimals;                         // liczba zwierząt żyjących i zmarłych tego dnia (zmarłe pozostają na mapie do nast. dnia)
    private int numPlants;                          // liczba roślin pozostających po tym dniu
    private Map<String, Integer> dominantGenomes;   // genomy dominujące i ich krotności (jeden lub kilka, tylko dla krotności >= 2)
    private Double meanAliveEnergy;                 // śr. energia zwierząt po tym dniu (pozostających przy życiu)
    private Double meanDeadsLifetime;               // śr. długość życia martwych (rozumiem jako "padłych" w tym dniu, a uśrednimy potem po epokach)
    private Double meanChildren;                    // śr. liczba dzieci dla żyjących zwierząt

    public DayStat(Engine engine) {
        this.day = engine.getDay();
        this.numAnimals = engine.getAnimals().size();
        this.numPlants = engine.getPlants().size();
        this.dominantGenomes = computeDominantGenomes(engine);
        this.meanAliveEnergy = computeMeanAliveEnergy(engine);
        this.meanDeadsLifetime = computeMeanDeadsLifetime(engine);
        this.meanChildren = computeMeanChildren(engine);
    }

    public String toStringFormatted() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Dzień:%5d    obiekty na mapie - zwierzęta:%5d    rośliny:%5d\n", day, numAnimals, numPlants));

        sb.append("Dominujące genomy i krotności:\n");
        if (dominantGenomes.isEmpty())
            sb.append("  Brak genomów o krotności >= 2\n");
        for (Map.Entry<String, Integer> entry : dominantGenomes.entrySet())
            sb.append("  " + entry.getKey() + " : " + entry.getValue() + "\n");

        String meanAliveEnergyStr = (meanAliveEnergy == null ? "?" : String.format("%7.2f", meanAliveEnergy));
        String meanDeadsLifetimeStr = (meanDeadsLifetime == null ? "?" : String.format("%7.2f", meanDeadsLifetime));
        String meanChildrenStr = (meanChildren == null ? "?" : String.format("%5.2f", meanChildren));
        sb.append("Średnia energia zw. żywych:" + meanAliveEnergyStr + "   " +
                "średnia dł. życia zw. martwych:" + meanDeadsLifetimeStr + "   " +
                "średnia liczba dzieci zw. żywych:" + meanChildrenStr + "\n");

        return sb.toString();
    }

    /**
     * Dominujące genomy j.w. ale tylko krotności w mapie, a nie listy zwierząt
     */
    public Map<String, Integer> computeDominantGenomes(Engine engine) {
        Map<String, Integer> dominantGenomes = new HashMap<>();
        Map<String, List<Animal>> dominantGenomeAnimals = engine.computeDominantGenomeAnimals();

        if (dominantGenomeAnimals != null)  // są wielokrotne genomy
            for (Map.Entry<String, List<Animal>> entry : dominantGenomeAnimals.entrySet())
                dominantGenomes.put(entry.getKey(), entry.getValue().size());

        return dominantGenomes;
    }

    /**
     * Srednia energia zwierząt pozostałych przy życiu po tym dniu
     */
    public Double computeMeanAliveEnergy(Engine engine) {
        double sum = 0.0;
        int aliveNum = 0;

        for (Animal animal : engine.getAnimals()) {
            if (animal.getEnergy() <= 0.0) continue;

            sum += animal.getEnergy();
            aliveNum++;
        }

        if (aliveNum == 0) return null;

        return sum / aliveNum;
    }

    /**
     * Srednia długość życia zwierząt martwych (ale pozostających na mapie do pocz. nast. dnia)
     */
    public Double computeMeanDeadsLifetime(Engine engine) {
        double sum = 0.0;
        int deadsNum = 0;

        for (Animal animal : engine.getAnimals()) {
            if (animal.getEnergy() > 0.0) continue;

            sum += engine.getDay() - animal.getBirthDay();
            deadsNum++;
        }

        if (deadsNum == 0) return null;

        return sum / deadsNum;
    }

    /**
     * Srednia liczba dzieci dla żyjących zwierząt
     */
    public Double computeMeanChildren(Engine engine) {
        double sum = 0.0;
        int aliveNum = 0;

        for (Animal animal : engine.getAnimals()) {
            if (animal.getEnergy() <= 0.0) continue;

            sum += animal.getNumberOfChildren();
            aliveNum++;
        }

        if (aliveNum == 0) return null;

        return sum / aliveNum;
    }
}
