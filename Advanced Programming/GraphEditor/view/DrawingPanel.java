package com.georgiana.ojoc.view;

import com.georgiana.ojoc.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private BufferedImage bufferedImage;

    public DrawingPanel(final Controller controller) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    if (mouseEvent.getClickCount() == 1) {
                        controller.drawNode(mouseEvent.getX(), mouseEvent.getY());
                    }
                    else {
                        controller.eraseNode(mouseEvent.getX(), mouseEvent.getY());
                    }
                }
                else {
                    if (mouseEvent.getClickCount() == 1) {
                        controller.drawEdge(mouseEvent.getX(), mouseEvent.getY());
                    }
                    else {
                        controller.eraseEdge(mouseEvent.getX(), mouseEvent.getY());
                    }
                }
            }
        });
    }

    public void draw(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (bufferedImage != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.drawImage(bufferedImage, 0, 0, this);
        }
    }
}
