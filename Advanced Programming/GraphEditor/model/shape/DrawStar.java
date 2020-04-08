package com.georgiana.ojoc.model.shape;

import com.georgiana.ojoc.view.Star;

import java.awt.*;
import java.io.Serializable;

public class DrawStar extends Shape implements Serializable {
    public DrawStar(double centerX, double centerY, int sideSize, Color color) {
        super(centerX, centerY, sideSize, color);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        Star star = new Star((int) centerX, (int) centerY, sideSize);
        graphics2D.setColor(color);
        graphics2D.fill(star);
    }
}
