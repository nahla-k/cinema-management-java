import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class ticketsLeft extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);
    changes changes = new changes();

    ticketsLeft() {
        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(240, 35);
        setSize(400, 440);
        setVisible(true);
        setResizable(false);
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
        JLabel scroomsLabel = new JLabel("TICKETS LEFT");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);

        scroomsLabel.setBounds(rcenter +37, 50, 300, 22);
        redPanel.add(scroomsLabel);


        JLabel nameLabel = new JLabel("Movie : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 130, 150, 18);
        redPanel.add(nameLabel);

        // SCREENING ID label
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(font3);
        idLabel.setForeground(Color.WHITE);
        idLabel.setBounds(25, 190, 150, 18);
        redPanel.add(idLabel);

        // TICKETS LEFT label
        JLabel tcktsleftLabel = new JLabel("Tickets left : ");
        tcktsleftLabel.setFont(font3);
        tcktsleftLabel.setForeground(Color.WHITE);
        tcktsleftLabel.setBounds(25, 250, 150, 18);
        redPanel.add(tcktsleftLabel);

        // MOVIE NAME combobox
        JComboBox<Film> nameComboBox = new JComboBox<>();
        changes.populateNameDiffusions(nameComboBox);
        nameComboBox.setFont(font3);
        nameComboBox.setBackground(new Color(67, 67, 67));
        nameComboBox.setForeground(Color.WHITE);
        nameComboBox.setBounds(120, 130, 150, 18);

        redPanel.add(nameComboBox);

        // ID combobox
        JComboBox<Integer> idComboBox = new JComboBox<>();
        idComboBox.setFont(font3);
        idComboBox.setBackground(new Color(67, 67, 67));
        idComboBox.setForeground(Color.WHITE);
        idComboBox.setBounds(120, 190, 120, 18);
        redPanel.add(idComboBox);

        // TICKETS LEFT textarea
        JTextArea ticketsleftTextArea = new JTextArea();
        ticketsleftTextArea.setFont(font3);
        ticketsleftTextArea.setBackground(new Color(67, 67, 67));
        ticketsleftTextArea.setForeground(Color.WHITE);
        ticketsleftTextArea.setBounds(120, 250, 80, 18);
        ticketsleftTextArea.setEditable(false);
        redPanel.add(ticketsleftTextArea);

        nameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Film selectedMovie = (Film) nameComboBox.getSelectedItem();
                if (selectedMovie != null) {
                    idComboBox.removeAllItems();
                    changes.populateDiffusionIDs(idComboBox, selectedMovie.getId());
                }
            }
        });

        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedDiffusionId = (Integer) idComboBox.getSelectedItem();
                if (selectedDiffusionId != null) {
                    // Retrieve the number of left tickets for the selected diffusion
                    int leftTickets = changes.getLeftTicketsNbr(selectedDiffusionId);
                    // Display the price and left ticket number in JTextAreas
                    ticketsleftTextArea.setText(String.valueOf(leftTickets));
                    
                } else {
                    // Clear or reset the fields if there is no selection
    
                    ticketsleftTextArea.setText("");
                    
                }
            }
            
        });

        // SAVE button
        JButton backButton = new JButton("BACK");
        backButton.setBounds(rcenter + 50, 320 , 100, 40);
        backButton.setFont(font2);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        redPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
            }
            
        });


        
    }

    public static void main(String[] args) {

    }
}

