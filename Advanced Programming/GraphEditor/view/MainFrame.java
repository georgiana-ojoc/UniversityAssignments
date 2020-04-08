package com.georgiana.ojoc.view;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class MainFrame extends JFrame {
    public MainFrame(ConfigurationPanel configurationPanel, DrawingPanel drawingPanel, ControlPanel controlPanel) {
        super("Draw Images");
        Dimension dimension = new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.6),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.6));
        setPreferredSize(dimension);
        initialize(configurationPanel, drawingPanel, controlPanel);
    }

    private void initialize(ConfigurationPanel configurationPanel, DrawingPanel drawingPanel, ControlPanel controlPanel) {
        add(configurationPanel, NORTH);
        add(drawingPanel, CENTER);
        add(controlPanel, SOUTH);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void open() {
        setVisible(true);
    }
}
