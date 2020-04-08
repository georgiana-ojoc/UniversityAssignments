package com.georgiana.ojoc.model.shape;

import com.georgiana.ojoc.Main;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public abstract class Shape implements Serializable {
    protected double centerX;
    protected double centerY;
    protected int sideSize;
    protected Color color;

    public Shape(double centerX, double centerY, int sideSize, Color color) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.sideSize = sideSize;
        this.color = color;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterX(double centerX) { this.centerX = centerX; }

    public void setCenterY(double centerY) { this.centerY = centerY; }

    public boolean contains(double x, double y) {
        double squaredCenterPointDistance = Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2);
        return squaredCenterPointDistance < Math.pow(sideSize, 2);
    }

    public boolean intersects(double centerX, double centerY, int sideSize) {
        double squaredCenterDistance = Math.pow(this.centerX - centerX, 2) +
                Math.pow(this.centerY - centerY, 2);
        double squaredRadiusSum = Math.pow(this.sideSize + sideSize, 2);
        return squaredCenterDistance <= squaredRadiusSum;
    }

    public abstract void draw(Graphics2D graphics2D);

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Shape)){
            return false;
        }
        Shape shape = (Shape) object;
        return Double.compare(shape.getCenterX(), getCenterX()) == 0 &&
                Double.compare(shape.getCenterY(), getCenterY()) == 0 &&
                sideSize == shape.sideSize &&
                Objects.equals(color, shape.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCenterX(), getCenterY(), sideSize, color);
    }
}
