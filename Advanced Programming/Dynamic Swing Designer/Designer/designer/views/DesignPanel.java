package designer.views;

import designer.models.Component;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DesignPanel extends JPanel implements Serializable {
    private List<Component> componentList;

    public DesignPanel() {
        setLayout(null);
        componentList = new ArrayList<>();

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (componentList == null) {
            return;
        }
        removeAll();
        revalidate();
        int width = 100;
        int height = 50;
        for (Component component : componentList) {
            JComponent jComponent = component.getComponent();
            int x = component.getX();
            int y = component.getY();
            jComponent.setBounds(x - width / 2, y - height / 2, width, height);
            add(jComponent);
        }
        repaint();
        revalidate();
    }
}
