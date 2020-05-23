package designer.views;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.*;

public class MainFrame extends JFrame {
    private JSplitPane splitPane;

    public MainFrame(ConfigurationPanel configurationPanel, DesignPanel designPanel,
                     PropertiesPanel propertiesPanel, ControlPanel controlPanel) {
        super("Dynamic Swing Designer");
        Dimension dimension = new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8));
        setPreferredSize(dimension);
        add(configurationPanel, NORTH);
        splitPane = new JSplitPane();
        splitPane.setLeftComponent(designPanel);
        splitPane.setRightComponent(propertiesPanel);
        splitPane.setResizeWeight(1);
        add(splitPane, CENTER);
        add(controlPanel, SOUTH);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void changeDesignPanel(DesignPanel designPanel) {
        splitPane.setLeftComponent(designPanel);
    }
}
