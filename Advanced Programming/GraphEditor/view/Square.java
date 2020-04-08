package com.georgiana.ojoc.view;

import java.awt.geom.RoundRectangle2D;

public class Square extends RoundRectangle2D.Double {
    public Square(double centerX, double centerY, double length) {
        super(centerX - length / 2, centerY - length / 2, length, length, 5, 5);
    }
}
