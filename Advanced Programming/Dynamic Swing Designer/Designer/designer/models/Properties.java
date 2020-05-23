package designer.models;

import designer.views.DesignPanel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * https://www.codejava.net/java-se/swing/editable-jtable-example
 */
public class Properties extends AbstractTableModel {
    private Component component;
    private DesignPanel designPanel;
    private List<Property> properties;
    private final String[] columnNames = new String[]{"Name", "Type", "Value"};

    public Properties(Component component, DesignPanel designPanel, List<Property> properties) {
        this.component = component;
        this.designPanel = designPanel;
        this.properties = properties;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return properties.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < 0 || row >= properties.size() || column < 0 || column >= columnNames.length) {
            return null;
        }
        switch (column) {
            case 0:
                return properties.get(row).getName();
            case 1:
                return properties.get(row).getType();
            case 2:
                return properties.get(row).getValue();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (row < 0 || row >= properties.size() || column != 2) {
            return false;
        }
        return properties.get(row).isValueEditable();
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row < 0 || row >= properties.size() || column != 2) {
            return;
        }
        Property property = properties.get(row);
        property.update(value);
        component.update(property.getName(), value);
        designPanel.repaint();
        designPanel.revalidate();
    }
}
