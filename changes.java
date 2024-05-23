import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class changes {

    static Statement mainStatement;
    static Statement archiveStatement;
    private static Connection mainConnection;
    private static Connection archiveConnection;
    private LocalTime duree;


    changes(){

        JTable table;

        try {
            mainConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema3?user=root&password=meriem123");
            mainStatement = mainConnection.createStatement();
            archiveConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/archive?user=root&password=meriem123");
            archiveStatement = archiveConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLeftTicketsNbr(int diffusionId) {
        try {
            ResultSet rs = mainStatement.executeQuery("SELECT COUNT(*) AS nb_billets FROM billets WHERE diffusion_id = " + diffusionId);
            int nbBillets = 0;
            if (rs.next()) {
                nbBillets = rs.getInt("nb_billets");
            }
            // Get the capacity of the room
            rs = mainStatement.executeQuery("SELECT capacite FROM salles JOIN diffusions ON diffusions.salle_id = salles.num WHERE diffusions.id = " + diffusionId);
            int capacite = 0;
            if (rs.next()) {
                capacite = rs.getInt("capacite");
            }
            rs.close();
            return (capacite - nbBillets);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public int getNextTicketId() {
        int nextTicketId = 0;
        try {
            Statement statement = mainConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(num) AS max_id FROM billets");
            if (resultSet.next()) {
                nextTicketId = resultSet.getInt("max_id") + 1;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextTicketId;
    }
    public void populateScreeningRoomNumbersFromDB(JComboBox<Integer> comboBox) {
        try {

            String query = "SELECT num FROM salles";

            // Create and execute the mainStatement
            PreparedStatement statement = mainConnection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int roomNumber = resultSet.getInt("num");
                comboBox.addItem(roomNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}
    public int getSalleCapacity(int salleId) {
        int capacity = 0;
        try {
            String sql = "SELECT capacite FROM salles WHERE num = ?";
            PreparedStatement pstmt = mainConnection.prepareStatement(sql);
            pstmt.setInt(1, salleId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                capacity = rs.getInt("capacite");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return capacity;
    }
    public void addTicket(int diffusionId,  int clientId, String methodePayment) {
        try {
            double price = getPriceFromDiffusion(diffusionId);
            if (getLeftTicketsNbr(diffusionId) > 0) {
                mainStatement.executeUpdate("INSERT INTO billets (prix, diffusion_id, client_id, methodePayment) VALUES (" + price + ", " + diffusionId + ", " + clientId + ", '" + methodePayment + "')");
                JOptionPane.showMessageDialog(null, "Ticket is added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "There are no tickets left for this screening.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ticket is not added.");
        }
    }
    private double getPriceFromDiffusion(int diffusionId) throws SQLException {
        double price = -1; // Default price if retrieval fails
        String query = "SELECT prix FROM diffusions WHERE id = ?";
        try (PreparedStatement preparedStatement = mainConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, diffusionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("prix");
                }
            }
        }
        return price;
    }
    public void populateNameDiffusions(JComboBox<Film> nameComboBox) {
        try {
            String query = "SELECT DISTINCT f.id, f.nom, f.version FROM films f JOIN diffusions d ON f.id = d.film_id";
            try (Statement statement = mainConnection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    int movieId = resultSet.getInt("id");
                    String movieName = resultSet.getString("nom");
                    String version = resultSet.getString("version");
                    String displayText = movieName + " : " + version;
                    Film movie = new Film(movieId, displayText);
                    nameComboBox.addItem(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void populateNameComboBox(JComboBox<Film> nameComboBox) {

        String query = "SELECT id, nom, version FROM films";
        try (PreparedStatement preparedStatement = mainConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Clear existing items
            nameComboBox.removeAllItems();

            while (resultSet.next()) {
                int movieId = resultSet.getInt("id");
                String movieName = resultSet.getString("nom");
                String version = resultSet.getString("version");
                String displayText = movieName + " : " + version;
                Film movie = new Film(movieId, displayText);
                nameComboBox.addItem(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading movie names: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    public void populateDiffusionIDs(JComboBox<Integer> idComboBox, int movieId) {
        try {
            String query = "SELECT id FROM diffusions WHERE film_id = ?";
            PreparedStatement preparedStatement = mainConnection.prepareStatement(query);
            preparedStatement.setInt(1, movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int diffusionID = resultSet.getInt("id");
                idComboBox.addItem(diffusionID);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getDiffusionPrice(int diffusionId) {
        double price = 0.0;
        try {
            String query = "SELECT prix FROM diffusions WHERE id = ?";
            PreparedStatement preparedStatement = mainConnection.prepareStatement(query);
            preparedStatement.setInt(1, diffusionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                price = resultSet.getDouble("prix");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
        return price;
    }
    public void populateSalleNumbers(JComboBox<Integer> comboBox) {
        // SQL query to select salle numbers
        String query = "SELECT num FROM salles ORDER BY num ASC";
        // Using try-with-resources to ensure that all resources are closed
        try {
            PreparedStatement stmt = mainConnection.prepareStatement("SELECT num FROM salles ORDER BY num ASC");
            ResultSet rs = stmt.executeQuery() ;
            comboBox.removeAllItems();  // Clear existing items
            while (rs.next()) {
                int num = rs.getInt("num");
                comboBox.addItem(num);  // Add salle number to the JComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to populate salle numbers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Date getDiffusionDate(int diffusionId) {
        try {
            Statement statement = mainConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT date FROM diffusions WHERE id = " + diffusionId);
            if (resultSet.next()) {
                // Assuming the date is stored as a Time in the database
                return resultSet.getDate("date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // If no date is found or an exception occurs, return null
        return null;
    }

    public void cancelTicket(int idBillet) {
        try {
            mainStatement.executeUpdate("DELETE FROM billets WHERE num =" + idBillet);
            JOptionPane.showMessageDialog(null, "Reservation is cancelled successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Reservation is not cancelled.");
        }
    }
    public int addClient(String nom,  String numTelephone) {
        int clientId = -1;
        try {
            mainStatement.executeUpdate("INSERT INTO clients (nom, numTelephone) VALUES ('" + nom + "', '" + numTelephone + "')",
                    Statement.RETURN_GENERATED_KEYS);
            //JOptionPane.showMessageDialog(null, "Client is added successfully.");
            ResultSet rs = mainStatement.getGeneratedKeys();
            if (rs.next()) {
                clientId = rs.getInt(1); // Get the first generated key (assuming client ID is the first column)
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Client is not added.");

        }
        return clientId;
    }




    public void updateClient(int clientId, String newNom, String newPrenom, String newNumTelephone) {

        try {
            int rowsUpdated = mainStatement.executeUpdate("UPDATE clients SET nom = '" + newNom + "', prenom = '" + newPrenom + "', numTelephone = '" + newNumTelephone + "' WHERE id = " + clientId);
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Client was updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No clients found with specified ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No clients found with specified ID.");
        }
    }

    // search for a client by name, last name, ID, or phone number
    public void searchClient(String searchQuery, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the existing table data
        try {
            ResultSet rs = mainConnection.createStatement().executeQuery("SELECT * FROM clients WHERE (nom LIKE '%" + searchQuery + "%' AND prenom LIKE '%" + searchQuery + "%') OR (nom LIKE '%" + searchQuery + "%' AND prenom LIKE '%" + searchQuery + "%') OR id = '" + searchQuery + "' OR numTelephone = '" + searchQuery + "'" );
            while (rs.next()) {
                String retrievedNom = rs.getString("nom");
                String retrievedPrenom = rs.getString("prenom");
                String retrievedNumTelephone = rs.getString("numTelephone");
                int retrievedId = rs.getInt("id");
                model.addRow(new Object[]{retrievedNom, retrievedPrenom, retrievedNumTelephone, retrievedId});
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There are no existing clients with the entered information.");
        }
    }
    public void populateMovieIDs(JComboBox<Integer> comboBox) {
        try {
            PreparedStatement stmt = mainConnection.prepareStatement("SELECT id FROM films ORDER BY id ASC");
            ResultSet rs = stmt.executeQuery();

            comboBox.removeAllItems();

            while (rs.next()) {
                int id = rs.getInt("id");
                comboBox.addItem(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to populate movie IDs: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Map<String, Object> getDiffusionDetails(int id) {
        Map<String, Object> details = new HashMap<>();
        String query = "SELECT film_id, salle_id, date, horaire_debut, prix FROM diffusions WHERE id = ?";
        try {
            PreparedStatement stmt = mainConnection.prepareStatement(query) ;
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.put("film_id", rs.getInt("film_id"));
                    details.put("salle_id", rs.getInt("salle_id"));
                    details.put("date", rs.getDate("date"));
                    details.put("horaire_debut", rs.getTime("horaire_debut"));
                    details.put("prix", rs.getDouble("prix"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching diffusion details: " + e.getMessage());
        }
        return details;
    }
    public byte[] getPhoto(int filmId) {
        String query = "SELECT photo FROM films WHERE id = ?";
        try (PreparedStatement pstmt = mainConnection.prepareStatement(query)) {
            pstmt.setInt(1, filmId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("photo");  // Ensure that this correctly fetches the blob data
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object getElement(int id, String table, String column) {
        try {
            String idN="id";
            if (table.equals("salles")){idN = "num";}
            Statement statement = mainConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(" SELECT "+ column +" FROM "+ table +" WHERE "+ idN + "  =  " + id +"");
            if (resultSet.next()) {
                return resultSet.getObject(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String logIn(String username, String password) {
        try {
            // verifier si l'utilisateur existe
            PreparedStatement verifier = mainConnection.prepareStatement("SELECT * FROM utilisateurs WHERE username = ?");
            verifier.setString(1, username);
            ResultSet rs = verifier.executeQuery();

            if (!rs.next()) {
                return "false";
            }

            // verifier son mot de passe
            String vraiPassword = rs.getString("password");
            if (password.equals(vraiPassword)) {
                // Récupérer le type de l'utilisateur
                String typeUtilisateur = rs.getString("type");
                return typeUtilisateur;

            } else {
                return "false";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "false";
        }
    }
    public int isRoomAvailable(int numSalle, Date date, Time heureDebut, Time heureFin) {
        try {
            String sql = "SELECT id FROM diffusions " +
                    "WHERE salle_id = ? AND date = ? AND (" +
                    "(horaire_debut < ? AND horaire_fin > ?) OR " +
                    "(horaire_debut BETWEEN ? AND ?) OR " +
                    "(horaire_fin BETWEEN ? AND ?))";

            PreparedStatement stmt = mainConnection.prepareStatement(sql);
            stmt.setInt(1, numSalle);
            stmt.setDate(2, date);
            stmt.setTime(3, heureDebut);
            stmt.setTime(4, heureFin);
            stmt.setTime(5, heureDebut);
            stmt.setTime(6, heureFin);
            stmt.setTime(7, heureDebut);
            stmt.setTime(8, heureFin);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");  // Returns the id of the conflicting diffusion
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean hasTimePassed(Date startDate, Time startTime) {
        // Convert java.sql.Date to LocalDate
        LocalDate datePart = startDate.toLocalDate();

        LocalTime timePart = startTime.toLocalTime();

        LocalDateTime startDateTime = LocalDateTime.of(datePart, timePart);

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        return startDateTime.isBefore(now);
    }
    public boolean isDurationLongerThanFourHours(Time duration) {
        LocalTime time = duration.toLocalTime();
        long totalHours = time.getHour();
        return totalHours > 4;
    }
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startScheduledDeletion() {
        Runnable deleteExpired = () -> {
            try {
                System.out.println("Scheduled deletion task started for tickets and diffusions at: " + new java.util.Date());
                deleteExpiredTickets();   // First handle the deletion of tickets
                deleteExpiredDiffusions(); // Then handle the deletion of diffusions
                System.out.println("Scheduled deletion task for tickets and diffusions completed at: " + new java.util.Date());
            } catch (Exception e) {
                System.err.println("Error during scheduled deletion: " + e.getMessage());
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(deleteExpired, 0, 1, TimeUnit.HOURS); // Executes every hour
        System.out.println("Deletion scheduler has been setup to run every hour for both tickets and diffusions.");
    }

    private static void deleteExpiredDiffusions() {
        ResultSet rs = null;

        try {
            mainConnection.setAutoCommit(false);
            archiveConnection.setAutoCommit(false);

            // SQL to select expired diffusions with film names
            String selectSql = "SELECT d.salle_id, d.date, d.horaire_debut, d.horaire_fin, d.prix, f.nom AS film_name " +
                    "FROM diffusions d " +
                    "JOIN films f ON d.film_id = f.id " +
                    "WHERE d.date < CURDATE() OR (d.date = CURDATE() AND d.horaire_fin < CURRENT_TIME())";
            PreparedStatement selectStmt = mainConnection.prepareStatement(selectSql);
            rs = selectStmt.executeQuery();

            // SQL to insert into archive database
            String archiveSql = "INSERT INTO diffusions (film_name, salle_num, date, horaire_debut, horaire_fin, prix) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement archiveStmt = archiveConnection.prepareStatement(archiveSql);

            // Loop through results and add to archive
            while (rs.next()) {
                archiveStmt.setString(1, rs.getString("film_name"));
                archiveStmt.setInt(2, rs.getInt("salle_id"));
                archiveStmt.setDate(3, rs.getDate("date"));
                archiveStmt.setTime(4, rs.getTime("horaire_debut"));
                archiveStmt.setTime(5, rs.getTime("horaire_fin"));
                archiveStmt.setDouble(6, rs.getDouble("prix"));
                archiveStmt.executeUpdate();
            }

            archiveConnection.commit();

            // Delete after archiving
            String deleteSql = "DELETE FROM diffusions WHERE date < CURDATE() OR (date = CURDATE() AND horaire_fin < CURRENT_TIME())";
            PreparedStatement deleteStmt = mainConnection.prepareStatement(deleteSql);
            int rowsDeleted = deleteStmt.executeUpdate();
            mainConnection.commit();

            System.out.println(rowsDeleted + " diffusions deleted and archived successfully.");
        } catch (SQLException e) {
            try {
                if (mainConnection != null) mainConnection.rollback();
                if (archiveConnection != null) archiveConnection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    private static void deleteExpiredTickets() {
        ResultSet rs = null;
        try {
            mainConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema?user=root&password=31574801");
            mainStatement = mainConnection.createStatement();
            archiveConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/archive?user=root&password=31574801");
            archiveStatement = archiveConnection.createStatement();

            // SQL to select expired tickets using diffusion information
            String selectSql = "SELECT b.num, b.prix, b.methodePayment, d.date, d.horaire_fin, c.nom AS client_nom " +
                    "FROM billets b " +
                    "JOIN diffusions d ON b.diffusion_id = d.id " +
                    "JOIN clients c ON b.client_id = c.id " +
                    "WHERE d.date < CURDATE() OR (d.date = CURDATE() AND d.horaire_fin < CURRENT_TIME())";
            PreparedStatement selectStmt = mainConnection.prepareStatement(selectSql);
            rs = selectStmt.executeQuery();

            // SQL to insert into archive database for tickets
            String archiveSql = "INSERT INTO billets ( prix, client_nom, methodePayment) VALUES ( ?, ?, ?)";
            PreparedStatement archiveStmt = archiveConnection.prepareStatement(archiveSql);

            // Loop through results and add to archive
            while (rs.next()) {
                archiveStmt.setDouble(1, rs.getDouble("prix"));
                archiveStmt.setString(2, rs.getString("client_nom"));
                archiveStmt.setString(3, rs.getString("methodePayment"));
                archiveStmt.executeUpdate();
            }


            // Delete after archiving
            String deleteSql = "DELETE b FROM billets b " +
                    "JOIN diffusions d ON b.diffusion_id = d.id " +
                    "WHERE d.date < CURDATE() OR (d.date = CURDATE() AND d.horaire_fin < CURRENT_TIME())";
            PreparedStatement deleteStmt = mainConnection.prepareStatement(deleteSql);
            int rowsDeleted = deleteStmt.executeUpdate();

            System.out.println(rowsDeleted + " tickets deleted and archived successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public void add(String table2, Object... args) throws DoubleRoomBooking, BookingTimePassed{

        for (Object arg : args) {
            if (arg == null || (arg instanceof String && ((String) arg).isEmpty())) {
                JOptionPane.showMessageDialog(null, "Not enough information! Please enter all the information.");
                return;
            }
        }


        String sql = "";
        try {

            switch (table2) {
                case "films":

                    String nom = (String) args[0];
                    String version = (String) args[1];
                    String genre = (String) args[2];
                    java.sql.Time duree = (java.sql.Time) args[3];
                    byte[] imageBytes = (byte[]) args[4];
                    if (isDurationLongerThanFourHours(duree)){
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String durationStr = timeFormat.format(duree);

                        int response = JOptionPane.showConfirmDialog(null,
                                "You entered a movie with " + durationStr + " duration, Are you sure you want to add it?",
                                "Confirm Movie Addition",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.NO_OPTION | response == JOptionPane.CANCEL_OPTION) return;
                    }
                    sql = "INSERT INTO " + table2 + " (nom, version, genre, duree, photo) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = mainConnection.prepareStatement(sql);
                    pstmt.setString(1, nom);
                    pstmt.setString(2, version);
                    pstmt.setString(3, genre);
                    pstmt.setTime(4, duree);
                    if (imageBytes != null) {
                        pstmt.setBytes(5, imageBytes);
                    } else {
                        pstmt.setNull(5, Types.BLOB);
                    }
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Film is added!");
                    break;
                case "salles":
                    int capacite = Integer.parseInt((String) args[0]);
                    sql = "INSERT INTO salles (capacite) VALUES (?)";
                    pstmt = mainConnection.prepareStatement(sql);
                    pstmt.setInt(1, capacite);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Screening room is added!");
                    break;
                case "diffusions":
                    int filmId = (int) args[0];
                    int salleId = (int) args[1];
                    java.sql.Date date = (java.sql.Date) args[2];
                    java.sql.Time horaireDebut = (java.sql.Time) args[3];
                    double prix = (Double) args[4];
                    java.sql.Time dureeF = (java.sql.Time) getElement(filmId,"films","duree");
                    Time extraThirtyMinutes = new Time(0, 30, 0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(horaireDebut.getTime());
                    calendar.add(Calendar.HOUR, dureeF.getHours());
                    calendar.add(Calendar.MINUTE, dureeF.getMinutes());
                    calendar.add(Calendar.SECOND, dureeF.getSeconds());
                    calendar.add(Calendar.MINUTE, extraThirtyMinutes.getMinutes());
                    Time finalTime = new Time(calendar.getTimeInMillis());
                    if (isRoomAvailable(salleId,date,horaireDebut,finalTime)!=-1)throw new DoubleRoomBooking("this room is not available at this time, please choose another room.");
                    if (hasTimePassed(date,horaireDebut)) throw new BookingTimePassed("The timing you choose has already passed, please choose a valid time.");
                    sql = "INSERT INTO diffusions (film_id, salle_id, date, horaire_debut, prix) VALUES (?, ?, ?, ?, ?)";
                    pstmt = mainConnection.prepareStatement(sql);
                    pstmt.setInt(1, filmId);
                    pstmt.setInt(2, salleId);
                    pstmt.setDate(3, date);
                    pstmt.setTime(4, horaireDebut);
                    pstmt.setDouble(5, prix);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Screening is added!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid entry type!");
                    return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Entry could not be added due to an error: " + e.getMessage());
        }
    }

    public void edit(String table2,int id, Object... args) throws DoubleRoomBooking,BookingTimePassed{

        for(Object arg : args){
            if(arg == null || (arg instanceof String && ((String) arg).isEmpty())){
                JOptionPane.showMessageDialog(null, "Not enough informations ! please enter all the informaations");
                return;
            }
        }
        try {
            switch (table2) {
                case "films":
                    String nom = (String) args[0];
                    String version = (String) args[1];
                    String genre = (String) args[2];
                    java.sql.Time duree = (java.sql.Time) args[3];
                    byte[] imageBytes = (byte[]) args[4];
                    String sql = "UPDATE " + table2 + " SET nom = '" + nom + "', version = '" + version +
                            "', genre = '" + genre + "', duree = '" + duree + "', photo = ? WHERE id = " + id;
                    PreparedStatement pstmt = mainConnection.prepareStatement(sql);

                    if (imageBytes != null) {
                        pstmt.setBytes(1, imageBytes);
                    } else {
                        pstmt.setNull(1, Types.BLOB);
                    }

                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Film is edited!");
                    break;
                case "salles":
                    int capacite = (int) args[0];
                    mainStatement.executeUpdate("UPDATE salles SET num = '" + id + "', capacite = " + capacite + " WHERE num = " + id);
                    JOptionPane.showInputDialog("Screening room is edited !");
                    break;
                case "diffusions":
                    int filmId = (int) args[0];
                    int salleId = (int) args[1];
                    java.sql.Date date = (java.sql.Date) args[2];
                    java.sql.Time horaireDebut = (java.sql.Time) args[3];
                    double prix = (double) args[4];
                    if (hasTimePassed(date,horaireDebut)) throw new BookingTimePassed("The timing you choose has already passed, please choose a valid time.");
                    java.sql.Time dureeF = (java.sql.Time) getElement(filmId,"films","duree");
                    Time extraThirtyMinutes = new Time(0, 30, 0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(horaireDebut.getTime());
                    calendar.add(Calendar.HOUR, dureeF.getHours());
                    calendar.add(Calendar.MINUTE, dureeF.getMinutes());
                    calendar.add(Calendar.SECOND, dureeF.getSeconds());
                    calendar.add(Calendar.MINUTE, extraThirtyMinutes.getMinutes());
                    Time finalTime = new Time(calendar.getTimeInMillis());
                    if (isRoomAvailable(salleId,date,horaireDebut,finalTime)!=id && isRoomAvailable(salleId,date,horaireDebut,finalTime)!=-1 )throw new DoubleRoomBooking("the room you chose is not available at this time, please choose another room.");
                    mainStatement.executeUpdate("UPDATE diffusions SET film_id = '" + filmId + "', salle_id = '" + salleId + "', date = '" + date + "', horaire_debut = '" + horaireDebut  + "', prix = '" + prix  + "' WHERE id = " + id);
                    JOptionPane.showMessageDialog(null,"Screening is edited !");
                    break;
                default:
                    JOptionPane.showMessageDialog(null,"Invalid entry type!");
                    return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Entry could not be added!");
        }
    }

    public ResultSet getMoviesWithPosters() throws SQLException {
        // Ensure your database connection is set up correctly before this method is called
        if (mainConnection == null || mainConnection.isClosed()) {
            throw new SQLException("Database connection is closed or not initialized.");
        }

        String query = "SELECT id, nom, photo FROM films";  // Adjust the column names based on your database schema
        Statement statement = mainConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        return rs;
    }

    public void filter(JPanel moviesPanel, String searchText, JComboBox idTextField, JFrame galleryFrame) {
        moviesPanel.removeAll(); // Remove all components initially
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; // Align items to the top-left
        gbc.gridx = 0;
        gbc.gridy = 0;

        try {
            ResultSet rs = getMoviesWithPosters(); // Assuming this is accessible here; might need adjustment based on scope
            int movieCount = 0;
            while (rs.next()) {
                int movieId = rs.getInt("id");
                String movieName = rs.getString("nom");
                byte[] imgBytes = rs.getBytes("photo");

                if (imgBytes != null && movieName.toLowerCase().contains(searchText.toLowerCase())) {
                    ImageIcon imageIcon = new ImageIcon(imgBytes);
                    Image image = imageIcon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);

                    JPanel moviePanel = new JPanel(new BorderLayout());
                    moviePanel.setBackground(new Color(166, 64, 64, 58));
                    moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    moviePanel.setPreferredSize(new Dimension(200, 310)); // Adjust width and height

                    JLabel imageLabel = new JLabel(new ImageIcon(image));
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);

                    JLabel textLabel = new JLabel(movieName, SwingConstants.CENTER);
                    textLabel.setForeground(Color.WHITE);

                    moviePanel.add(imageLabel, BorderLayout.CENTER);
                    moviePanel.add(textLabel, BorderLayout.SOUTH);

                    moviePanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            idTextField.setSelectedItem(movieId);
                            galleryFrame.dispose();
                        }
                    });

                    gbc.gridx = movieCount % 4;
                    gbc.gridy = movieCount / 4;
                    moviesPanel.add(moviePanel, gbc);

                    movieCount++;
                }
            }

            // Add a filler panel to push content to the top-left, only after all movies are added
            gbc.gridx = 0;
            gbc.gridy = (movieCount / 4) + 1;
            gbc.gridwidth = 4;
            gbc.weightx = 1;
            gbc.weighty = 1;
            moviesPanel.add(new JPanel(), gbc);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading movie data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        moviesPanel.revalidate();
        moviesPanel.repaint();
    }






    public void deleteRoom(int num) {
        try {
            // Check for associated diffusions using a dedicated Statement

            Statement stmt1 = mainConnection.createStatement();
            ResultSet rs = stmt1.executeQuery("SELECT COUNT(*) FROM diffusions WHERE salle_id = " + num);
            if (rs.next()) {
                int diffusionCount = rs.getInt(1);
                if (diffusionCount > 0) {
                    int option = JOptionPane.showConfirmDialog(null, "There are " + diffusionCount + " associated screenings. Are you sure you want to delete this screening room?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }


            // If there are diffusions, delete tickets and diffusions using separate Statements
            Statement stmt2 = mainConnection.createStatement();
            ResultSet rs1 = stmt2.executeQuery("SELECT id FROM diffusions WHERE salle_id = " + num) ;
            while (rs1.next()) {
                int diffusionId = rs1.getInt("id");
                try (Statement stmt3 = mainConnection.createStatement()) {
                    stmt3.execute("DELETE FROM billets WHERE diffusion_id = " + diffusionId);
                }
            }


            // Finally, delete the diffusions and the room
            Statement stmt4 = mainConnection.createStatement() ;
            stmt4.execute("DELETE FROM diffusions WHERE salle_id = " + num);
            stmt4.execute("DELETE FROM salles WHERE num = " + num);


            JOptionPane.showMessageDialog(null, "Screening room is deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Screening room is not deleted", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void deleteMovie(int movieId) {
        if (movieId < 1) {
            JOptionPane.showMessageDialog(null, "Select a movie first!");
            return;
        }

        try {Connection conn = mainConnection; // Assuming mainConnection is your Connection object
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM diffusions WHERE film_id = " + movieId);

            if (rs.next()) {
                int diffusionCount = rs.getInt(1);
                if (diffusionCount > 0) {
                    int option = JOptionPane.showConfirmDialog(null, "There are " + diffusionCount + " associated screenings. Are you sure you want to delete this movie?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // Delete tickets associated with the movie's diffusions
            Statement deleteTicketStmt = conn.createStatement();
            ResultSet diffusionRs = checkStmt.executeQuery("SELECT id FROM diffusions WHERE film_id = " + movieId) ;
            while (diffusionRs.next()) {
                int diffusionId = diffusionRs.getInt("id");
                deleteTicketStmt.execute("DELETE FROM billets WHERE diffusion_id = " + diffusionId);
            }


            // Delete diffusions associated with the movie
            Statement deleteDiffusionStmt = conn.createStatement();
            deleteDiffusionStmt.execute("DELETE FROM diffusions WHERE film_id = " + movieId);


            // Finally, delete the movie
            try (Statement deleteMovieStmt = conn.createStatement()) {
                deleteMovieStmt.execute("DELETE FROM films WHERE id = " + movieId);
            }

            JOptionPane.showMessageDialog(null, "Movie and all associated screenings and tickets have been deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete the movie: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void deleteDiffusion(int diffusionId) {
        if (diffusionId < 1) {
            JOptionPane.showMessageDialog(null, "Select a diffusion first!");
            return;
        }

        try{ Connection conn = mainConnection; // Assuming mainConnection is your Connection object
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM billets WHERE diffusion_id = " + diffusionId);

            if (rs.next()) {
                int ticketCount = rs.getInt(1);
                if (ticketCount > 0) {
                    int option = JOptionPane.showConfirmDialog(null, "There are " + ticketCount + " tickets sold for this screening. Are you sure you want to delete this screening?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
            }

            // Delete tickets associated with the diffusion
            Statement deleteTicketStmt = conn.createStatement();
            deleteTicketStmt.execute("DELETE FROM billets WHERE diffusion_id = " + diffusionId);


            // Delete the diffusion itself
            Statement deleteDiffusionStmt = conn.createStatement();
            deleteDiffusionStmt.execute("DELETE FROM diffusions WHERE id = " + diffusionId);


            JOptionPane.showMessageDialog(null, "Screening and all associated tickets have been deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete the screening: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }










    public void showAll(JTable table, String table2) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the existing table data

        try {
            ResultSet resultSet = mainStatement.executeQuery("SELECT * FROM " + table2);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Populate the model with data
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);

                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void populateDiffusionIDs(JComboBox comboBox) {
        try {
            comboBox.removeAllItems();
            PreparedStatement stmt = mainConnection.prepareStatement("SELECT id FROM diffusions ORDER BY id ASC");
            ResultSet rs = stmt.executeQuery() ;
            comboBox.removeAllItems();  // Clear existing items
            while (rs.next()) {
                int id = rs.getInt("id");
                comboBox.addItem(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error populating diffusion IDs: " + e.getMessage());
        }
    }

    public void signUp(String username, String password,String type ) {
        try {
            if (username.isEmpty()){
                JOptionPane.showMessageDialog(null, "Enter a valid username.");
                return;}
            // Vérifier si l'utilisateur existe déjà
            PreparedStatement verifier = mainConnection.prepareStatement("SELECT COUNT(*) FROM utilisateurs WHERE username = ?");
            verifier.setString(1, username);
            ResultSet rs = verifier.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    JOptionPane.showMessageDialog(null, "Username : " + username + "is already taken.");
                    return;
                }}

            // Inscrire l'utilisateur
            PreparedStatement inscription = mainConnection.prepareStatement("INSERT INTO utilisateurs (username, password,type) VALUES (?, ?,?)");
            inscription.setString(1, username);
            inscription.setString(2, password);
            inscription.setString(3, type);
            inscription.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inscription réussie pour l'utilisateur " + username + ".");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showScreenings(JTable table, String table2) {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the existing table data

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
    }

}
