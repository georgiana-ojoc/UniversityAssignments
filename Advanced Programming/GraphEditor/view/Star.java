package com.georgiana.ojoc.view;

import java.awt.*;


/**
 * inspiration: http://java-sl.com/shapes.html
 */
public class Star extends Polygon {
    static int cornerNumber = 5;

    public Star(int centerX, int centerY, int radius) {
        super(getPointsX(centerX, radius),
                getPointsY(centerY, radius),
                cornerNumber * 2);
    }

    private static int[] getPointsX(int centerX, int radius) {
        int[] pointsX = new int[cornerNumber * 2];
        int innerRadius = radius / 2;
        double degrees = 2 * Math.PI / cornerNumber;
        double angle = 45;
        double innerAngle = angle + Math.PI / cornerNumber;
        for (int index = 0; index < cornerNumber; ++index) {
            pointsX[index * 2] = (int)(centerX + Math.round(radius * Math.cos(angle)));
            angle += degrees;
            pointsX[index * 2 + 1]=(int)(centerX + Math.round(innerRadius * Math.cos(innerAngle)));
            innerAngle += degrees;
        }
        return pointsX;
    }

    private static int[] getPointsY(int centerY, int radius) {
        int[] pointsY = new int[cornerNumber * 2];
        int innerRadius = radius / 2;
        double degrees = 2 * Math.PI / cornerNumber;
        double angle = 45;
        double innerAngle = angle + Math.PI / cornerNumber;
        for (int index = 0; index < cornerNumber; ++index) {
            pointsY[index * 2] = (int)(centerY + Math.round(radius * Math.sin(angle)));
            angle += degrees;
            pointsY[index * 2 + 1]=(int)(centerY + Math.round(innerRadius * Math.sin(innerAngle)));
            innerAngle += degrees;
        }
        return pointsY;
    }
}
