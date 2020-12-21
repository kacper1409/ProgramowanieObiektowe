package agh.cs.lab8.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Genome {
    public static final int NUMBER_OF_GENES = 8;
    public static final int GENOME_SIZE = 32;
    public static final int NUMBER_OF_PARENTS = 2;
    public static final int GENOME_PARTS = 3;
    private Random random;

    private int[] genomeSequence;

    public Genome() {
        this.genomeSequence = new int[GENOME_SIZE];

        this.random = new Random();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i : genomeSequence)
            sb.append(i);
        return sb.toString();
    }

    public void initGenome() {
        int upperBound = NUMBER_OF_GENES;

        for (int i = 0; i < NUMBER_OF_GENES; i++) {   // zapewnienie co najmniej jednego genu kazdego rodzaju (zakres [0, 7])
            genomeSequence[i] = i;
        }

        for (int i = NUMBER_OF_GENES; i < GENOME_SIZE; i++) { // wypelnienie pozostalej czesci losowymi genami
            genomeSequence[i] = random.nextInt(upperBound);
        }

        Arrays.sort(genomeSequence);
    }

    public int generateRotation() {
        int selectedIndex = random.nextInt(GENOME_SIZE);
        return genomeSequence[selectedIndex];
    }

    /**
     * generowanie genotypu powstajacego dziecka
     * wynik losowania wartosci zmiennej randomGenome okresla rodzica, ktory uwspolni dwie z trzech czesci
     * genomu: 0 - firstParentGenome, 1 - secondParentGenome.
     * wynik losowania wartosci zmiennej randomPart okresla czesc genomu, ktora zostanie wybrana z drugiego rodzica
     * i nadpisana na genomie pierwszego wybranego rodzica.
     *
     * @param firstParentGenome
     * @param secondParentGenome
     */
    public void generateChildGenotype(Genome firstParentGenome, Genome secondParentGenome) {
        int randomGenome = random.nextInt(NUMBER_OF_PARENTS);
        int randomPart = random.nextInt(GENOME_PARTS);

        List<Genome> parentGenomes = new ArrayList<>();
        parentGenomes.add(firstParentGenome);
        parentGenomes.add(secondParentGenome);

        int leftDivider = random.nextInt(GENOME_SIZE - 1);
        int rightDivider;
        do {
            rightDivider = random.nextInt(GENOME_SIZE - 1);
        }
        while (leftDivider == rightDivider);

        if (leftDivider > rightDivider) {           // dla wygody dalszych obliczen rightDivider
            int tmp = leftDivider;                  // jest zawsze wiekszy od leftDivider
            leftDivider = rightDivider;
            rightDivider = tmp;
        }

        for (int i = 0; i < GENOME_SIZE; i++) {
            genomeSequence[i] = parentGenomes.get(randomGenome).genomeSequence[i];
        }

        int otherGenome = Math.abs(randomGenome - 1);   // otherGenome = 1 wtw. gdy randomGenome = 0
        // otherGenome = 0 wtw. gdy randomGenome = 1
        if (randomPart == 0)
            for (int i = 0; i <= leftDivider; i++) genomeSequence[i] = parentGenomes.get(otherGenome).genomeSequence[i];
        else if (randomPart == 1) for (int i = leftDivider + 1; i <= rightDivider; i++)
            genomeSequence[i] = parentGenomes.get(otherGenome).genomeSequence[i];
        else for (int i = rightDivider + 1; i < GENOME_SIZE; i++)
                genomeSequence[i] = parentGenomes.get(otherGenome).genomeSequence[i];

        completeGenomeHandler();
        Arrays.sort(genomeSequence);
    }

    private void completeGenomeHandler() {          // zapewnia kompletnosc genomu dziecka
        int[] genes = new int[NUMBER_OF_GENES];
        boolean isComplete = false;

        while (isComplete == false) {
            isComplete = true;
            for (int gene : genes) gene = 0;
            for (int gene : genomeSequence) genes[gene] += 1;
            for (int i = 0; i < NUMBER_OF_GENES; i++) {
                if (genes[i] == 0) {
                    isComplete = false;
                    genomeSequence[random.nextInt(GENOME_SIZE)] = i;
                }
            }
        }
    }
}

