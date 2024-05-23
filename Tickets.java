import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.icons.FlatSearchIcon;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Tickets extends JFrame {
    boolean isAdmin;
    private DefaultTableModel model;
    JTable table;

    Tickets(boolean isAdmin) {
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
        ImageIcon ticketsIcon = new ImageIcon("C:/Users/Asus/Downloads/CinemaManagement/TicketsIcon.png");
        JLabel tckLabel = new JLabel(ticketsIcon);
        tckLabel.setBounds(gcenter + 5, 30, 150, 150);
        grayPanel.add(tckLabel);

        String[] columnNames = {"Ticket", "Client", "Phone", "Film", "Date", "DST", "DET", "Price", "Payment", "Action"};
        model = new DefaultTableModel(0, columnNames.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Only the 'Action' column is editable
            }
        };
        model.setColumnIdentifiers(columnNames);

        table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(false);

        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), model, this));

        // Set row height
        int rowHeight = 25;
        table.setRowHeight(rowHeight);

        String url = "jdbc:mysql://localhost:3306/cinema";
        String user = "root";
        String password = "31574801";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT billets.num, billets.prix, billets.methodePayment, clients.nom AS client_nom, clients.numTelephone, " +
                    "diffusions.date, diffusions.horaire_debut, diffusions.horaire_fin, films.nom AS film_nom " +
                    "FROM billets " +
                    "JOIN clients ON billets.client_id = clients.id " +
                    "JOIN diffusions ON billets.diffusion_id = diffusions.id " +
                    "JOIN films ON diffusions.film_id = films.id";
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int ticketNum = resultSet.getInt("num");
                double price = resultSet.getDouble("prix");
                String paymentMethod = resultSet.getString("methodePayment");
                String clientName = resultSet.getString("client_nom");
                String phoneNumber = resultSet.getString("numTelephone");
                Date diffusionDate = resultSet.getDate("date");
                Time diffusionStartTime = resultSet.getTime("horaire_debut");
                Time diffusionEndTime = resultSet.getTime("horaire_fin");
                String filmName = resultSet.getString("film_nom");

                model.addRow(new Object[]{ticketNum, clientName, phoneNumber, filmName, diffusionDate, diffusionStartTime, diffusionEndTime, price, paymentMethod, "Delete"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Tickets.this, "probleme de connexion sql");
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(25, 80, 600, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);

        JTextField searchField = new JTextField();
        searchField.putClientProperty("JTextField.leadingIcon", new FlatSearchIcon());
        searchField.putClientProperty("JTextField.placeholderText", "Search...");
        searchField.setBounds(475, 37, 150, 30);
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

        // BOOK TICKET button
        JButton bookticket = new JButton("BOOK");
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
                    setVisible(false);
                    new BookTicket(isAdmin); // Pass date as null for now
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // CANCEL TICKET button
        JButton cancelButton = new JButton("CANCEL");
        int cancelButtonY = vCenter + 1 * (buttonHeight + 10);
        cancelButton.setBounds(gcenter, cancelButtonY + 20, 160, 40);
        cancelButton.setFont(font2);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        grayPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cancel action here if needed
            }
        });

        // REMAINING TICKETS button
        JButton ticketsleftButton = new JButton("TICKETS LEFT");
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
                    new ticketsLeft();

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

    // Custom renderer for the delete button
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }

    // Custom editor for the delete button
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private DefaultTableModel tableModel;
        private JFrame parentFrame;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel tableModel, JFrame parentFrame) {
            super(checkBox);
            this.tableModel = tableModel;
            this.parentFrame = parentFrame;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "Delete" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                int ticketNum = (int) tableModel.getValueAt(row, 0);

                // Confirm before deleting
                int confirm = JOptionPane.showConfirmDialog(parentFrame,
                        "Are you sure you want to delete ticket #" + ticketNum + "?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    changes changes=new changes();
                    changes.cancelTicket(ticketNum);
                }
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        new Tickets(true);
    }
}
