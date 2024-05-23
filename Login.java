import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.FlatLightLaf;


public class Login extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Segoe UI", Font.PLAIN, 20);
    Font font3 = new Font("Tahoma", Font.BOLD, 16);
    changes changes = new changes();
    Login() {
        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(300, 25);
        setSize(700, 505);
        setVisible(true);

        // adding red panel
        /*JPanel redPanel = new JPanel();
        redPanel.setLayout(null);
        redPanel.setBounds(5, 5, 675, 457);
        redPanel.setBackground(new Color(75, 15, 15));
        add(redPanel);*/

        Color[] redColors = {Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18")};
        float[] redFractions = {0.0f, 0.52f, 1.0f};
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(5, 5, 675, 457);
        getContentPane().add(redPanel);

        
        // redpanel horizontal center
        int rightlabelWidth = 150;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        // adding icon
        ImageIcon icon = new ImageIcon("C:/Users/Asus/OneDrive/Images/LoginIcon.png");
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(40, 150, 150, 150);
        redPanel.add(iconLabel);

        // ADD USER label
        JLabel adduserLabel = new JLabel("LOG IN");
        adduserLabel.setBounds(rcenter + 50, 50, 200, 30);
        adduserLabel.setFont(font1);
        adduserLabel.setForeground(Color.WHITE);
        redPanel.add(adduserLabel);

        // USERNAME label
        JLabel usernameLabel = new JLabel("Username : ");
        usernameLabel.setBounds(250, 170, 300, 30);
        usernameLabel.setFont(font2);
        usernameLabel.setForeground(Color.WHITE);
        redPanel.add(usernameLabel);

        // PASSWORD label
        JLabel pswdLabel = new JLabel("Password : ");
        pswdLabel.setBounds(250, 250, 300, 30);
        pswdLabel.setFont(font2);
        pswdLabel.setForeground(Color.WHITE);
        redPanel.add(pswdLabel);

        // USERNAME textfield
        JTextField usernameTextField = new JTextField();
        usernameTextField.setFont(font3);
        usernameTextField.setBackground(new Color(67, 67, 67));
        usernameTextField.setForeground(Color.WHITE);
        usernameTextField.setBounds(380, 170, 245, 30);
        redPanel.add(usernameTextField);

        // PASSWORD passwordfield
        JPasswordField pswdPasswordField = new JPasswordField();
        pswdPasswordField.setFont(font3);
        pswdPasswordField.setBackground(new Color(67, 67, 67));
        pswdPasswordField.setForeground(Color.WHITE);
        pswdPasswordField.setBounds(380, 250, 245, 30);
        redPanel.add(pswdPasswordField);
        // LOGIN BUTTON
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(rcenter + 150, 370, 140, 40);
        loginButton.setFont(font3);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        redPanel.add(loginButton);
        loginButton.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameTextField.getText();
                String password = new String(pswdPasswordField.getPassword());
                String state =changes.logIn(username, password );

                if ( !state.equals("false")) {
                    setVisible(false);
                    HomePage homePage = new HomePage(state.equals("ADMIN"));
                    //homePage.setAdmin(state.equals("ADMIN")); // Set admin status based on the login response
                    homePage.setVisible(true);  // Show the home page
                    homePage.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Nom d'utilisateur ou mot de passe incorrect.");
                }
            }
        });

        // CANCEL BUTTON
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(rcenter - 150, 370, 140, 40);
        cancelButton.setFont(font3);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        redPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.exit(0);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) {
        new Login();
    }
}