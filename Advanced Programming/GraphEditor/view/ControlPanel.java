package com.georgiana.ojoc.view;

import com.georgiana.ojoc.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ControlPanel extends JPanel {
    private Controller controller;

    public ControlPanel(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        createLoadButton();
        createSaveButton();
        createResetButton();
        createAutoLayoutGraphButton();
    }

    private void createLoadButton() {
        JButton loadButton = new JButton("Load");
        add(loadButton);
        loadButton.addActionListener(this::loadButtonClicked);
    }

    private void createSaveButton() {
        JButton saveButton = new JButton("Save");
        add(saveButton);
        saveButton.addActionListener(this::saveButtonClicked);
    }

    private void createResetButton() {
        JButton resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.addActionListener(this::resetButtonClicked);
    }

    private void createAutoLayoutGraphButton() {
        JButton autoLayoutGraphButton = new JButton("Auto layout graph");
        add(autoLayoutGraphButton);
        autoLayoutGraphButton.addActionListener(this::autoLayoutGraphButtonClicked);
    }

    private void loadButtonClicked(ActionEvent actionEvent) {
        controller.load();
    }

    private void saveButtonClicked(ActionEvent actionEvent) {
        controller.save();
    }

    private void resetButtonClicked(ActionEvent actionEvent) {
        controller.reset();
    }

    private void autoLayoutGraphButtonClicked(ActionEvent actionEvent) {
        controller.autoLayoutGraph();
    }
}
