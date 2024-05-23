import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowAllUsers extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);

    String[] type = { "ADMIN", "EMPLOYEE" };
    boolean isAdmin;
    private DefaultTableModel model;
    changes changes = new changes();

    ShowAllUsers(boolean isAdmin) {
        this.isAdmin = isAdmin;
        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(200, 25);
        setSize(900, 650);
        setVisible(true);

        // adding red panel
        Color[] redColors = { Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18") };
        float[] redFractions = { 0.0f, 0.52f, 1.0f };
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(230, 5, 650, 600);
        getContentPane().add(redPanel);

        Color[] grayColors = { Color.decode("#1f1c18"), Color.decode("#414345"), Color.decode("#1f1c18") };
        float[] grayFractions = { 0.0f, 0.5f, 1.0f };
        gradient grayPanel = new gradient(grayColors, grayFractions);
        grayPanel.setLayout(null);
        grayPanel.setBounds(5, 5, 220, 600);
        getContentPane().add(grayPanel);

        // redpanel horizontal center
        int rightlabelWidth = 140;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        // graypanel horizontal center
        int leftlabelWidth = 157;
        int graypanelWidth = grayPanel.getWidth();
        int gcenter = (graypanelWidth - leftlabelWidth) / 2;

        // graypanel vertical center
        int graypanelHeight = grayPanel.getHeight();
        int buttonHeight = 40;
        int totalButtonHeight = buttonHeight * 5;
        int vCenter = (graypanelHeight - totalButtonHeight) / 2;

        // adding icon
        ImageIcon ticketsIcon = new ImageIcon("C://Users//HP//Downloads//PFCfinal (3)//PFCfinal//icons//Users.png");
        JLabel tckLabel = new JLabel(ticketsIcon);
        tckLabel.setBounds(gcenter + 5, 30, 150, 150);
        grayPanel.add(tckLabel);

        // USER label
        JLabel adduserLabel = new JLabel("USERS");
        adduserLabel.setBounds(290, 50, 100, 30);
        adduserLabel.setFont(font1);
        adduserLabel.setForeground(Color.WHITE);
        redPanel.add(adduserLabel);

        String[] columnNames = { "Username", "Type" };

        model = new DefaultTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);

        JTable table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(false);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(270, 100, 350, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);

        // USERNAME label
        JLabel nameLabel = new JLabel("Username : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 250, 100, 18);
        redPanel.add(nameLabel);

        // TYPE label
        JLabel numberLabel = new JLabel("Type : ");
        numberLabel.setFont(font3);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setBounds(25, 330, 100, 18);
        redPanel.add(numberLabel);

        // USERNAME textfield
        JTextArea usernamTextArea = new JTextArea();
        usernamTextArea.setFont(font3);
        usernamTextArea.setBackground(new Color(67, 67, 67));
        usernamTextArea.setForeground(Color.WHITE);
        usernamTextArea.setBounds(120, 250, 100, 18);
        redPanel.add(usernamTextArea);

        // TYPE textfield
        JTextArea typeTextArea = new JTextArea();
        typeTextArea.setFont(font3);
        typeTextArea.setBackground(new Color(67, 67, 67));
        typeTextArea.setForeground(Color.WHITE);
        typeTextArea.setBounds(120, 330, 100, 18);
        redPanel.add(typeTextArea);

        // ADD USER button
        JButton bookticket = new JButton("ADD USER");
        int bookButtonY = vCenter + 0 * (buttonHeight + 10);
        bookticket.setBounds(gcenter, bookButtonY + 20, 160, 40);
        bookticket.setFont(font2);
        bookticket.setBackground(Color.BLACK);
        bookticket.setForeground(Color.WHITE);
        grayPanel.add(bookticket);
        bookticket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(true);
                    new AddUser(isAdmin); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // EDIT button
        JButton cancelButton = new JButton("EDIT");
        int cancelButtonY = vCenter + 1 * (buttonHeight + 10);
        cancelButton.setBounds(gcenter, cancelButtonY + 20, 160, 40);
        cancelButton.setFont(font2);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        grayPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // DELETE button
        JButton ticketsleftButton = new JButton("DELETE");
        int ticketsleftButtonY = vCenter + 2 * (buttonHeight + 10);
        ticketsleftButton.setBounds(gcenter, ticketsleftButtonY + 20, 160, 40);
        ticketsleftButton.setFont(font2);
        ticketsleftButton.setBackground(Color.BLACK);
        ticketsleftButton.setForeground(Color.WHITE);
        grayPanel.add(ticketsleftButton);
        ticketsleftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });


        // BACK button
        JButton backButton = new JButton("BACK");
        int backButtonY = vCenter + 4 * (buttonHeight + 30);
        backButton.setBounds(gcenter + 10, backButtonY, 140, 40);
        backButton.setFont(font2);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        grayPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new HomePage(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) {

    }
}
