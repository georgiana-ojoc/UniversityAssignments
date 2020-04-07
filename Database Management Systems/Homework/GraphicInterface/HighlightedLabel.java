import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HighlightedLabel extends JLabel {
    private List<Rectangle2D> rectangles = new ArrayList<>();
    private Color highlightColor = Color.YELLOW;

    public void reset() {
        rectangles.clear();
        repaint();
    }

    public void highlightText(String textToHighlight) {
        if (textToHighlight == null) {
            return;
        }
        reset();

        final String textToMatch = textToHighlight.toLowerCase().trim();
        if (textToMatch.length() == 0) {
            return;
        }
        textToHighlight = textToHighlight.trim();

        final String labelText = getText().toLowerCase();
        if (labelText.contains(textToMatch)) {
            FontMetrics fontMetrics = getFontMetrics(getFont());
            float width = -1;
            final float height = fontMetrics.getHeight() - 1;
            int index = 0;
            while (true) {
                index = labelText.indexOf(textToMatch, index);
                if (index == -1) {
                    break;
                }
                if (width == -1) {
                    String matchingText = getText().substring(index, index + textToHighlight.length());
                    width = fontMetrics.stringWidth(matchingText);
                }
                String beforeText = getText().substring(0, index);
                float x = fontMetrics.stringWidth(beforeText);
                rectangles.add(new Rectangle2D.Float(x, 1, width, height));
                index = index + textToMatch.length();
            }
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(),getHeight());
        if (rectangles.size() > 0) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Color color = graphics2D.getColor();
            for (Rectangle2D rectangle : rectangles) {
                graphics2D.setColor(highlightColor);
                graphics2D.fill(rectangle);
                graphics2D.setColor(Color.LIGHT_GRAY);
                graphics2D.draw(rectangle);
            }
            graphics2D.setColor(color);
        }
        super.paintComponent(graphics);
    }
}