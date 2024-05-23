import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DateFormatter;

import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Screenings extends JFrame {
    List<Integer> movieMap = new ArrayList<>();
    boolean isAdmin;
    private DefaultTableModel model;

    Screenings(boolean isAdmin) {
        this.isAdmin=isAdmin;
        Font font1 = new Font("Segoe UI", Font.BOLD, 22);
        Font font2 = new Font("Tahoma", Font.BOLD, 16);
        Font font3 = new Font("Tahoma", Font.PLAIN, 14);

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
        ImageIcon scrIcon = new ImageIcon("C:/Users/Asus/Downloads/CinemaManagement/ScreeningsIcon.png");
        JLabel scrLabel = new JLabel(scrIcon);
        scrLabel.setBounds(gcenter + 5, 70, 150, 100);
        grayPanel.add(scrLabel);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Id","Film","Version","Date","Start hour","End hour","Price"});
        JTable table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(true);

        //int rowCount = table.getRowCount();
        int rowHeight = 25;
        table.setRowHeight(rowHeight);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(280, 120, 350, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);


        // Créez un nouveau modèle de table par défaut
        //I USED ANOTHER METHOD WITHOUT USING THE FUNCTION (check orponex discussion)

        changes change = new changes();

        // SHOWING THE TABLE WITH THE MOVIE NAME NOT MOVIE ID 
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the existing table data
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(1);
        column.setPreferredWidth(140);
        String url = "jdbc:mysql://localhost:3306/cinema3";
            String user = "root";
            String password = "meriem123";

        try (Connection connection = DriverManager.getConnection(url, user, password)){
        // Adjust the query to join the screenings and movie tables
            String query = "SELECT diffusions.id, films.nom,films.version," +
               "diffusions.date, diffusions.horaire_debut, diffusions.horaire_fin, diffusions.prix " +
               "FROM diffusions JOIN films ON diffusions.film_id = films.id";


            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Populate the model with data
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }

            // Close the result set
            resultSet.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter dateFormatter = new DateFormatter(dateFormat);
        JFormattedTextField searchField = new JFormattedTextField(dateFormatter);
        searchField.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
        searchField.putClientProperty("JTextField.placeholderText", "yyyy-MM-dd");
        searchField.setBounds(480,82,150,30);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                searchField.getBorder()
            ));
        redPanel.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }

            private void filterTable() {
                String searchText = searchField.getText().toLowerCase();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);

                if (searchText.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
                }
            }

        });


        // MOVIE ID label
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(font3);
        idLabel.setForeground(Color.WHITE);
        idLabel.setBounds(25, 120, 100, 20);
        redPanel.add(idLabel);

        // MOVIE NAME label
        JLabel nameLabel = new JLabel("Film : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 190, 100, 20);
        redPanel.add(nameLabel);

        // SCREENING ROOM NUMBER label
        JLabel numberLabel = new JLabel("Room : ");
        numberLabel.setFont(font3);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setBounds(25, 250, 100, 20);
        redPanel.add(numberLabel);

        // DATE label
        JLabel dateLabel = new JLabel("Date : ");
        dateLabel.setFont(font3);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBounds(25, 310, 100, 20);
        redPanel.add(dateLabel);

        // STARTS AT label
        JLabel startLabel = new JLabel("Starts at : ");
        startLabel.setFont(font3);
        startLabel.setForeground(Color.WHITE);
        startLabel.setBounds(25, 370, 100, 20);
        redPanel.add(startLabel);

        // REMAINING TICKETS label
        JLabel remainingLabel = new JLabel("Tickets left : ");
        remainingLabel.setFont(font3);
        remainingLabel.setForeground(Color.WHITE);
        remainingLabel.setBounds(25, 430, 100, 20);
        redPanel.add(remainingLabel);

        // ID JComboBox
        JComboBox idComboBox = new JComboBox();  // Remove the generic type to allow any object
        idComboBox.setFont(font3);
        idComboBox.setBackground(new Color(67, 67, 67));
        idComboBox.setForeground(Color.WHITE);
        idComboBox.setBounds(120, 120, 120, 23);
        redPanel.add(idComboBox);
        change.populateDiffusionIDs(idComboBox);



        // NAME combobox
        JComboBox<Film> nameComboBox = new JComboBox<>();
        nameComboBox.setFont(font3);
        nameComboBox.setBackground(new Color(67, 67, 67));
        nameComboBox.setForeground(Color.WHITE);
        nameComboBox.setBounds(120, 190, 120, 23);
        redPanel.add(nameComboBox);
        change.populateNameDiffusions(nameComboBox);
        // REMAINING textfield
        JTextArea remainingTextField = new JTextArea();
        remainingTextField.setFont(font3);
        remainingTextField.setBackground(new Color(67, 67, 67));
        remainingTextField.setForeground(Color.WHITE);
        remainingTextField.setBounds(120, 430, 120, 23);
        redPanel.add(remainingTextField);

        //PRICE label
        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setFont(font3);
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setBounds(25, 500, 100, 20);
        redPanel.add(priceLabel);

        // PRICE text field
        JTextField priceTextField = new JTextField();
        priceTextField.setFont(font3);
        priceTextField.setBackground(new Color(67, 67, 67));
        priceTextField.setForeground(Color.WHITE);
        priceTextField.setBounds(120, 500, 120, 23);
        redPanel.add(priceTextField);

        // NUMBER combobox
        JComboBox<Integer> nbrComboBox = new JComboBox<>();
        nbrComboBox.setFont(font3);
        nbrComboBox.setBackground(new Color(67, 67, 67));
        nbrComboBox.setForeground(Color.WHITE);
        nbrComboBox.setBounds(120, 250, 120, 23);
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
        nameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Film selectedMovie = (Film) nameComboBox.getSelectedItem();
                if (selectedMovie != null) {
                    idComboBox.removeAllItems();
                    change.populateDiffusionIDs(idComboBox, selectedMovie.getId());
                }
            }
        });
        JDateChooser date = new JDateChooser();
        date.setBounds(120, 310, 120, 23);
        redPanel.add(date);

        // Create a SpinnerDateModel with default values
        SpinnerDateModel modelTime = new SpinnerDateModel();
        modelTime.setCalendarField(Calendar.MINUTE);

        JSpinner timeSpinner = new JSpinner(modelTime);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(120, 370, 120, 23);
        redPanel.add(timeSpinner);




        // SCREENINGS label
        JLabel scroomsLabel = new JLabel("SCREENINGS");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);
        scroomsLabel.setBounds(rcenter + 30, 50, 300, 22);
        redPanel.add(scroomsLabel);

        // SHOW ALL SCREENINGS button
        JButton showallButton = new JButton("SHOW ALL");
        int showallButtonY = vCenter + 0 * (buttonHeight + 10);
        showallButton.setBounds(gcenter, showallButtonY + 20, 160, 40);
        showallButton.setFont(font2);
        showallButton.setBackground(Color.BLACK);
        showallButton.setForeground(Color.WHITE);
        grayPanel.add(showallButton);
        showallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new  MovieDiffusionDisplay(idComboBox,true);
            }
        });

        // ADD button
        JButton addButton = new JButton("ADD");
        int addButtonY = vCenter + 1 * (buttonHeight + 10);
        addButton.setBounds(gcenter, addButtonY + 20, 160, 40);
        addButton.setFont(font2);
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.WHITE);
        grayPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new AddScreening(table, idComboBox);
               change.showScreenings(table,"diffusions");

            }
        });


        // EDIT button
        JButton editButton = new JButton("EDIT");
        int editButtonY = vCenter + 2 * (buttonHeight + 10);
        editButton.setBounds(gcenter, editButtonY + 20, 160, 40);
        editButton.setFont(font2);
        editButton.setBackground(Color.BLACK);
        editButton.setForeground(Color.WHITE);
        grayPanel.add(editButton);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Check if an ID is selected
                    Object selectedIdObj = idComboBox.getSelectedItem();
                    if (selectedIdObj == null) {
                        JOptionPane.showMessageDialog(null, "Please select a diffusion ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // Exit early if no diffusion ID is selected
                    }
                    int scID = (int) selectedIdObj;

                    java.util.Date pickedTime = (java.util.Date) timeSpinner.getValue();
                    java.sql.Time startTime = new java.sql.Time(pickedTime.getTime()); // Convert util.Date to sql.Time

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
                    java.sql.Date sqlDate = utilDate != null ? new java.sql.Date(utilDate.getTime()) : null;

                    // Call to edit a diffusion
                    change.edit("diffusions", scID, movieId, roomId, sqlDate, startTime, price);
                    change.showScreenings(table,"diffusions");
                    //reset
                    idComboBox.setSelectedIndex(-1);
                    nameComboBox.setSelectedIndex(-1);
                    nbrComboBox.setSelectedIndex(-1);
                    date.setDate(null);
                    timeSpinner.setValue(new java.util.Date()); // Reset to current time
                    priceTextField.setText("");
                    remainingTextField.setText("");
                } catch (DoubleRoomBooking | BookingTimePassed bookingException) {
                    JOptionPane.showMessageDialog(null, bookingException.getMessage());
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Failed to update diffusion: " + exception.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idComboBox.getSelectedItem() != null) {
                    int selectedId = (int) idComboBox.getSelectedItem();
                    Map<String, Object> details = change.getDiffusionDetails(selectedId);

                    // Find the corresponding Film object in the nameComboBox
                    for (int i = 0; i < nameComboBox.getItemCount(); i++) {
                        Film film = nameComboBox.getItemAt(i);
                        if (film.getId() == (int) details.get("film_id")) {
                            nameComboBox.setSelectedItem(film); // Set the selected item directly to the Film object
                            break;
                        }
                    }
                    nbrComboBox.setSelectedItem(details.get("salle_id"));
                    if (details.get("date") != null) {
                        date.setDate((Date) details.get("date"));
                    }

                    if (details.get("horaire_debut") != null) {
                        Time time = (Time) details.get("horaire_debut");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(time.getTime());
                        timeSpinner.setValue(calendar.getTime());
                    }

                    if (details.get("prix") != null) {
                        priceTextField.setText(String.valueOf(details.get("prix")));
                    }
                }
            }
        });

        // REMOVE button
        JButton removeButton = new JButton("REMOVE");
        int removeButtonY = vCenter + 3 * (buttonHeight + 10);
        removeButton.setBounds(gcenter, removeButtonY + 20, 160, 40);
        removeButton.setFont(font2);
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        grayPanel.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedIdObject = idComboBox.getSelectedItem();
                if (selectedIdObject == null) {
                    JOptionPane.showMessageDialog(null, "Please select a diffusion ID to remove.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method early if no ID is selected
                }
                int scID = (int) selectedIdObject; // Safely cast the object to an integer

                try {
                    change.deleteDiffusion(scID);
                    change.showScreenings(table,"diffusions");

                    // Reset UI components after successful operation
                    change.populateNameDiffusions(nameComboBox);
                    idComboBox.setSelectedIndex(-1);
                    nameComboBox.setSelectedIndex(-1);
                    nbrComboBox.setSelectedIndex(-1);
                    date.setDate(null);
                    timeSpinner.setValue(new java.util.Date()); // Reset to current time
                    priceTextField.setText("");
                    remainingTextField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while removing the diffusion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
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
                    new HomePage(isAdmin);
                    setVisible(false);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });
        idComboBox.setSelectedIndex(-1);
        nameComboBox.setSelectedIndex(-1);
        nbrComboBox.setSelectedIndex(-1);
        date.setDate(null);
        timeSpinner.setValue(new java.util.Date());  // Reset to current time
        priceTextField.setText("");
        remainingTextField.setText("");

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int selectedRow = table.getSelectedRow();
                if(selectedRow != -1){
                    TableModel model = table.getModel();
                    idComboBox.setSelectedItem(table.getValueAt(selectedRow, 0));
                    nameComboBox.setSelectedItem(table.getValueAt(selectedRow, 1));
                    nbrComboBox.setSelectedItem(table.getValueAt(selectedRow, 2));
                    Object dateValue = model.getValueAt(selectedRow, 3);
                    if (dateValue != null && dateValue instanceof java.sql.Date) {
                        java.sql.Date sqlDate = (java.sql.Date) dateValue;
                        date.setDate(new Date(sqlDate.getTime()));
                    }
                    // Get the start time from the table and set it to JSpinner
                    Object timeValue = model.getValueAt(selectedRow, 4);
                    if (timeValue != null && timeValue instanceof java.sql.Time) {
                        java.sql.Time sqlTime = (java.sql.Time) timeValue;
                        timeSpinner.setValue(new Date(sqlTime.getTime()));
                    }
                    
                    priceTextField.setText(String.valueOf(table.getValueAt(selectedRow, 6)));
                    int id = (int) model.getValueAt(selectedRow, 0);
                    int remainingTickets = change.getLeftTicketsNbr(id);
                    remainingTextField.setText(String.valueOf(remainingTickets));
                    

                }
                }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
        
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            
        });

    }

    public static void main(String[] args) {
        new Screenings(true);
    }
}