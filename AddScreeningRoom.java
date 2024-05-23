import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddScreeningRoom extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);
    changes changes = new changes();

    AddScreeningRoom(JTable table,JComboBox idComboBox) {
        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(200, 25);
        setSize(400, 440);
        setVisible(true);

        // adding red panel
        Color[] redColors = { Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18") };
        float[] redFractions = { 0.0f, 0.52f, 1.0f };
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(5, 5, 375, 390);
        getContentPane().add(redPanel);

        // redpanel horizontal center
        int rightlabelWidth = 200;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        // NEW SCREENING ROOM label
        JLabel scroomsLabel = new JLabel("NEW SCREENING ROOM");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);

        scroomsLabel.setBounds(rcenter - 25, 50, 300, 22);
        redPanel.add(scroomsLabel);


        // SCREENING ROOM NUMBER label
//        JLabel numberLabel = new JLabel("Number : ");
//        numberLabel.setFont(font3);
//        numberLabel.setForeground(Color.WHITE);
//        numberLabel.setBounds(25, 150, 100, 18);
//        redPanel.add(numberLabel);

        // SCREENING ROOM CAPACITY label
        JLabel capacityLabel = new JLabel("Capacity : ");
        capacityLabel.setFont(font3);
        capacityLabel.setForeground(Color.WHITE);
        capacityLabel.setBounds(25, 230, 100, 18);
        redPanel.add(capacityLabel);

        // number textfield
//        JTextField nbrTextField = new JTextField();
//        nbrTextField.setFont(font3);
//        nbrTextField.setBackground(new Color(67, 67, 67));
//        nbrTextField.setForeground(Color.WHITE);
//        nbrTextField.setBounds(120, 150, 100, 18);
//        nbrTextField.setEditable(false);
//        redPanel.add(nbrTextField);

        // capacity textfield
        JTextField capacityTextField = new JTextField();
        capacityTextField.setFont(font3);
        capacityTextField.setBackground(new Color(67, 67, 67));
        capacityTextField.setForeground(Color.WHITE);
        capacityTextField.setBounds(120, 230, 100, 20);
        redPanel.add(capacityTextField);

        // SAVE button
        JButton saveButton = new JButton("SAVE");
        saveButton.setBounds(215, 320 , 100, 40);
        saveButton.setFont(font2);
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        redPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String capacity = capacityTextField.getText();
                    changes change = new changes();
                    change.add("salles", capacity);
                    // Rafraîchir le tableau pour afficher les données mises à jour
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Effacer les données existantes
                    change.showAll(table, "salles");
                    changes.populateSalleNumbers(idComboBox);
                    // Reset all combo boxes to an unselected state
                    idComboBox.setSelectedIndex(-1);

                    // Clear all text fields
                    capacityTextField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while adding the screening room : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // CANCEL button
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(50, 320 , 100, 40);
        cancelButton.setFont(font2);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        redPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {

    }
}
