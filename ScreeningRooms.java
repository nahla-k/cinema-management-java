import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class ScreeningRooms extends JFrame {
    Font font1 = new Font("Segoe UI", Font.BOLD, 22);
    Font font2 = new Font("Tahoma", Font.BOLD, 16);
    Font font3 = new Font("Tahoma", Font.PLAIN, 14);
    boolean isAdmin;
    changes changes = new changes();

    ScreeningRooms(boolean isAdmin) {
    this.isAdmin=isAdmin;
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
        ImageIcon scrIcon = new ImageIcon("C:/Users/Asus/Downloads/CinemaManagement/ScreeningRoomsIcon.png");
        JLabel scrLabel = new JLabel(scrIcon);
        scrLabel.setBounds(gcenter + 5, 50, 150, 150);
        grayPanel.add(scrLabel);

        // SCREENING ROOMS label
        JLabel scroomsLabel = new JLabel("SCREENING ROOMS");
        scroomsLabel.setFont(font1);
        scroomsLabel.setForeground(Color.WHITE);

        scroomsLabel.setBounds(rcenter, 50, 250, 22);
        redPanel.add(scroomsLabel);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"num","capacity"});
        JTable table = new JTable(model);
        table.setOpaque(false);
        ((javax.swing.table.DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(true);

        //int rowCount = table.getRowCount();
        int rowHeight = 25;
        table.setRowHeight(rowHeight);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(270, 100, 350, 400); // Adjust bounds as needed
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Add the scroll pane to the gradient panel
        redPanel.add(scrollPane);
        // Créez un nouveau modèle de table par défaut
        //I USED ANOTHER METHOD WITHOUT USING THE FUNCTION (check orponex discussion)
    
        changes change = new changes();
        change.showAll(table,"salles");



        // SHOWALL button
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


            }
        });
        // SCREENING ROOM NUMBER label
        JLabel numberLabel = new JLabel("Number : ");
        numberLabel.setFont(font3);
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setBounds(25, 250, 100, 18);
        redPanel.add(numberLabel);

        // SCREENING ROOM CAPACITY label
        JLabel capacityLabel = new JLabel("Capacity : ");
        capacityLabel.setFont(font3);
        capacityLabel.setForeground(Color.WHITE);
        capacityLabel.setBounds(25, 330, 100, 18);
        redPanel.add(capacityLabel);



        // number combobox
        JComboBox<Integer> nbrComboBox = new JComboBox<>();
        nbrComboBox.setFont(font3);
        nbrComboBox.setBackground(new Color(67, 67, 67));
        nbrComboBox.setForeground(Color.WHITE);
        nbrComboBox.setBounds(120, 250, 100, 18);
        redPanel.add(nbrComboBox);
        changes.populateSalleNumbers(nbrComboBox);

        // capacity textfield
        JTextField capacityTextField = new JTextField();
        capacityTextField.setFont(font3);
        capacityTextField.setBackground(new Color(67, 67, 67));
        capacityTextField.setForeground(Color.WHITE);
        capacityTextField.setBounds(120, 330, 100, 18);
        redPanel.add(capacityTextField);
        nbrComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nbrComboBox.getSelectedItem()!=null)
            capacityTextField.setText(change.getElement((int) nbrComboBox.getSelectedItem(),"salles","capacite").toString());
            }
        });

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int selectedRow = table.getSelectedRow();
                if(selectedRow != -1){
                    TableModel model = table.getModel();
                    nbrComboBox.setSelectedItem(model.getValueAt(selectedRow, 0));
                    capacityTextField.setText(model.getValueAt(selectedRow, 1).toString());
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
                new AddScreeningRoom(table,nbrComboBox);
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
                    String capacity = capacityTextField.getText();
                    int roomNumber = (int) nbrComboBox.getSelectedItem();
//                    // Obtenir l'index de ligne sélectionné
//                    int rowIndex = table.getSelectedRow();
//                    if (rowIndex == -1) {
//                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à éditer.");
//                        return;
//                    }
                    // Obtenir le numéro de salle à partir de la ligne sélectionnée
                    //int roomId = (int) table.getValueAt(rowIndex, 0);
                    changes change = new changes();
                    change.edit("salles",  roomNumber, Integer.parseInt(capacity));
                    // Rafraîchir le tableau pour afficher les données mises à jour
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Effacer les données existantes
                    change.showAll(table, "salles");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'édition de la salle de projection : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
                try {
                    int roomNumber = (int) nbrComboBox.getSelectedItem();
                    changes change = new changes();
                    change.deleteRoom(roomNumber);
                    // Rafraîchir le tableau pour afficher les données mises à jour
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Effacer les données existantes
                    change.showAll(table, "salles");
                    changes.populateSalleNumbers(nbrComboBox);
                    // Reset all combo boxes to an unselected state
                    nbrComboBox.setSelectedIndex(-1);
                    // Clear all text fields
                    capacityTextField.setText("");

                } catch (Exception E) {
                    E.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la salle de projection : " + E.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        // BACK button
        JButton backButton = new JButton("BACK");
        int backButtonY = vCenter + 4 * (buttonHeight + 40);
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

        // Reset all combo boxes to an unselected state
        nbrComboBox.setSelectedIndex(-1);

        // Clear all text fields
        capacityTextField.setText("");



    }

    public static void main(String[] args) {
        //new ScreeningRooms();
    }

}
