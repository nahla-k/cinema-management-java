import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.icons.FlatSearchIcon;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Archive extends JFrame{
    boolean isAdmin;
    Archive(boolean isAdmin) {
        this.isAdmin = isAdmin;
        Font font1 = new Font("Segoe UI", Font.BOLD, 22);
        Font font2 = new Font("Tahoma", Font.BOLD, 16);
        Font font3 = new Font("Tahoma", Font.PLAIN, 14);

        changes change = new changes();
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
        redPanel.setBounds(5, 5, 878, 600);
        getContentPane().add(redPanel);

        // redpanel horizontal center
        int rightlabelWidth = 150;
        int redpanelWidth = redPanel.getWidth();
        int rcenter = (redpanelWidth - rightlabelWidth) / 2;

        JLabel scroomsLabel = new JLabel("SCREENINGS ARCHIVE");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);
        scroomsLabel.setBounds(rcenter -30, 35, 300, 22);
        redPanel.add(scroomsLabel);




        String[] columnNames = {"Movie","Room",  "Date", "DST", "DET","Price"};

        DefaultTableModel model = new DefaultTableModel(0, columnNames.length);
        model.setColumnIdentifiers(columnNames);

        JTable table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(false);

        //int rowCount = table.getRowCount();
        int rowHeight = 25;
        table.setRowHeight(rowHeight);

        String url = "jdbc:mysql://localhost:3306/archive";
        String user = "root";
        String password = "31574801";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT id, film_name, salle_num, date, horaire_debut, horaire_fin, prix FROM diffusions";
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String filmName = resultSet.getString("film_name");
                int salleNum = resultSet.getInt("salle_num");
                java.sql.Date date = resultSet.getDate("date");
                java.sql.Time startTime = resultSet.getTime("horaire_debut");
                java.sql.Time endTime = resultSet.getTime("horaire_fin");
                double price = resultSet.getDouble("prix");

                model.addRow(new Object[]{filmName, salleNum, date,startTime, endTime, price});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "probleme de connexion sql");
        }



        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25,70, 850, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);

        JTextField searchField = new JTextField();
        searchField.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
        searchField.putClientProperty("JTextField.placeholderText", "Search...");
        searchField.setBounds(700,37,150,30);
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









        // BACK button
        JButton backButton = new JButton("BACK");
        backButton.setBounds(rcenter -300, 500, 140, 40);
        backButton.setFont(font2);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        redPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                    new Tickets( isAdmin);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
        });



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
                new Archive(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

