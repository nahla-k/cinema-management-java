
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class BookTicket extends JFrame {
    boolean isAdmin;

    BookTicket( boolean isAdmin) {

        this.isAdmin = isAdmin;
        Font font1 = new Font("Segoe UI", Font.BOLD, 22);
        Font font2 = new Font("Tahoma", Font.BOLD, 16);
        Font font3 = new Font("Tahoma", Font.PLAIN, 14);

        String[] method = { "CASH", "CIB" };
        changes changes = new changes();
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
        int rightlabelWidth = 200;
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
        ImageIcon bookingIcon = new ImageIcon("C:/Users/Asus/Downloads/CinemaManagement/BookTicketIcon.png");
        JLabel bookingLabel = new JLabel(bookingIcon);
        bookingLabel.setBounds(gcenter + 5, 60, 150, 150);
        grayPanel.add(bookingLabel);

        // BOOK TICKET label
        JLabel ticketsLabel = new JLabel("BOOK TICKET");
        ticketsLabel.setFont(font1);
        ticketsLabel.setForeground(Color.WHITE);
        ticketsLabel.setBounds(rcenter + 30, 50, 300, 22);
        redPanel.add(ticketsLabel);

        // MOVIE NAME label
        JLabel nameLabel = new JLabel("Movie : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 160, 150, 18);
        redPanel.add(nameLabel);

        // SCREENING ID label
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(font3);
        idLabel.setForeground(Color.WHITE);
        idLabel.setBounds(25, 240, 150, 18);
        redPanel.add(idLabel);

        // PRICE label
        JLabel priceLabel = new JLabel("Price : ");
        priceLabel.setFont(font3);
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setBounds(25, 320, 150, 18);
        redPanel.add(priceLabel);

        // DATE label
        JLabel dateLabel = new JLabel("Date : ");
        dateLabel.setFont(font3);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBounds(25, 400, 150, 18);
        redPanel.add(dateLabel);

        // TICKETS LEFT label
        JLabel tcktsleftLabel = new JLabel("Tickets left : ");
        tcktsleftLabel.setFont(font3);
        tcktsleftLabel.setForeground(Color.WHITE);
        tcktsleftLabel.setBounds(25, 480, 150, 18);
        redPanel.add(tcktsleftLabel);

        // FULL NAME label
        JLabel fullnameLabel = new JLabel("Full name : ");
        fullnameLabel.setFont(font3);
        fullnameLabel.setForeground(Color.WHITE);
        fullnameLabel.setBounds(320, 160, 150, 18);
        redPanel.add(fullnameLabel);

        // PHONE NUMBER label
        JLabel phonenbrLabel = new JLabel("Phone number : ");
        phonenbrLabel.setFont(font3);
        phonenbrLabel.setForeground(Color.WHITE);
        phonenbrLabel.setBounds(320, 240, 150, 18);
        redPanel.add(phonenbrLabel);

        // PAYEMENT METHOD label
        JLabel methodLabel = new JLabel("Payement method : ");
        methodLabel.setFont(font3);
        methodLabel.setForeground(Color.WHITE);
        methodLabel.setBounds(320, 320, 150, 18);
        redPanel.add(methodLabel);

        // TICKET ID label
        JLabel ticketidLabel = new JLabel("Ticket ID : ");
        ticketidLabel.setFont(font3);
        ticketidLabel.setForeground(Color.WHITE);
        ticketidLabel.setBounds(320, 400, 150, 18);
        redPanel.add(ticketidLabel);

        // MOVIE NAME combobox
        JComboBox<Film> nameComboBox = new JComboBox<>();
        changes.populateNameDiffusions(nameComboBox);
        nameComboBox.setFont(font3);
        nameComboBox.setBackground(new Color(67, 67, 67));
        nameComboBox.setForeground(Color.WHITE);
        nameComboBox.setBounds(120, 160, 150, 18);

        redPanel.add(nameComboBox);

        // ID combobox
        JComboBox<Integer> idComboBox = new JComboBox<>();
        idComboBox.setFont(font3);
        idComboBox.setBackground(new Color(67, 67, 67));
        idComboBox.setForeground(Color.WHITE);
        idComboBox.setBounds(120, 240, 120, 18);
        redPanel.add(idComboBox);

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

        // PRICE textarea
        JTextArea price = new JTextArea();
        price.setFont(font3);
        price.setBackground(new Color(67, 67, 67));
        price.setForeground(Color.WHITE);
        price.setBounds(120, 320, 100, 18);
        price.setEditable(false);
        redPanel.add(price);



        // DATE textarea
        JTextArea dateTextArea = new JTextArea();
        dateTextArea.setFont(font3);
        dateTextArea.setBackground(new Color(67, 67, 67));
        dateTextArea.setForeground(Color.WHITE);
        dateTextArea.setBounds(120, 400, 100, 18);
        dateTextArea.setEditable(false);
        redPanel.add(dateTextArea);

        // TICKETS LEFT textarea
        JTextArea ticketsleftTextArea = new JTextArea();
        ticketsleftTextArea.setFont(font3);
        ticketsleftTextArea.setBackground(new Color(67, 67, 67));
        ticketsleftTextArea.setForeground(Color.WHITE);
        ticketsleftTextArea.setBounds(120, 480, 80, 18);
        ticketsleftTextArea.setEditable(false);
        redPanel.add(ticketsleftTextArea);

        // FULL NAME textfield
        JTextField fnTextField = new JTextField();
        fnTextField.setFont(font3);
        fnTextField.setBackground(new Color(67, 67, 67));
        fnTextField.setForeground(Color.WHITE);
        fnTextField.setBounds(460, 160, 150, 18);
        redPanel.add(fnTextField);

        // PHONE NUMBER textfield
        JTextField phnTextField = new JTextField();
        phnTextField.setFont(font3);
        phnTextField.setBackground(new Color(67, 67, 67));
        phnTextField.setForeground(Color.WHITE);
        phnTextField.setBounds(460, 240, 150, 18);
        redPanel.add(phnTextField);

        // PAYEMENT MWTHOD combobox
        JComboBox<String> pmComboBox = new JComboBox<>(method);
        pmComboBox.setFont(font3);
        pmComboBox.setBackground(new Color(67, 67, 67));
        pmComboBox.setForeground(Color.WHITE);
        pmComboBox.setBounds(460, 320, 100, 18);
        redPanel.add(pmComboBox);

        // TICKET ID textarea
        JTextArea tickidTextArea = new JTextArea();
        tickidTextArea.setFont(font3);
        tickidTextArea.setBackground(new Color(67, 67, 67));
        tickidTextArea.setForeground(Color.WHITE);
        tickidTextArea.setBounds(460, 400, 150, 18);
        tickidTextArea.setEditable(false);
        redPanel.add(tickidTextArea);

        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer selectedDiffusionId = (Integer) idComboBox.getSelectedItem();
                if (selectedDiffusionId != null) {
                    // Retrieve the price of the selected diffusion
                    double priceV = changes.getDiffusionPrice(selectedDiffusionId);
                    // Retrieve the number of left tickets for the selected diffusion
                    int leftTickets = changes.getLeftTicketsNbr(selectedDiffusionId);
                    // Display the price and left ticket number in JTextAreas
                    price.setText(String.valueOf(priceV));
                    ticketsleftTextArea.setText(String.valueOf(leftTickets));
                    tickidTextArea.setText(String.valueOf(changes.getNextTicketId()));
                    Date date = changes.getDiffusionDate(selectedDiffusionId);
                    dateTextArea.setText(date.toString()); // Assuming the date is already in Date format
                } else {
                    // Clear or reset the fields if there is no selection
                    price.setText("");
                    ticketsleftTextArea.setText("");
                    tickidTextArea.setText("");
                    dateTextArea.setText("");
                }
            }
        });
        ticketsleftTextArea.setEditable(false);



        // SAVE button
        JButton saveButton = new JButton("SAVE");
        int saveButtonY = vCenter + 1 * (buttonHeight + 10);
        saveButton.setBounds(gcenter, saveButtonY + 20, 160, 40);
        saveButton.setFont(font2);
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        grayPanel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fullName = fnTextField.getText().trim();
                    String phoneNumber = phnTextField.getText().trim();
                    Object selectedDiffusionId = idComboBox.getSelectedItem();
                    String paymentMethod = (String) pmComboBox.getSelectedItem();

                    // Check if all required fields are filled
                    if (fullName.isEmpty() || phoneNumber.isEmpty() || selectedDiffusionId == null || paymentMethod == null) {
                        JOptionPane.showMessageDialog(BookTicket.this, "Please fill all fields and make sure a movie and screening are selected.",
                                "Missing Information", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Proceed with adding ticket
                    changes.addTicket((int) selectedDiffusionId, changes.addClient(fullName, phoneNumber), paymentMethod);

                    // Clear fields after save
                    fnTextField.setText("");
                    phnTextField.setText("");
                    tickidTextArea.setText("");
                    price.setText("");
                    ticketsleftTextArea.setText("");
                    dateTextArea.setText("");

                    // Reset ComboBoxes
                    nameComboBox.setSelectedIndex(-1);
                    idComboBox.removeAllItems();
                    pmComboBox.setSelectedIndex(-1);

                    // Update ticket ID to next available ID
                    tickidTextArea.setText(String.valueOf(changes.getNextTicketId()));


                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookTicket.this, "An error occurred while saving ticket information.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // CANCEL button
        JButton cancelButton = new JButton("CANCEL");
        int cancelButtonY = vCenter + 2 * (buttonHeight + 10);
        cancelButton.setBounds(gcenter, cancelButtonY + 20, 160, 40);
        cancelButton.setFont(font2);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        grayPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
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
        nameComboBox.setSelectedIndex(-1);
        idComboBox.removeAllItems();
        pmComboBox.setSelectedIndex(-1);
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
                    new Tickets(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) {
       new BookTicket(true);
    }

}