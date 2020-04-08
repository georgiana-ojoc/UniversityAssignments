package com.georgiana.ojoc.model.shape;

import com.georgiana.ojoc.view.Square;

import java.awt.*;
import java.io.Serializable;

public class DrawSquare extends Shape implements Serializable {
    public DrawSquare(double centerX, double centerY, int sideSize, Color color) {
        super(centerX, centerY, sideSize, color);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        Square square = new Square(centerX, centerY, sideSize);
        graphics2D.setColor(color);
        graphics2D.fill(square);
    }
}
