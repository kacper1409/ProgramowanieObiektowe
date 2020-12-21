package agh.cs.lab8.graph;

import agh.cs.lab8.engine.DayStat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class StatDialog extends JDialog {
    public StatDialog(Frame parent, List<DayStat> dayStats) {
        super(parent);

        setTitle("Statystyki");

        // fragment z danymi stat.:

        JTextArea textArea = new JTextArea(32, 128);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));

        createContent(textArea, dayStats);
        textArea.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(scroll);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // panel klawiszy:

        var okButton = new JButton("OK");
        okButton.addActionListener(event -> dispose());

        var fileButton = new JButton("Do pliku TXT");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("TXT", "txt");
                fileChooser.setFileFilter(filter);
                fileChooser.showDialog(StatDialog.this, "Wybierz nazwę pliku do zapisu");
                File workingDirectory = new File(System.getProperty("user.dir"));
                fileChooser.setCurrentDirectory(workingDirectory);
                fileChooser.setVisible(true);

                File file = fileChooser.getSelectedFile();
                String fileName = file.getPath();

                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(fileName));
                    writer.write(textArea.getText());
                    writer.close();
                } catch (IOException ex) {
                    showMessageDialog(StatDialog.this, "Problem z zapisem pliku:\n" + ex.getMessage());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(fileButton);

        // dodanie obu paneli do dialogu:

        add(BorderLayout.CENTER, centerPanel);
        add(BorderLayout.SOUTH, buttonPanel);

        pack();

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLocationRelativeTo(parent);
    }

    private void createContent(JTextArea textArea, List<DayStat> dayStats) {
        for (DayStat dayStat : dayStats) {
            textArea.append(dayStat.toStringFormatted());
            textArea.append("\n");
        }
    }
}
