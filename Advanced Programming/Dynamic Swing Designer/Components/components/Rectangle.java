package components;

import javax.swing.*;
import java.awt.*;

@Text("Rectangle")
public class Rectangle extends JComponent {
    public Rectangle(String text) {
        setToolTipText(text);
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.green);
        graphics.fillRect(0, 0, 100, 100);
    }
}