import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddScreening extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);

    List<Integer> movieMap = new ArrayList<>();


    AddScreening(JTable table,JComboBox idComboBox) {
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

        // NEW SCREENING label
        JLabel scroomsLabel = new JLabel("NEW SCREENING");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);

        scroomsLabel.setBounds(100, 50, 300, 22);
        redPanel.add(scroomsLabel);

        changes change = new changes();

        // MOVIE NAME label
        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 120, 100, 20);
        redPanel.add(nameLabel);

        // SCREENING ROOM NUMBER label
        JLabel numberLabel = new JLabel("Room : ");
        numberLabel.setFont(font3);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setBounds(25, 180, 100, 20);
        redPanel.add(numberLabel);

        // DATE label
        JLabel dateLabel = new JLabel("Date : ");
        dateLabel.setFont(font3);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBounds(25, 240, 100, 20);
        redPanel.add(dateLabel);

        // STARTS AT label
        JLabel startLabel = new JLabel("Starts at : ");
        startLabel.setFont(font3);
        startLabel.setForeground(Color.WHITE);
        startLabel.setBounds(25, 300, 100, 20);
        redPanel.add(startLabel);

        // REMAINING TICKETS label
        JLabel remainingLabel = new JLabel("Tickets left : ");
        remainingLabel.setFont(font3);
        remainingLabel.setForeground(Color.WHITE);
        remainingLabel.setBounds(25, 360, 100, 20);
        redPanel.add(remainingLabel);

        // NAME combobox
        JComboBox<Film> nameComboBox = new JComboBox<>();
        nameComboBox.setFont(font3);
        nameComboBox.setBackground(new Color(67, 67, 67));
        nameComboBox.setForeground(Color.WHITE);
        nameComboBox.setBounds(120, 120, 120, 20);
        redPanel.add(nameComboBox);
         change.populateNameComboBox(nameComboBox);

        // REMAINING textfield
        JTextArea remainingTextField = new JTextArea();
        remainingTextField.setFont(font3);
        remainingTextField.setBackground(new Color(67, 67, 67));
        remainingTextField.setForeground(Color.WHITE);
        remainingTextField.setBounds(120, 360, 120, 20);
        redPanel.add(remainingTextField);

        // PRICE label
        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setFont(font3);
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setBounds(25, 420, 100, 20);
        redPanel.add(priceLabel);

        // PRICE text field
        JTextField priceTextField = new JTextField();
        priceTextField.setFont(font3);
        priceTextField.setBackground(new Color(67, 67, 67));
        priceTextField.setForeground(Color.WHITE);
        priceTextField.setBounds(120, 420, 120, 20);
        redPanel.add(priceTextField);

        // NUMBER combobox
        JComboBox<Integer> nbrComboBox = new JComboBox<>();
        nbrComboBox.setFont(font3);
        nbrComboBox.setBackground(new Color(67, 67, 67));
        nbrComboBox.setForeground(Color.WHITE);
        nbrComboBox.setBounds(120, 180, 120, 22);
        redPanel.add(nbrComboBox);
        change.populateScreeningRoomNumbersFromDB(nbrComboBox);
        nbrComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nbrComboBox.getSelectedItem() != null) {
                    int selectedRoomId = (int) nbrComboBox.getSelectedItem();
                    int capacity = change.getSalleCapacity(selectedRoomId);
                    remainingTextField.setText(String.valueOf(capacity)); // Set the capacity in the text field
                }
            }
        });

        // DATE
        JDateChooser date = new JDateChooser();
        date.setBounds(120, 240, 120, 22);
        redPanel.add(date);

        // Create a SpinnerDateModel with default values
        SpinnerDateModel modelTime = new SpinnerDateModel();
        modelTime.setCalendarField(Calendar.MINUTE);

        JSpinner timeSpinner = new JSpinner(modelTime);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(120, 300, 120, 18);
        redPanel.add(timeSpinner);



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
                Film selectedMovie = (Film) nameComboBox.getSelectedItem();
                if (selectedMovie == null) {
                    JOptionPane.showMessageDialog(null, "Please select a movie.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit early if no movie is selected
                }
                int movieId = selectedMovie.getId(); // Safely retrieve the movie ID

                Object selectedRoomObj = nbrComboBox.getSelectedItem();
                if (selectedRoomObj == null) {
                    JOptionPane.showMessageDialog(null, "Please select a room number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit early if no room number is selected
                }
                int roomId = (int) selectedRoomObj;

                double price;
                try {
                    price = Double.parseDouble(priceTextField.getText());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit early if the price is not valid
                }

                java.util.Date utilDate = date.getDate();
                java.sql.Date sqlDate = null;
                if (utilDate != null) {
                    sqlDate = new java.sql.Date(utilDate.getTime()); // Convert to java.sql.Date
                }

                java.util.Date pickedTime = (java.util.Date) timeSpinner.getValue();
                java.sql.Time startTime = new java.sql.Time(pickedTime.getTime()); // Convert util.Date to sql.Time

                try {
                    // Call to add a new diffusion
                    change.add("diffusions", movieId, roomId, sqlDate, startTime, price);
                    change.showAll(table, "diffusions"); // Refresh the table display
                    change.populateDiffusionIDs(idComboBox);

                    // Reset UI components after successful operation
                    nameComboBox.setSelectedIndex(-1);
                    nbrComboBox.setSelectedIndex(-1);
                    date.setDate(null);
                    timeSpinner.setValue(new java.util.Date()); // Reset to current time
                    priceTextField.setText("");
                    remainingTextField.setText("");
                } catch (DoubleRoomBooking | BookingTimePassed bookingException) {
                    JOptionPane.showMessageDialog(null, bookingException.getMessage());
                }}
            });
        remainingTextField.setEditable(false);


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

    }

    public static void main(String[] args) {
    //new AddScreening();
    }

}