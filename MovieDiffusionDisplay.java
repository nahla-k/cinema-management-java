import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;

import static java.lang.StringTemplate.STR;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;

public class MovieDiffusionDisplay {
    private JFrame frame;
    private gradient mainPanel;
    private Connection mainConnection;
    private JTextField searchField;
    JComboBox idComboBox;
    Boolean selectDiffusion;


    public MovieDiffusionDisplay( JComboBox idComboBox,Boolean selectDiffusion) {
        try {
            mainConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema3?user=root&password=meriem123");
            createUI( idComboBox,selectDiffusion);
            this.idComboBox=idComboBox;
            this.selectDiffusion=selectDiffusion;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUI( JComboBox idComboBox,Boolean selectDiffusion) {
        frame = new JFrame("Movie Diffusions");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);

        Color[] redColors = {Color.decode("#1f1c18"), Color.decode("#8e0e00"), Color.decode("#1f1c18")};
        float[] redFractions = {0.0f, 0.52f, 1.0f};

        mainPanel = new gradient(redColors, redFractions);
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(34, 34, 34));


        // Search panel setup
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(80);
        searchField.putClientProperty("JTextField.placeholderText", "Type to search...");
        searchField.setPreferredSize(new Dimension(200, 24));
        searchPanel.add(searchField);
        frame.add(searchPanel, BorderLayout.NORTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterDiffusions();
            }

            public void removeUpdate(DocumentEvent e) {
                filterDiffusions();
            }
            public void changedUpdate(DocumentEvent e) {
                filterDiffusions();
            }
        });

        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        frame.setVisible(true);

        filterDiffusions(); // Initial display
    }

    private void filterDiffusions() {
        mainPanel.removeAll();
        String searchText = searchField.getText().toLowerCase();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; // Align items to the top-left
        gbc.gridx = 0;
        gbc.gridy = 0;

        try {
            String query = "SELECT d.id, f.nom, f.photo, d.date, d.horaire_debut, d.horaire_fin, d.salle_id, s.capacite, " +
                    "(SELECT COUNT(*) FROM billets b WHERE b.diffusion_id = d.id) AS tickets_sold " +
                    "FROM diffusions d JOIN films f ON d.film_id = f.id JOIN salles s ON s.num = d.salle_id " +
                    "WHERE LOWER(f.nom)  LIKE ? OR CAST(s.num AS CHAR) LIKE ? ";

            PreparedStatement pstmt = mainConnection.prepareStatement(query);
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();
            int movieCount = 0;
            while (rs.next()) {
                JPanel diffusionPanel = createDiffusionPanel(rs);
                gbc.gridx = movieCount % 3;
                gbc.gridy = movieCount / 3;
                mainPanel.add(diffusionPanel, gbc);
                movieCount++;
            }
            gbc.gridx = 0;
            gbc.gridy = (movieCount / 3) + 1;
            gbc.gridwidth = 3;
            gbc.weightx = 1;
            gbc.weighty = 1;
            mainPanel.add(new JPanel(), gbc);
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading diffusions.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createDiffusionPanel(ResultSet rs ) throws SQLException, IOException {
        int diffusionId = rs.getInt("id");
        String filmName = rs.getString("nom");
        byte[] imgBytes = rs.getBytes("photo");
        Time startTime = rs.getTime("horaire_debut");
        Time endTime = rs.getTime("horaire_fin");
        Date date = rs.getDate("date");
        int salleId = rs.getInt("salle_id");
        int capacity = rs.getInt("capacite");
        int ticketsSold = rs.getInt("tickets_sold");
        int seatsAvailable = capacity - ticketsSold;

        JPanel diffusionPanel = new JPanel(new BorderLayout());
        diffusionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        diffusionPanel.setBackground(new Color(166, 64, 64, 58));

        JLabel imageLabel = new JLabel();
        if (imgBytes != null) {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(imgBytes)).getScaledInstance(160, 220, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
        }
        diffusionPanel.add(imageLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(255, 255, 255, 0));
        textPanel.setPreferredSize(new Dimension(200, 180));
        textPanel.add(Box.createVerticalStrut(10));

        JLabel titleLabel = new JLabel(filmName);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Assuming you've defined the dateFormat earlier
        String formattedDate = dateFormat.format(date); // Assuming you have a Date object named 'date'

        JLabel dateLabel = new JLabel(String.format("Date: %s", formattedDate));

        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textPanel.add(dateLabel);
        textPanel.add(Box.createVerticalStrut(10));

        JLabel roomTimeLabel = new JLabel(
            "<html>Room: " + salleId + "<br>Start: " + startTime + "<br>End: " + endTime + "</html>"
        );

        roomTimeLabel.setForeground(Color.WHITE);
        roomTimeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textPanel.add(roomTimeLabel);
        textPanel.add(Box.createVerticalStrut(10));

        JProgressBar progressBar = new JProgressBar(0, capacity);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));
        progressPanel.setBackground(new Color(166, 64, 64, 0));

        // Setting up the progress bar properties
        progressBar.setValue(seatsAvailable);
        progressBar.setStringPainted(false);
        progressBar.setForeground(new Color(243, 239, 239, 239));

        // Set the size of the progress bar
        int progressBarHeight = 5;
        progressBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, progressBarHeight));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, progressBarHeight));
        progressBar.setMinimumSize(new Dimension(10, progressBarHeight)); // Ensure it has a minimal height when shrunk

// Adding horizontal struts to align with your styling needs
        progressPanel.add(Box.createHorizontalStrut(6)); // Space on the left
        progressPanel.add(progressBar);
        progressPanel.add(Box.createHorizontalStrut(6)); // Space on the right

// Set the same height for the progress panel as that of the progress bar
        progressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        textPanel.add(progressPanel);

        JLabel seatsLabel = new JLabel(seatsAvailable + " / " + capacity + " seats");
        seatsLabel.setForeground(Color.WHITE);
        seatsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textPanel.add(seatsLabel);

        diffusionPanel.add(textPanel, BorderLayout.CENTER);
        diffusionPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectDiffusion) {
                    idComboBox.setSelectedItem(diffusionId);
                } else {
                    idComboBox.setSelectedItem(salleId);
                }
                frame.dispose();
            }
        });
        return diffusionPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            new MovieDiffusionDisplay(null,true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
