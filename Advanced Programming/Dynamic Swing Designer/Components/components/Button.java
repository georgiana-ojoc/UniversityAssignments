package components;

import javax.swing.*;
import java.awt.*;

@Text("Click")
public class Button extends JButton {
    public Button(String text) {
        super(text);
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    private int getDiameter() {
        return Math.min(getWidth(), getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics metrics = getGraphics().getFontMetrics(getFont());
        int diameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
        return new Dimension(diameter, diameter);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int diameter = getDiameter();
        int radius = diameter / 2;
        graphics.setColor(Color.lightGray);
        graphics.fillOval(getWidth() / 2 - radius, getHeight() / 2 - radius, diameter, diameter);
        graphics.drawOval(getWidth() / 2 - radius, getHeight() / 2 - radius, diameter, diameter);
        graphics.setColor(Color.darkGray);
        graphics.setFont(getFont());
        FontMetrics metrics = graphics.getFontMetrics(getFont());
        int stringWidth = metrics.stringWidth(getText());
        int stringHeight = metrics.getHeight();
        graphics.drawString(getText(), getWidth() / 2 - stringWidth / 2, getHeight() / 2 + stringHeight / 4);
    }
}
