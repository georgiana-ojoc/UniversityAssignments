package com.georgiana.ojoc.view;

import java.awt.geom.Ellipse2D;

public class Circle extends Ellipse2D.Double {
    public Circle(double centerX, double centerY, double radius) {
        super(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }
}
