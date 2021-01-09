package agh.cs.lab8;

import agh.cs.lab8.engine.Engine;
import agh.cs.lab8.graph.EGFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa uruchomieniowa:
 * Wczytywanie parametrów z pliku JSON (argument programu), tworzenie okien i "silników", timer do sterowania symulacją
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Brak parametru: nazwa pliku z konfiguracją");
            return;
        }

        Parameters parameters;

        try {
            parameters = Parameters.read(args[0]);
        } catch (Exception ex) {    // wszystkie wyjątki do jednego worka
            System.out.println("Błąd odczytu pliku z konfiguracją: " + ex.getMessage());
            return;
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // tworzymy dwa niezależne okna symulacji z osobnymi "silnikami" inicjowanymi tymi samymi parametrami

                EGFrame[] frames = new EGFrame[]{
                        new EGFrame(new Engine(parameters), new Point(10, 10)),
                        new EGFrame(new Engine(parameters), new Point(10, 440)),
                };

                for (EGFrame frame : frames) {
                    frame.getEngine().init();
                    frame.updateLabels();
                    frame.getEgPanel().repaint();

                    frame.setPaused(parameters.getAutoStart() == 0);
                }

                int delay = (int) (1000.0 / parameters.getEngineSpeedPerSec());
                Timer timer = new Timer(delay, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // pętla po oknach symulacji:

                        for (EGFrame frame : frames) {
                            // krok symulacji dla tego okna i jego silnika:

                            if (frame.getPaused()) continue;

                            frame.getEngine().step();
                            frame.updateLabels();
                            frame.getEgPanel().repaint();

                            // krok symulacji zakończony, info w oknie wyświetlone, sprawdzamy warunki zakończenia śledzenia jeśli było aktywne

                            if (frame.getTrackingInfo().getAnimal() != null)
                                if (frame.getTrackingInfo().getAnimal().getEnergy() <= 0.0 ||
                                        frame.getEngine().getDay() == frame.getTrackingInfo().getMaxNTrack()) {
                                    frame.setPaused(true);
                                    frame.showTrackingSummary();
                                    frame.resetTracking();
                                    frame.updateLabels();
                                    frame.getEgPanel().repaint();
                                }
                        }
                    }
                });

                timer.start();
            }
        });
    }
}
