import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class CourseTableModel<T> extends AbstractTableModel {
    private List<T> rows = new ArrayList<>();

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public abstract String getColumnName(int column);

    @Override
    public Class<?> getColumnClass(int column) {
        if (rows.isEmpty()) {
            return Object.class;
        }
        Object cell = getValueAt(0, column);
        return cell != null ? cell.getClass() : Object.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return getValueAt(rows.get(row), column);
    }

    public abstract Object getValueAt(T t, int columnIndex);
}
