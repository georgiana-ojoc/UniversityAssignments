import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class HighlightRenderer extends DefaultTableCellRenderer {
    private JTextField searchField;

    public HighlightRenderer(JTextField searchField) {
        this.searchField = searchField;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
                                                   int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
        JLabel original = (JLabel) component;
        HighlightedLabel label = new HighlightedLabel();
        label.setFont(original.getFont());
        label.setText(original.getText());
        label.setBackground(original.getBackground());
        label.setForeground(original.getForeground());
        label.setHorizontalTextPosition(original.getHorizontalTextPosition());
        label.highlightText(searchField.getText());
        return label;
    }
}
