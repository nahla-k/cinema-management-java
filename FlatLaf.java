import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import java.sql.SQLException;

import javax.swing.*;

public class FlatLaf {
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
                new Login();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
