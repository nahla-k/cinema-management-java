import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EventObject;
public class Movies extends Film {
    static boolean isAdmin;
    private File selectedImageFile;
    byte[] imgBytes;
    public Movies(boolean isAdmin)  {
        super();  // Calling the no-argument constructor of the superclass
        Movies.isAdmin = isAdmin;
        MoviesUI();
    }
    private void MoviesUI()  {
        this.isAdmin=isAdmin;
        Font font1 = new Font("Segoe UI", Font.BOLD, 22);
        Font font2 = new Font("Tahoma", Font.BOLD, 16);
        Font font3 = new Font("Tahoma", Font.PLAIN, 14);
        Font font4 = new Font("Tahoma", Font.BOLD, 14);
        
        File fontFile = new File("C:/Users/Asus/Downloads/filepile/Filepile.otf");
        Font[] fonts;
        try {
            fonts = Font.createFonts(fontFile);
            if (fonts != null && fonts.length > 0) {
                Font font5 = fonts[0];
            } else {
                System.err.println("No fonts loaded.");
            }
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] Genre = { "Action", "Aventure", "ComÃ©die", "Drame", "Fantaisie", "Horreur", "Romance",
                "Sciencefiction", "Thriller", "Autre" };
        String[] Version = { "VO", "VF" };
        JFrame frame = new JFrame();

        Connection connection;
        Statement statement;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema?user=root&password=31574801");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        changes change = new changes();

        frame.getContentPane().setBackground(new Color(232, 232, 232));
        frame.setLayout(null);
        frame.setLocation(200, 25);
        frame.setSize(900, 650);
        frame.setVisible(true);

        Color[] redColors = {Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18")};
        float[] redFractions = {0.0f, 0.52f, 1.0f};
        gradient redPanel = new gradient(redColors, redFractions);
        redPanel.setLayout(null);
        redPanel.setBounds(230, 5, 650, 600);
        frame.getContentPane().add(redPanel);

        Color[] grayColors = {Color.decode("#1f1c18"), Color.decode("#414345"), Color.decode("#1f1c18")};
        float[] grayFractions = {0.0f, 0.5f, 1.0f};
        gradient grayPanel = new gradient(grayColors, grayFractions);
        grayPanel.setLayout(null);
        grayPanel.setBounds(5, 5, 220, 600);
        frame.getContentPane().add(grayPanel);

        // redpanel horizontal center
        int rightlabelWidth = 220;
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
        ImageIcon mvIcon = new ImageIcon("C:/Users/Asus/Downloads/CinemaManagement/MoviesIcon.png");
        JLabel scrLabel = new JLabel(mvIcon);
        scrLabel.setBounds(gcenter + 5, 50, 150, 150);
        grayPanel.add(scrLabel);

        // MOVIES label
        JLabel moviesLabel = new JLabel("MOVIES");
        moviesLabel.setFont(font1);
        moviesLabel.setForeground(Color.WHITE);
        moviesLabel.setBounds(rcenter + 50, 50, 200, 22);
        redPanel.add(moviesLabel);

        // SHOW ALL MOVIES button
        JButton showallButton = new JButton("SHOW ALL");
        int showallButtonY = vCenter + 0 * (buttonHeight + 10);
        showallButton.setBounds(gcenter, showallButtonY + 20, 160, 40);
        showallButton.setFont(font2);
        //showallButton.putClientProperty("JButton.buttonType", "roundRect");
        showallButton.setBackground(Color.BLACK);
        showallButton.setForeground(Color.WHITE);
        grayPanel.add(showallButton);

        
        // REMOVE button
        JButton removeButton = new JButton("REMOVE");
        int removeButtonY = vCenter + 3 * (buttonHeight + 10);
        removeButton.setBounds(gcenter, removeButtonY + 20, 160, 40);
        removeButton.setFont(font2);
        //removeButton.putClientProperty("JButton.buttonType", "roundRect");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        grayPanel.add(removeButton);
       

        // BACK button
        JButton backButton = new JButton("BACK");
        int backButtonY = vCenter + 4 * (buttonHeight + 30);
        backButton.setBounds(gcenter + 10, backButtonY, 140, 40);
        backButton.setFont(font2);
        //backButton.putClientProperty("JButton.buttonType", "roundRect");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        grayPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.setVisible(false);
                    new HomePage(isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });



        // MOVIE ID label
        JLabel idLabel = new JLabel("ID : ");
        idLabel.setFont(font3);
        idLabel.setForeground(Color.WHITE);
        idLabel.setBounds(25, 100, 100, 18);
        redPanel.add(idLabel);

        // MOVIE NAME label
        JLabel nameLabel = new JLabel("Name : ");
        nameLabel.setFont(font3);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(25, 150, 100, 18);
        redPanel.add(nameLabel);

        // TIME label
        JLabel timeLabel = new JLabel("Time : ");
        timeLabel.setFont(font3);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(25, 200, 100, 18);
        redPanel.add(timeLabel);

        // GENRE label
        JLabel genreLabel = new JLabel("Genre : ");
        genreLabel.setFont(font3);
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setBounds(25, 250, 100, 18);
        redPanel.add(genreLabel);

        // VERSION label
        JLabel versionLabel = new JLabel("Version : ");
        versionLabel.setFont(font3);
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setBounds(25, 300, 100, 18);
        redPanel.add(versionLabel);

        // ID textfield
        JComboBox idTextField = new JComboBox();
        idTextField.setFont(font3);
        idTextField.setBackground(new Color(67, 67, 67));
        idTextField.setForeground(Color.WHITE);
        idTextField.setBounds(120, 100, 120, 18);
        redPanel.add(idTextField);
        change.populateMovieIDs(idTextField);

        // MOVIE NAME textfield
        JTextField nameTextField = new JTextField();
        nameTextField.setFont(font3);
        nameTextField.setBackground(new Color(67, 67, 67));
        nameTextField.setForeground(Color.WHITE);
        nameTextField.setBounds(120, 150, 120, 18);
        redPanel.add(nameTextField);

        // TIME textfield
        SpinnerDateModel modelTime = new SpinnerDateModel();
        modelTime.setCalendarField(Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(modelTime);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setBounds(120, 200, 120, 18);
        redPanel.add(timeSpinner);


        // GENRE combobox
        JComboBox<String> genreComboBox = new JComboBox<>(Genre);
        genreComboBox.setFont(font3);
        genreComboBox.setBackground(new Color(67, 67, 67));
        genreComboBox.setForeground(Color.WHITE);
        genreComboBox.setBounds(120, 250, 120, 18);
        redPanel.add(genreComboBox);

        // VERSION combobox
        JComboBox<String> versionComboBox = new JComboBox<>(Version);
        versionComboBox.setFont(font3);
        versionComboBox.setBackground(new Color(67, 67, 67));
        versionComboBox.setForeground(Color.WHITE);
        versionComboBox.setBounds(120, 300, 120, 18);
        redPanel.add(versionComboBox);


        // PHOTO panel
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(null);
        photoPanel.setBounds(120, 350, 120, 180);
        photoPanel.setBackground(new Color(67, 67, 67));
        redPanel.add(photoPanel);


        // ADD PHOTO button
        JButton chooseButton = new JButton("ADD PHOTO");
        chooseButton.setBounds(120, 540, 120, 30);
        chooseButton.setFont(font4);
        chooseButton.setBackground(Color.BLACK);
        chooseButton.setForeground(Color.WHITE);
        redPanel.add(chooseButton);
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

        String[] columnNames = {"ID", "Nom", "DurÃ©e", "Version", "Genre"};
        DefaultTableModel model = new DefaultTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);

        JTable table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(true);

        //int rowCount = table.getRowCount();
        int rowHeight = 25;
        table.setRowHeight(rowHeight);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(280, 110, 350, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);

        change.showAll(table, "films");

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int selectedRow = table.getSelectedRow();
                if(selectedRow != -1){
                    TableModel model = table.getModel();
                    idTextField.setSelectedItem(model.getValueAt(selectedRow, 0));
                    nameTextField.setText(model.getValueAt(selectedRow, 1).toString());
                    versionComboBox.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
                    //genreComboBox.setSelectedItem(model.getValueAt(selectedRow, 5).toString());
                }
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        });


        JTextField searchField = new JTextField();
        searchField.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
        searchField.putClientProperty("JTextField.placeholderText", "Search...");
        searchField.setBounds(480,72,150,30);
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



        JButton addButton = new JButton("ADD");
        int addButtonY = vCenter + 1 * (buttonHeight + 10);
        addButton.setBounds(gcenter, addButtonY + 20, 160, 40);
        addButton.setFont(font2);
        //addButton.putClientProperty("JButton.buttonType", "roundRect");
        addButton.setBackground(Color.BLACK);
        addButton.setForeground(Color.WHITE);
        grayPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMovie(table,idTextField);
            }
        });

        // EDIT button
        JButton editButton = new JButton("EDIT");
        int editButtonY = vCenter + 2 * (buttonHeight + 10);
        editButton.setBounds(gcenter, editButtonY + 20, 160, 40);
        editButton.setFont(font2);
        //editButton.putClientProperty("JButton.buttonType", "roundRect");
        editButton.setBackground(Color.BLACK);
        editButton.setForeground(Color.WHITE);
        grayPanel.add(editButton);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String idField = idTextField.getSelectedItem().toString();
                    if (idField == null || idField.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Please select a valid ID.", "Invalid ID", JOptionPane.ERROR_MESSAGE);
                        return; // Exit the method early if no ID is selected
                    }
                    int idVal = Integer.parseInt(idField); // This might throw NumberFormatException if not a valid integer
                    String titre = nameTextField.getText();
                    String vovf = (String) versionComboBox.getSelectedItem();
                    String genrefilm = (String) genreComboBox.getSelectedItem();

                    java.util.Date pickedTime = (java.util.Date) timeSpinner.getValue();
                    java.sql.Time duree = new java.sql.Time(pickedTime.getTime()); // Convert util.Date to sql.Time

                    // Attempt to edit the film details
                    change.edit("films", idVal, titre, vovf, genrefilm, duree,  imgBytes);
                    change.showAll(table, "films");
                    // Reset fields to their default state
                  //  reset(idTextField, nameTextField, timeSpinner, genreComboBox, versionComboBox, photoPanel, change);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                }
        });


        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Ensure there's a selected item and it can be parsed to integer
                    int idField = (int) idTextField.getSelectedItem();
                    if (idField <1) {
                        JOptionPane.showMessageDialog(null, "Please select a valid movie ID.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Perform deletion
                    change.deleteMovie( idField);
                    change.showAll(table, "films");

                    // Reset form after deletion
                    reset(idTextField, nameTextField, timeSpinner, genreComboBox, versionComboBox, photoPanel, change);

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Invalid movie ID format. Please select a valid ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while removing the movie: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });


        showallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame galleryFrame = new JFrame("Movie Gallery");
                // Increased the width to better accommodate four movie panels
                galleryFrame.setSize(910, 600); // Adjusted size to provide enough space for four panels
                galleryFrame.setLayout(new BorderLayout());
                galleryFrame.setLocationRelativeTo(frame);
                galleryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                gradient moviesPanel = new gradient(redColors, redFractions);
                moviesPanel.setLayout(new GridBagLayout()); // Adjust horizontal and vertical gaps
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 0;
                gbc.gridy = 0;

                JScrollPane scrollPane = new JScrollPane(moviesPanel);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                JTextField searchField = new JTextField();
                searchField.putClientProperty("JTextField.placeholderText", "Type to search...");

                try {
                    ResultSet rs = change.getMoviesWithPosters();
                    int movieCount = 0;

                    while (rs.next()) {
                        int movieId = rs.getInt("id");
                        String movieName = rs.getString("nom");
                        byte[] imgBytes = rs.getBytes("photo");

                        if (imgBytes != null) {
                            ImageIcon imageIcon = new ImageIcon(imgBytes);
                            // Adjusted image dimensions slightly
                            Image image = imageIcon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);

                            JPanel moviePanel = new JPanel(new BorderLayout());
                            moviePanel.setBackground(new Color(166, 64, 64, 58));
                            moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                            // Set fixed size for each movie panel
                            moviePanel.setPreferredSize(new Dimension(200, 310)); // Adjust width and height

                            JLabel imageLabel = new JLabel(new ImageIcon(image));
                            imageLabel.setHorizontalAlignment(JLabel.CENTER);

                            JLabel textLabel = new JLabel(movieName, SwingConstants.CENTER);
                            textLabel.setForeground(Color.WHITE);

                            moviePanel.add(imageLabel, BorderLayout.CENTER);
                            moviePanel.add(textLabel, BorderLayout.SOUTH);
                            moviePanel.addMouseListener(new MouseAdapter() {
//                                @Override
//                                public void mouseEntered(MouseEvent e) {
//                                   imageLabel.setBorder(BorderFactory.createLineBorder(new Color(196, 194, 194), 3));
//                                }

//                                @Override
//                                public void mouseExited(java.awt.event.MouseEvent e) {
//                                    imageLabel.setBorder(BorderFactory.createEmptyBorder());
//                                }

//                                @Override
                                public void mouseClicked(java.awt.event.MouseEvent e) {
                                    SwingUtilities.invokeLater(() -> {
                                        idTextField.setSelectedItem(movieId);
                                        nameTextField.setText(movieName);
                                        galleryFrame.dispose();
                                    });
                                }
                            });
                            gbc.gridx = movieCount % 4;
                            gbc.gridy = movieCount / 4;
                            moviesPanel.add(moviePanel, gbc);

                            movieCount++;
                        }
                    }
                    gbc.gridx = 0;
                    gbc.gridy = (movieCount / 4) + 1;
                    gbc.gridwidth = 4;
                    gbc.weightx = 1;
                    gbc.weighty = 1;
                    moviesPanel.add(new JPanel(), gbc);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(galleryFrame, "Error loading movie data.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                galleryFrame.add(searchField, BorderLayout.NORTH);
                galleryFrame.add(scrollPane, BorderLayout.CENTER);
                galleryFrame.setVisible(true);


                searchField.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        change.filter(moviesPanel, searchField.getText(),idTextField,galleryFrame);
                    }

                    public void removeUpdate(DocumentEvent e) {
                        change.filter(moviesPanel, searchField.getText(),idTextField,galleryFrame);

                    }

                    public void insertUpdate(DocumentEvent e) {
                        change.filter(moviesPanel, searchField.getText(),idTextField,galleryFrame);

                    }
                });
            }
        });
        idTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                Object selectedItemId = comboBox.getSelectedItem();
                if (selectedItemId != null) {
                    try {
                        int filmId = Integer.parseInt(selectedItemId.toString());
                            String name = (String) change.getElement(filmId,"films","nom");
                            String genre = (String) change.getElement(filmId,"films","genre");
                            String version = (String) change.getElement(filmId,"films","version");
                        Time duree = (Time) change.getElement(filmId, "films", "duree");

                         imgBytes = (byte[]) change.getElement(filmId,"films","photo");


                            nameTextField.setText(name);
                            genreComboBox.setSelectedItem(genre);
                            versionComboBox.setSelectedItem(version);
                            timeSpinner.setValue(duree);


                            photoPanel.removeAll();
                            if (imgBytes != null && imgBytes.length > 0) {
                                ImageIcon imageIcon = new ImageIcon(imgBytes); // Create icon directly from bytes
                                Image image = imageIcon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH); // Scale it once
                                JLabel imageLabel = new JLabel(new ImageIcon(image));
                                imageLabel.setBounds(0, 0, 120, 180);

                                photoPanel.add(imageLabel);
                            }
                            photoPanel.revalidate();
                            photoPanel.repaint();

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame, "Invalid film ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });





        idTextField.setSelectedIndex(-1);
        nameTextField.setText("");
        timeSpinner.setValue(new java.util.Date());
        genreComboBox.setSelectedIndex(-1);
        versionComboBox.setSelectedIndex(-1);
        photoPanel.removeAll();
        photoPanel.repaint();
    }

    private void reset(JComboBox idTextField, JTextField nameTextField, JSpinner timeSpinner, JComboBox<String> genreComboBox, JComboBox<String> versionComboBox, JPanel photoPanel, changes change) {
        change.populateMovieIDs(idTextField);
        idTextField.setSelectedIndex(-1);
        nameTextField.setText("");
        timeSpinner.setValue(new java.util.Date());
        genreComboBox.setSelectedIndex(-1);
        versionComboBox.setSelectedIndex(-1);
        photoPanel.removeAll();
        photoPanel.repaint();

    }


    public static void main(String[] args) {
        try {
            // Set FlatLaf as the look and feel
            try {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            changes.startScheduledDeletion();
            // Create and show your Swing GUI
            SwingUtilities.invokeLater(() -> {
                new Movies(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
