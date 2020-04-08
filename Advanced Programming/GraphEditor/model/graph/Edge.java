package com.georgiana.ojoc.model.graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.Objects;

public class Edge implements Serializable {
    private Node source;
    private Node target;
    private Color color;

    public Edge(Node source, Node target, Color color) {
        this.source = source;
        this.target = target;
        this.color = color;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public boolean isNear(int x, int y) {
        double sourceX = source.getX();
        double sourceY = source.getY();
        double targetX = target.getX();
        double targetY = target.getY();
        double sourceTargetDistance = Math.sqrt(Math.pow(targetY - targetX, 2) + Math.pow(sourceY - sourceX, 2));
        double pointEdgeDistance = Math.abs((targetY- sourceY) * x - (targetX - sourceX) * y +
                targetX * sourceY - targetY * sourceX) / sourceTargetDistance;
        return pointEdgeDistance < 5;
    }

    public void draw(Graphics2D graphics2D) {
        Line2D line2D = new Line2D.Double(source.getX(), source.getY(), target.getX(), target.getY());
        graphics2D.setStroke(new BasicStroke(2.0f));
        graphics2D.setColor(color);
        graphics2D.draw(line2D);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Edge)) {
            return false;
        }
        Edge edge = (Edge) object;
        return ((Objects.equals(source, edge.getSource()) &&
                Objects.equals(target, edge.getTarget())) ||
                (Objects.equals(source, edge.getTarget()) &&
                        Objects.equals(target, edge.getSource()))) &&
                Objects.equals(color, edge.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, getTarget(), color);
    }
}
