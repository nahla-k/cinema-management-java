import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

public class gradient extends JPanel {

    private Color[] colors;
    private float[] fractions;

    public gradient(Color[] colors, float[] fractions) {
        this.colors = colors;
        this.fractions = fractions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Create a LinearGradientPaint with the defined colors and fractions
        int width = getWidth();
        int height = getHeight();
        LinearGradientPaint gradientPaint = new LinearGradientPaint(0, 0, 0, height, fractions, colors);

        // Set the paint of the Graphics2D object to the gradient
        g2d.setPaint(gradientPaint);

        // Fill the panel with the gradient
        g2d.fillRect(0, 0, width, height);
    }}