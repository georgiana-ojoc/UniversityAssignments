package com.georgiana.ojoc.model.graph;

import com.georgiana.ojoc.model.shape.Shape;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node implements Serializable {
    private Shape shape;
    private List<Edge> incidenceList;

    public Node(Shape shape) {
        this.shape = shape;
        incidenceList = new ArrayList<>();
    }

    public double getX() {
        return shape.getCenterX();
    }

    public double getY() {
        return shape.getCenterY();
    }

    public List<Edge> getIncidenceList() { return incidenceList; }

    public void setX(double centerX) { shape.setCenterX(centerX); }
    public void setY(double centerY) { shape.setCenterY(centerY); }

    public boolean isAdjacentTo(Node node) {
        for (Edge edge : incidenceList) {
            if (edge.getTarget().equals(node)) {
                return true;
            }
        }
        return false;
    }

    public void addIncidentEdge(Node node, Color edgeColor) {
        incidenceList.add(new Edge(this, node, edgeColor));
    }

    public void removeIncidentEdge(Edge edge) { incidenceList.remove(edge); }

    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    public boolean intersects(double centerX, double centerY, int sideSize) {
        return shape.intersects(centerX, centerY, sideSize);
    }

    public void draw(Graphics2D graphics2D) {
        shape.draw(graphics2D);
    }

    public void drawIncidentEdges(Graphics2D graphics2D) {
        for (Edge edge : incidenceList) {
            edge.draw(graphics2D);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Node)) {
            return false;
        }
        Node node = (Node) object;
        return Objects.equals(shape, node.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shape, incidenceList);
    }
}
