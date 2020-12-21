package agh.cs.lab8.graph;

import agh.cs.lab8.engine.Animal;
import agh.cs.lab8.engine.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * To jest obsługa okna aplikacji (frame), jednego z dwóch (może być więcej)
 * Labelki, klawisze na górnym panelu i interakcja z nimi
 * Bez rysowania mapy (rysowanie mapy jest w EGPanel)
 */
public class EGFrame extends JFrame {
    private EGPanel egPanel;                        // panel z wizualizacją stepu i dżungli

    public JPanel getEgPanel() {
        return egPanel;
    }

    private JPanel controlPanel;                    // górny panel z klawiszami etc.
    private JLabel labelDay;                        // labelka do wyśw. nr dnia
    private JLabel labelGenome;                     // labelka do wyśw. genomu
    private JButton buttonStopStart;                // klawisz pauzowania i wznawiania symulacji w tym oknie
    private JButton buttonDominantGenomeAnimals;    // klawisz zaznaczenia zwierząt z dominującym genomem
    private JButton buttonStat;                     // klawisz statystyk
    private JLabel labelMaxNTrack;                  // labelka do wyśw. opisu pola edycyjnego maks. nr dnia do śledzenia
    private JTextField textFieldMaxNTrack;          // pole edycyjne maks. nr dnia do śledzenia

    private Engine engine;                          // "silnik" pracujący w tym oknie

    public Engine getEngine() {
        return engine;
    }

    private TrackingInfo trackingInfo;

    public TrackingInfo getTrackingInfo() {
        return trackingInfo;
    }

    private Animal getSelectedAnimal() {
        return egPanel.getSelectedAnimal();
    }

    private void setSelectedAnimal(Animal selectedAnimal) {
        egPanel.setSelectedAnimal(selectedAnimal);
    }

    private Map<String, List<Animal>> getDominantGenomeAnimals() {
        return egPanel.getDominantGenomeAnimals();
    }

    private void setDominantGenomeAnimals(Map<String, List<Animal>> dominantGenomeAnimals) {
        egPanel.setDominantGenomeAnimals(dominantGenomeAnimals);
    }

    public EGFrame(Engine engine, Point xy) {
        super("Sawanna i dżungla");

        this.engine = engine;
        trackingInfo = new TrackingInfo();

        egPanel = createEGPanel();              // to jak najwcześniej bo następne pośrednio korzystają z tego panela
        add(egPanel, BorderLayout.CENTER);

        controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        add(Box.createRigidArea(new Dimension(6, 0)), BorderLayout.WEST);
        add(Box.createRigidArea(new Dimension(6, 0)), BorderLayout.EAST);
        add(Box.createRigidArea(new Dimension(0, 6)), BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(xy);
        setVisible(true);
    }

    /**
     * Panel kontrolek
     *
     * @return
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(Box.createRigidArea(new Dimension(6, 0)));
        createDisplayDayControls(panel);
        panel.add(Box.createRigidArea(new Dimension(6, 0)));
        createButtonStartStop(panel);
        panel.add(Box.createRigidArea(new Dimension(6, 0)));
        createButtonDominantGenomeAnimals(panel);
        panel.add(Box.createRigidArea(new Dimension(6, 0)));
        createButtonStat(panel);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        createNTrackControls(panel);

        return panel;
    }

    /**
     * wyświetlanie epoki (dnia):
     */
    private void createDisplayDayControls(JPanel panel) {
        JLabel labelDayCaption = new JLabel("Dzień: ");
        panel.add(labelDayCaption);

        labelDay = new JLabel();
        Dimension width = new Dimension(40, 100);
        labelDay.setMinimumSize(width);
        labelDay.setMaximumSize(width);
        panel.add(labelDay);
    }

    /**
     * Klawisz start/stop:
     */
    private void createButtonStartStop(JPanel panel) {
        buttonStopStart = new JButton();
        Dimension width = new Dimension(100, 100);
        buttonStopStart.setMinimumSize(width);
        buttonStopStart.setMaximumSize(width);
        setPaused(false);

        buttonStopStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDominantGenomeAnimals(null); // zatrzymujemy wizualizację genomów dominujących
                setButtonCaptionDominantGenomeAnimals();

                // inicjacja śledzenia przy wznowieniu symulacji (jeżeli nie było aktywnego śledzenia lub zmieniło się zwierzę)

                if (getSelectedAnimal() != null && engine.getPaused()) {
                    int maxNTrack;

                    try {
                        maxNTrack = readMaxNTrack();
                    } catch (Exception ex) {
                        showMessageDialog(EGFrame.this, "Błędna zawartość pola z nr granicznym dnia do śledzenia");
                        return;
                    }

                    if (maxNTrack <= engine.getDay()) {
                        showMessageDialog(EGFrame.this,
                                "Nr graniczny dnia do śledzenia musi być większy od bieżącego dnia");
                        return;
                    }

                    trackingInfo.setMaxNTrack(maxNTrack);

                    if (getSelectedAnimal() != trackingInfo.getAnimal()) {
                        trackingInfo.setAnimal(getSelectedAnimal());
                        trackingInfo.setNTrack0(engine.getDay());
                        trackingInfo.setNumberOfChildren0(getSelectedAnimal().getNumberOfChildren());
                        trackingInfo.setNumberOfDescendants0(getSelectedAnimal().getNumberOfDescendants());
                    }
                }

                // wznawiamy lub pauzujemy symulację

                setPaused(!engine.getPaused());
            }
        });

        panel.add(buttonStopStart);
    }

    /**
     * Klawisz statystyk
     */
    private void createButtonStat(JPanel panel) {
        buttonStat = new JButton("Statystyki");
        panel.add(buttonStat);

        buttonStat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(true);

                StatDialog statDialog = new StatDialog(EGFrame.this, engine.getDayStats());
                statDialog.setVisible(true);
            }
        });
    }

    /**
     * Klawisz zwierząt z dominującym genomem
     */
    private void createButtonDominantGenomeAnimals(JPanel panel) {
        buttonDominantGenomeAnimals = new JButton();
        panel.add(buttonDominantGenomeAnimals);
        setButtonCaptionDominantGenomeAnimals();

        buttonDominantGenomeAnimals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPaused(true);    // stan dominujących genomów pobierany jest tylko na ten dzień

                if (getDominantGenomeAnimals() == null) {
                    Map<String, List<Animal>> dominantGenomeAnimals = engine.computeDominantGenomeAnimals();
                    if (dominantGenomeAnimals == null)
                        showMessageDialog(EGFrame.this, "Brak genomów o krotności większej niż jeden");
                    setDominantGenomeAnimals(dominantGenomeAnimals);
                } else
                    setDominantGenomeAnimals(null);

                setButtonCaptionDominantGenomeAnimals();

                egPanel.repaint();
            }
        });
    }

    /**
     * Kontroli śledzenia: labelka i edytor na parametr śledzenia (nr dnia lub pusty / zero - do śmierci) oraz labelka genomu
     */
    private void createNTrackControls(JPanel panel) {
        labelMaxNTrack = new JLabel("Śledź do dnia (pusty = do śm.): ");
        panel.add(labelMaxNTrack);

        panel.add(Box.createRigidArea(new Dimension(2, 0)));

        textFieldMaxNTrack = new JTextField(3);
        textFieldMaxNTrack.setMaximumSize(new Dimension(40, 100));
        panel.add(textFieldMaxNTrack);

        panel.add(Box.createRigidArea(new Dimension(6, 0)));

        labelGenome = new JLabel();
        panel.add(labelGenome);

        setTrackingControls(false, null);   // śledzenie początkowo wyłączone
    }

    /**
     * Panel mapy
     *
     * @return
     */
    private EGPanel createEGPanel() {
        EGPanel panel = new EGPanel(engine);

        panel.addMouseListener(new MouseListener() {
            // Obsługa kliknięcia myszką na zwierzęciu

            @Override
            public void mouseClicked(MouseEvent e) {
                Animal animal = panel.animalAt(e.getX(), e.getY());
                if (animal != null) {
                    // zatrzymujemy symulację, jeśli nie zatrzymano przed kliknięciem:

                    setPaused(true);

                    // jeśli śledzenie tego zwierzaka było włączone - wyłączamy, jeśli nie było - włączamy

                    if (animal == getSelectedAnimal()) {
                        setSelectedAnimal(null);
                        setTrackingControls(false, null);
                    } else {
                        setSelectedAnimal(animal);
                        setTrackingControls(true, animal.getGenome().toString());
                    }

                    panel.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        return panel;
    }

    /**
     * Odczyt granicznego nr dnia do śledzenia z kontrolki edycyjnej
     *
     * @return
     */
    private int readMaxNTrack() {
        String maxNTrackStr = textFieldMaxNTrack.getText().trim();              // obcięta ze spacji zawartość pola

        if (maxNTrackStr.isEmpty())
            return Integer.MAX_VALUE;                                           // puste pole => śledzenie "do śmierci"
        else
            return Integer.parseInt(maxNTrackStr);
    }

    private void setTrackingControls(boolean on, String genomeStr) {
        labelMaxNTrack.setVisible(on);
        textFieldMaxNTrack.setText(null);
        textFieldMaxNTrack.setVisible(on);
        labelGenome.setText(on ? "Genom: " + genomeStr : null);
        labelGenome.setVisible(on);
    }

    /**
     * Zatrzymanie / wznowienie silnika i jednoczesna aktualizacja opisu klawisza
     */
    public void setPaused(boolean paused) {
        engine.setPaused(paused);
        buttonStopStart.setText(engine.getPaused() ? "Wznów" : "Wstrzymaj");
    }

    /**
     * Zatrzymanie / wznowienie wizualizacji genomów dominujących
     */
    private void setButtonCaptionDominantGenomeAnimals() {
        buttonDominantGenomeAnimals.setText(getDominantGenomeAnimals() == null ? "Pokaż domin. genom" : "Wygaś domin. genom");
    }

    /**
     * Dla wygody
     */
    public boolean getPaused() {
        return engine.getPaused();
    }

    /**
     * Do aktualizacji spoza klasy labelek panela kontrolnego
     */
    public void updateLabels() {
        labelDay.setText(Integer.toString(engine.getDay()));
        labelGenome.setText(getSelectedAnimal() != null ? "Genom: " + getSelectedAnimal().getGenome().toString() : null);
    }

    /**
     * Wyświetlenie podsumowania śledzenia zwierzęcia po zatrzymaniu programu w trybie śledzenia
     */
    public void showTrackingSummary() {
        trackingInfo.setNTrack(getEngine().getDay());
        TrackingSummaryDialog trackingSummaryDialog = new TrackingSummaryDialog(this, trackingInfo);
        trackingSummaryDialog.setVisible(true);
    }

    /**
     * Deaktywacja śledzenia
     */
    public void resetTracking() {
        trackingInfo.reset();
        setSelectedAnimal(null);
        setTrackingControls(false, null);
    }
}
