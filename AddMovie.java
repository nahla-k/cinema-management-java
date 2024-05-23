import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMovie extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);
    Font font4 = new Font("Tahoma", Font.BOLD, 14);


    changes change = new changes();
    private File selectedImageFile;

    String[] Genre = { "Action", "Aventure", "Comédie", "Drame", "Fantaisie", "Horreur", "Romance",
            "Sciencefiction", "Thriller", "Autre" };
    String[] Version = { "VO", "VF" };

    AddMovie(JTable table, JComboBox idTextField) {

        getContentPane().setBackground(new Color(232, 232, 232));
        setLayout(null);
        setLocation(200, 25);
        setSize(400, 650);
        setVisible(true);

        // adding red panel
        Color[] redColors = { Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18") };
        float[] redFractions = { 0.0f, 0.52f, 1.0f };
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(5, 5, 375, 600);
        getContentPane().add(redPanel);

        // redpanel horizontal center
        int rightlabelWidth = 200;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        // NEW MOVIE label
        JLabel scroomsLabel = new JLabel("NEW MOVIE");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);

        scroomsLabel.setBounds(rcenter, 50, 300, 22);
        redPanel.add(scroomsLabel);

        // MOVIE NAME label
        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 120, 100, 18);
        redPanel.add(nameLabel);

        // TIME label
        JLabel timeLabel = new JLabel("Time : ");
        timeLabel.setFont(font3);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(25, 170, 100, 18);
        redPanel.add(timeLabel);

        // GENRE label
        JLabel genreLabel = new JLabel("Genre : ");
        genreLabel.setFont(font3);
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setBounds(25, 220, 100, 18);
        redPanel.add(genreLabel);

        // VERSION label
        JLabel versionLabel = new JLabel("Version : ");
        versionLabel.setFont(font3);
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setBounds(25, 270, 100, 18);
        redPanel.add(versionLabel);

        // MOVIE NAME textfield
        JTextField nameTextField = new JTextField();
        nameTextField.setFont(font3);
        nameTextField.setBackground(new Color(67, 67, 67));
        nameTextField.setForeground(Color.WHITE);
        nameTextField.setBounds(120, 120, 120, 18);
        redPanel.add(nameTextField);

        // TIME textfield
        SpinnerDateModel modelTime = new SpinnerDateModel();
        modelTime.setCalendarField(Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(modelTime);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(120, 170, 120, 18);
        redPanel.add(timeSpinner);

        // GENRE combobox
        JComboBox<String> genreComboBox = new JComboBox<>(Genre);
        genreComboBox.setFont(font3);
        genreComboBox.setBackground(new Color(67, 67, 67));
        genreComboBox.setForeground(Color.WHITE);
        genreComboBox.setBounds(120, 220, 120, 18);
        redPanel.add(genreComboBox);

        // VERSION combobox
        JComboBox<String> versionComboBox = new JComboBox<>(Version);
        versionComboBox.setFont(font3);
        versionComboBox.setBackground(new Color(67, 67, 67));
        versionComboBox.setForeground(Color.WHITE);
        versionComboBox.setBounds(120, 270, 120, 18);
        redPanel.add(versionComboBox);


        // PHOTO panel
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(null);
        photoPanel.setBounds(120, 320, 120, 180);
        photoPanel.setBackground(new Color(67, 67, 67));
        redPanel.add(photoPanel);

        // ADD PHOTO button
        JButton chooseButton = new JButton("+");
        chooseButton.setBounds(20, 370, 60, 60);
        chooseButton.setFont(font1);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setForeground(Color.WHITE);
        redPanel.add(chooseButton);

        // SAVE button
        JButton saveButton = new JButton("SAVE");
        saveButton.setBounds(215, 535, 100, 40);
        saveButton.setFont(font2);
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        redPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titre = nameTextField.getText();
                String vovf = (String) versionComboBox.getSelectedItem();
                String genrefilm = (String) genreComboBox.getSelectedItem();
                java.util.Date pickedTime = (java.util.Date) timeSpinner.getValue();
                java.sql.Time duree = new java.sql.Time(pickedTime.getTime()); // Convert util.Date to sql.Time

                byte[] imageBytes = null;
                if (selectedImageFile != null) {
                    try (FileInputStream fis = new FileInputStream(selectedImageFile)) {
                        imageBytes = new byte[(int) selectedImageFile.length()];
                        fis.read(imageBytes);
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "Error processing the image file.",
                                "File Error", JOptionPane.ERROR_MESSAGE);
                        ioException.printStackTrace();
                    }
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String durationStr = timeFormat.format(duree);


                try {
                    change.add("films", titre, vovf, genrefilm, duree,imageBytes);
                    change.showAll(table, "films");
                    change.populateMovieIDs(idTextField);
                    nameTextField.setText("");
                    timeSpinner.setValue(new java.util.Date());
                    genreComboBox.setSelectedIndex(-1);
                    versionComboBox.setSelectedIndex(-1);
                    photoPanel.removeAll();
                    photoPanel.repaint();

                }
                catch (Exception exception){
                    JOptionPane.showMessageDialog(null,exception.getMessage());
                }

            }




        });

        // CANCEL button
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBounds(50, 535, 100, 40);
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


        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == chooseButton) {
                    // Create a file chooser dialog
                    JFileChooser fileChooser = new JFileChooser();

                    // Set the file filter to show only image files (png, jpg, gif)
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "Image Files", "jpg", "jpeg", "png", "gif");
                    fileChooser.setFileFilter(filter);
                    fileChooser.setLocation(200, 25);

                    // Show the file chooser dialog and capture the user's choice
                    int result = fileChooser.showOpenDialog(chooseButton);

                    // If the user selects a file, extract the file path
                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedImageFile = fileChooser.getSelectedFile();
                        String filePath = selectedImageFile.getAbsolutePath();
                        ImageIcon selectedImageIcon = new ImageIcon(filePath);
                        JLabel selectedImageLabel = new JLabel(selectedImageIcon);
                        selectedImageLabel.setBounds(0, 0, 120, 180);
                        photoPanel.removeAll();
                        photoPanel.add(selectedImageLabel);
                        photoPanel.revalidate();
                        photoPanel.repaint();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {

    }
}
