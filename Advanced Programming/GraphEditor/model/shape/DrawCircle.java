package com.georgiana.ojoc.model.shape;

import com.georgiana.ojoc.view.Circle;

import java.awt.*;
import java.io.Serializable;

public class DrawCircle extends Shape implements Serializable {
    public DrawCircle(double centerX, double centerY, int sideSize, Color color) {
        super(centerX, centerY, sideSize, color);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        Circle circle = new Circle(centerX, centerY, sideSize);
        graphics2D.setColor(color);
        graphics2D.fill(circle);
    }
}
