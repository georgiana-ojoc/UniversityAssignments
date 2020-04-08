package com.georgiana.ojoc.controller;

import com.georgiana.ojoc.exception.InvalidExtensionException;
import com.georgiana.ojoc.model.graph.Edge;
import com.georgiana.ojoc.model.graph.FruchtermanReingold;
import com.georgiana.ojoc.model.graph.Graph;
import com.georgiana.ojoc.model.graph.Node;
import com.georgiana.ojoc.model.shape.*;
import com.georgiana.ojoc.model.shape.Shape;
import com.georgiana.ojoc.view.*;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

public class Controller {
    private ConfigurationPanel configurationPanel;
    private DrawingPanel drawingPanel;
    private MainFrame mainFrame;
    private Graph graph;
    private Node lastRightClickedNode = null;

    public Controller() {
        configurationPanel = new ConfigurationPanel();
        drawingPanel = new DrawingPanel(this);
        ControlPanel controlPanel = new ControlPanel(this);
        mainFrame = new MainFrame(configurationPanel, drawingPanel, controlPanel);
        graph = new Graph();
    }

    public void drawNode(int x, int y) {
        int sideSize = configurationPanel.getSideSize();
        if (!graph.intersects(x, y, sideSize)) {
            Color shapeColor = configurationPanel.getShapeColor();
            Shape shape = null;
            switch (configurationPanel.getShapeType()) {
                case CIRCLE: shape = new DrawCircle(x, y, sideSize, shapeColor); break;
                case SQUARE: shape = new DrawSquare(x, y, sideSize, shapeColor); break;
                case STAR: shape = new DrawStar(x, y, sideSize, shapeColor);
            }
            graph.addNode(shape);
            draw();
        }
    }

    public void eraseNode(int x, int y) {
        Node node = graph.getNode(x, y);
        if (node != null) {
            graph.removeNode(node);
            draw();
        }
    }

    public void drawEdge(int x, int y) {
        if (lastRightClickedNode == null) {
            lastRightClickedNode = graph.getNode(x, y);
        }
        else {
            Node currentRightClickedNode = graph.getNode(x, y);
            if (currentRightClickedNode != null) {
                if (!lastRightClickedNode.equals(currentRightClickedNode)) {
                    if (!lastRightClickedNode.isAdjacentTo(currentRightClickedNode)) {
                        if (!graph.intersects(lastRightClickedNode, currentRightClickedNode)) {
                            Color edgeColor = configurationPanel.getEdgeColor();
                            graph.addEdge(lastRightClickedNode, currentRightClickedNode, edgeColor);
                            draw();
                            lastRightClickedNode = null;
                        }
                    }
                }
            }
        }
    }

    public void eraseEdge(int x, int y) {
        Edge edge = graph.getEdge(x, y);
        if (edge != null) {
            graph.removeEdge(edge);
            draw();
        }
    }

    public void load() {
        try {
            JFileChooser fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                Graph loadedGraph = Graph.load(fileChooser.getSelectedFile().getAbsolutePath());
                if (loadedGraph != null) {
                    graph = loadedGraph;
                    draw();
                }
            }
        }
        catch (InvalidExtensionException ignored) {
        }
    }

    public void save() {
        try {
            JFileChooser fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (graph != null) {
                    graph.save(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        }
        catch (InvalidExtensionException ignored) {
        }
    }

    public void reset() {
        graph = new Graph();
        lastRightClickedNode = null;
        draw();
    }

    public void autoLayoutGraph() {
        FruchtermanReingold fruchtermanReingold = new FruchtermanReingold(graph);
        fruchtermanReingold.run();
        graph = fruchtermanReingold.getGraph();
        draw();
    }

    public void draw() {
        drawingPanel.draw(graph.draw());
    }

    public void open() {
        mainFrame.open();
    }
}
