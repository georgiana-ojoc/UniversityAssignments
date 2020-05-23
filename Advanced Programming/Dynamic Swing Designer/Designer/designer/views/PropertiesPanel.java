package designer.views;

import designer.models.Properties;

import javax.swing.*;

public class PropertiesPanel extends JScrollPane {
    private JTable table;

    public PropertiesPanel() {
        table = new JTable();
        setViewportView(table);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    public void update(Properties properties) {
        table = new JTable(properties);
        setViewportView(table);
    }
}
