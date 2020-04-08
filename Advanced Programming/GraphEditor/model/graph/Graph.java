package com.georgiana.ojoc.model.graph;

import com.georgiana.ojoc.exception.InvalidExtensionException;
import com.georgiana.ojoc.model.shape.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class Graph implements Serializable {
    private List<Node> nodes;
    int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.6);
    int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.5);

    public Graph() {
        nodes = new ArrayList<>();
    }

    public Graph(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int getNodeIndex(Node searchedNode) {
        for (int index = 0; index < nodes.size(); ++index) {
            if (nodes.get(index).equals(searchedNode)) {
                return index;
            }
        }
        return -1;
    }

    public Node getNode(double x, double y) {
        for (Node node : nodes) {
            if (node.contains(x, y)) {
                return node;
            }
        }
        return null;
    }

    public Edge getEdge(int x, int y) {
        for (Edge edge : getEdges()) {
            if (edge.isNear(x, y)) {
                return edge;
            }
        }
        return null;
    }

    public List<Node> getNodes() { return nodes; }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Node node : nodes) {
            for (Edge edge : node.getIncidenceList()) {
                if (!edges.contains(edge)) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    public void addNode(Shape shape) {
        nodes.add(new Node(shape));
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addEdge(Node source, Node target, Color edgeColor) {
        nodes.get(getNodeIndex(source)).addIncidentEdge(target, edgeColor);
        nodes.get(getNodeIndex(target)).addIncidentEdge(source, edgeColor);
    }

    public void removeEdge(Edge edge) {
        nodes.get(getNodeIndex(edge.getSource())).removeIncidentEdge(edge);
        nodes.get(getNodeIndex(edge.getTarget())).removeIncidentEdge(edge);
    }

    public boolean intersects(double centerX, double centerY, int sideSize) {
        for (Node node : nodes) {
            if (node.intersects(centerX, centerY, sideSize)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersects(Node source, Node target) {
        for (Node node : nodes) {
            if (!(node.equals(source) || node.equals(target))) {
                if (isOnEdge(node.getX(), node.getY(), source, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnEdge(double x, double y, Node source, Node target) {
        double squaredSourceNodeDistance = Math.pow(source.getX() - x, 2) +
                Math.pow(source.getY() - y, 2);
        double squaredNodeTargetDistance = Math.pow(x - target.getX(), 2) +
                Math.pow(y - target.getY(), 2);
        double squaredSourceTargetDistance = Math.pow(source.getX() - target.getX(), 2) +
                Math.pow(source.getY() - target.getY(), 2);
        return squaredSourceNodeDistance + squaredNodeTargetDistance == squaredSourceTargetDistance;
    }

    public BufferedImage draw() {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Node node : nodes) {
            node.drawIncidentEdges(graphics2D);
        }
        for (Node node : nodes) {
            node.draw(graphics2D);
        }
        return bufferedImage;
    }

    public void save(String path) throws InvalidExtensionException {
        if (!path.endsWith(".ser")) {
            throw new InvalidExtensionException();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static Graph load(String path) throws InvalidExtensionException {
        if (!path.endsWith(".ser")) {
            throw new InvalidExtensionException();
        }
        Graph loadedGraph;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            loadedGraph = (Graph) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return loadedGraph;
        }
        catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }
}
