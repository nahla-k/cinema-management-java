import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    Font font2 = new Font("Bookman Old Style", Font.PLAIN, 20);
    Font font1 = new Font("Tahoma", Font.BOLD, 16);
    boolean isAdmin ;
    HomePage(boolean isAdmin) {
        this.isAdmin = isAdmin;
        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(200, 25);
        setSize(900, 650);
        setVisible(true);

        // adding red panel
        Color[] redColors = {Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18")};
        float[] redFractions = {0.0f, 0.52f, 1.0f};
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(230, 5, 650, 600);
        getContentPane().add(redPanel);

        Color[] grayColors = {Color.decode("#1f1c18"), Color.decode("#414345"), Color.decode("#1f1c18")};
        float[] grayFractions = {0.0f, 0.5f, 1.0f};
        gradient grayPanel = new gradient(grayColors, grayFractions);
        grayPanel.setLayout(null);
        grayPanel.setBounds(5, 5, 220, 600);
        getContentPane().add(grayPanel);

        // adding icon
        ImageIcon homepIcon = new ImageIcon( "C://Users//HP//Downloads//PFCfinal (3)//PFCfinal//icons//HomepageIcon.png");
        JLabel hpLabel = new JLabel(homepIcon);
        hpLabel.setBounds(102, 120, 400, 400);
        redPanel.add(hpLabel);

        // welcome label
        JLabel welcomLabel = new JLabel("WELCOME TO YOUR CINEMA MANAGEMENT SYSTEM");
        welcomLabel.setFont(font2);
        welcomLabel.setForeground(Color.WHITE);

        // redpanel horizontal center
        int labelWidth = 550;
        int panelWidth = redPanel.getWidth();
        int rcenter = (panelWidth - labelWidth) / 2;

        welcomLabel.setBounds(rcenter, 30, labelWidth, 22);
        redPanel.add(welcomLabel);

        // screening rooms button
        JButton scroomsButton = new JButton("SCREENING ROOMS");
        scroomsButton.setBounds(12, 100, 200, 40);
        scroomsButton.setFont(font1);
        scroomsButton.setBackground(Color.BLACK);
        scroomsButton.setForeground(Color.WHITE);
        grayPanel.add(scroomsButton);

        scroomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new ScreeningRooms( isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // screenings button
        JButton scButton = new JButton("SCREENINGS");
        scButton.setBounds(12, 160, 200, 40);
        scButton.setFont(font1);
        scButton.setBackground(Color.BLACK);
        scButton.setForeground(Color.WHITE);
        grayPanel.add(scButton);

        scButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new Screenings(isAdmin);

                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // movies button
        JButton moviesButton = new JButton("MOVIES");
        moviesButton.setFont(font1);
        moviesButton.setBounds(12, 220, 200, 40);
        moviesButton.setBackground(Color.BLACK);
        moviesButton.setForeground(Color.WHITE);
        grayPanel.add(moviesButton);

        moviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new Movies(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // TICKETS button
        JButton ticketsButton = new JButton("TICKETS");
        ticketsButton.setFont(font1);
        ticketsButton.setBounds(12, 280, 200, 40);
        ticketsButton.setBackground(Color.BLACK);
        ticketsButton.setForeground(Color.WHITE);
        grayPanel.add(ticketsButton);

        ticketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new Tickets(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

        // ADD USER button
        JButton adduserButton = new JButton("USERS");
        adduserButton.setFont(font1);
        adduserButton.setBounds(12, 340, 200, 40);
        adduserButton.setBackground(Color.BLACK);
        adduserButton.setForeground(Color.WHITE);
        grayPanel.add(adduserButton);
        //adduserButton.setVisible(false);
        if (!isAdmin) {

            adduserButton.setVisible(false);
        }
        adduserButton.addActionListener(new ActionListener() {
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

        // LOGOUT button
        JButton logoutButton = new JButton("LOG OUT");
        logoutButton.setFont(font1);
        logoutButton.setBounds(35, 470, 150, 40);
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setForeground(Color.WHITE);
        grayPanel.add(logoutButton);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // System.exit(0);
                    setVisible(false);
                    new Login();
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public static void main(String[] args) {
        try {
            // Set FlatLaf as the look and feel
            try {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                new HomePage(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
        //new HomePage();
    }
