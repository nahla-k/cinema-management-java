import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);

    String[] type = { "ADMIN", "EMPLOYEE" };
    boolean isAdmin;
    changes changes = new changes();

    AddUser(boolean isAdmin) {
        this.isAdmin = isAdmin;
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
        int rightlabelWidth = 100;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        /*
         * // adding icon
         * ImageIcon scrIcon = new
         * ImageIcon("C://Users//HP//Downloads//PFCfinal (3)//PFCfinal//icons//AddUserIcon.png"
         * );
         * JLabel scrLabel = new JLabel(scrIcon);
         * scrLabel.setBounds(50, 220, 150, 150);
         * redPanel.add(scrLabel);
         */

        // ADD USER label
        JLabel adduserLabel = new JLabel("ADD USER");
        adduserLabel.setBounds(rcenter, 40, 150, 22);
        adduserLabel.setFont(font1);
        adduserLabel.setForeground(Color.WHITE);
        redPanel.add(adduserLabel);

        // USER TYPE
        JLabel typeLabel = new JLabel("Type : ");
        typeLabel.setBounds(25, 100, 100, 18);
        typeLabel.setFont(font3);
        typeLabel.setForeground(Color.WHITE);
        redPanel.add(typeLabel);

        // USERNAME label
        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setBounds(25, 150, 100, 18);
        usernameLabel.setFont(font3);
        usernameLabel.setForeground(Color.WHITE);
        redPanel.add(usernameLabel);

        // PASSWORD label
        JLabel pswdLabel = new JLabel("Password : ");
        pswdLabel.setBounds(25, 200, 100, 18);
        pswdLabel.setFont(font3);
        pswdLabel.setForeground(Color.WHITE);
        redPanel.add(pswdLabel);

        // CONFIRM PASSWORD label
        JLabel cfpswdLabel = new JLabel("Confirm password : ");
        cfpswdLabel.setBounds(25, 250, 150, 18);
        cfpswdLabel.setFont(font3);
        cfpswdLabel.setForeground(Color.WHITE);
        redPanel.add(cfpswdLabel);

        // TYPE combobox
        JComboBox<String> typeComboBox = new JComboBox<>(type);
        typeComboBox.setFont(font3);
        typeComboBox.setBackground(new Color(67, 67, 67));
        typeComboBox.setForeground(Color.WHITE);
        typeComboBox.setBounds(170, 100, 150, 22);
        redPanel.add(typeComboBox);

        // USERNAME textfield
        JTextField usernameTextField = new JTextField();
        usernameTextField.setFont(font3);
        usernameTextField.setBackground(new Color(67, 67, 67));
        usernameTextField.setForeground(Color.WHITE);
        usernameTextField.setBounds(170, 150, 150, 22);
        redPanel.add(usernameTextField);

        // PASSWORD passwordfield
        JPasswordField pswdPasswordField = new JPasswordField();
        pswdPasswordField.setFont(font3);
        pswdPasswordField.setBackground(new Color(67, 67, 67));
        pswdPasswordField.setForeground(Color.WHITE);
        pswdPasswordField.setBounds(170, 200, 150, 22);
        redPanel.add(pswdPasswordField);

        // CONFIRM PASSWORD passwordfield
        JPasswordField cfpswdPasswordField = new JPasswordField();
        cfpswdPasswordField.setFont(font3);
        cfpswdPasswordField.setBackground(new Color(67, 67, 67));
        cfpswdPasswordField.setForeground(Color.WHITE);
        cfpswdPasswordField.setBounds(170, 250, 150, 22);
        redPanel.add(cfpswdPasswordField);

        // ADD BUTTON
        JButton addButton = new JButton("ADD");
        addButton.setBounds(rcenter + 150, 530, 140, 40);
        addButton.setFont(font3);
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.WHITE);
        redPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = usernameTextField.getText();
                    String password = pswdPasswordField.getText();
                    String cfpswd = cfpswdPasswordField.getText();
                    if (cfpswd.equals(password)) {
                        String type = typeComboBox.getSelectedItem().toString();
                        changes.signUp(username, password, type);
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                    JOptionPane.showMessageDialog(null, "error, user is not added");
                }
            }
        });

        // CANCEL BUTTON
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(rcenter -100, 320, 100, 40);
        cancelButton.setFont(font2);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        redPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new ShowAllUsers(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // SAVE button
        JButton saveButton = new JButton("SAVE");
        saveButton.setBounds(rcenter +100, 320, 100, 40);
        saveButton.setFont(font2);
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        redPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { String username =usernameTextField.getText();
                    String password =pswdPasswordField.getText();
                    String cfpswd=cfpswdPasswordField.getText();
                    if (cfpswd.equals(password))
                    {
                    String type = typeComboBox.getSelectedItem().toString();
                    changes.signUp(username, password, type );}
                    else {
                        JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    }
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,"error, user is not added");
                }
            }
        });


    }

    public static void main(String[] args) {

    }

}