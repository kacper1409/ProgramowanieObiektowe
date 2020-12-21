package agh.cs.lab8.graph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TrackingSummaryDialog extends JDialog {
    public TrackingSummaryDialog(Frame parent, TrackingInfo trackingInfo) {
        super(parent);

        setTitle("Podsumowanie śledzenia zwierzęcia");

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));

        int maxNTrack = trackingInfo.getMaxNTrack();

        textArea.append("          Początek śledzenia w dniu: " + trackingInfo.getNTrack0() + "\n");
        textArea.append("  Planowany koniec śledzenia w dniu: " + (maxNTrack == Integer.MAX_VALUE ? "bez ogr. (do śmierci)" : trackingInfo.getMaxNTrack()) + "\n");
        textArea.append("Rzeczywisty koniec śledzenia w dniu: " + trackingInfo.getNTrack() + "\n");
        textArea.append("                      Zmarło w dniu: " + (trackingInfo.getAnimal().getEnergy() <= 0.0 ? trackingInfo.getNTrack() : "żyje") + "\n");

        textArea.append("\n");
        textArea.append("          Liczba dzieci:\n");
        textArea.append("  Na początku śledzenia: " + trackingInfo.getNumberOfChildren0() + "\n");
        textArea.append("               Przyrost: " + (trackingInfo.getAnimal().getNumberOfChildren() - trackingInfo.getNumberOfChildren0()) + "\n");
        textArea.append("     Na końcu śledzenia: " + trackingInfo.getAnimal().getNumberOfChildren() + "\n");

        textArea.append("\n");
        textArea.append("        Liczba potomków:\n");
        textArea.append("  Na początku śledzenia: " + trackingInfo.getNumberOfDescendants0() + "\n");
        textArea.append("               Przyrost: " + (trackingInfo.getAnimal().getNumberOfDescendants() - trackingInfo.getNumberOfDescendants0()) + "\n");
        textArea.append("     Na końcu śledzenia: " + trackingInfo.getAnimal().getNumberOfDescendants() + "\n");

        var okButton = new JButton("OK");
        okButton.addActionListener(event -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(textArea);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(BorderLayout.CENTER, centerPanel);
        add(BorderLayout.SOUTH, buttonPanel);

        pack();

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLocationRelativeTo(parent);
    }
}
