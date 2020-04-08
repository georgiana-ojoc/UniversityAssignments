package com.georgiana.ojoc.view;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel extends JPanel {
    private JComboBox<String> shapeTypeComboBox = null;
    private JSpinner sideSizeSpinner = null;
    private JComboBox<String> shapeColorComboBox = null;
    private JComboBox<String> edgeColorComboBox = null;

    public ConfigurationPanel() {
        initialize();
    }

    private void initialize() {
        createShapeTypeOption();
        createSideSizeOption();
        createShapeColorOption();
        createEdgeColorOption();
    }

    private void createShapeTypeOption() {
        JLabel shapeTypeLabel = new JLabel("Shape type:");
        add(shapeTypeLabel);
        String[] shapeTypes = {"circle", "square", "star"};
        shapeTypeComboBox = new JComboBox<>(shapeTypes);
        add(shapeTypeComboBox);
    }

    private void createSideSizeOption() {
        JLabel sideSizeLabel = new JLabel("Side size:");
        add(sideSizeLabel);
        sideSizeSpinner = new JSpinner(new SpinnerNumberModel(50, 25, 75, 5));
        add(sideSizeSpinner);
    }

    private void createShapeColorOption() {
        JLabel shapeColorLabel = new JLabel("Shape color:");
        add(shapeColorLabel);
        String[] shapeColors = {"darkGray", "magenta", "orange"};
        shapeColorComboBox = new JComboBox<>(shapeColors);
        add(shapeColorComboBox);
    }

    private void createEdgeColorOption() {
        JLabel edgeColorLabel = new JLabel("Edge color:");
        add(edgeColorLabel);
        String[] edgeColors = {"green", "lightGray", "yellow"};
        edgeColorComboBox = new JComboBox<>(edgeColors);
        add(edgeColorComboBox);
    }

    public ShapeType getShapeType() {
        if (shapeTypeComboBox == null) {
            return ShapeType.CIRCLE;
        }
        switch (shapeTypeComboBox.getSelectedIndex()) {
            case 0: return ShapeType.CIRCLE;
            case 1: return ShapeType.SQUARE;
        }
        return ShapeType.STAR;
    }

    public int getSideSize() {
        if (sideSizeSpinner == null) {
            return 30;
        }
        return (int) sideSizeSpinner.getValue();
    }

    public Color getShapeColor() {
        if (shapeColorComboBox == null) {
            return Color.orange;
        }
        switch (shapeColorComboBox.getSelectedIndex()) {
            case 0: return Color.darkGray;
            case 1: return Color.magenta;
        }
        return Color.orange;
    }

    public Color getEdgeColor() {
        if (edgeColorComboBox == null) {
            return Color.lightGray;
        }
        switch (edgeColorComboBox.getSelectedIndex()) {
            case 0: return Color.green;
            case 1: return Color.lightGray;
        }
        return Color.yellow;
    }
}
