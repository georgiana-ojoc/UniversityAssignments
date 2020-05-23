package designer.views;

import designer.controllers.Controller;

import javax.swing.*;

public class ControlPanel extends JPanel {

    public ControlPanel(Controller controller) {
        JButton loadButton = new JButton("Load");
        add(loadButton);
        loadButton.addActionListener(actionEvent -> controller.load());
        JButton saveButton = new JButton("Save");
        add(saveButton);
        saveButton.addActionListener(actionEvent -> controller.save());
        JButton resetButton = new JButton("Reset");
        add(resetButton);
        resetButton.addActionListener(actionEvent -> controller.reset());
    }
}
