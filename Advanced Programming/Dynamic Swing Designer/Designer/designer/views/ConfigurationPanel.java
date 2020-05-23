package designer.views;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class ConfigurationPanel extends JPanel {
    private JTextField jarPath;
    private JTextField swingComponent;
    private JTextField defaultText;

    public ConfigurationPanel() {
        add(new JLabel("JAR Path:"));
        jarPath = new JTextField(28);
        add(jarPath);
        add(new JLabel("Swing component:"));
        swingComponent = new JTextField(28);
        add(swingComponent);
        add(new JLabel("Default text:"));
        defaultText = new JTextField(28);
        add(defaultText);
    }
}
