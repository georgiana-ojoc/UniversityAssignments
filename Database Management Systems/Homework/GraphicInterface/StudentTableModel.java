import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private static final int COLUMN_NO = 0;
    private static final int COLUMN_LAST_NAME = 1;
    private static final int COLUMN_FIRST_NAME = 2;
    private static final int COLUMN_AVERAGE = 3;

    private String[] columnNames = {"Numar", "Nume", "Prenume", "Medie"};
    private List<Student> listStudents;

    public StudentTableModel(List<Student> listStudents) {
        this.listStudents = listStudents;
        int indexCount = 1;
        for (Student student : listStudents) {
            student.setIndex(indexCount++);
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return listStudents.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (listStudents.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = listStudents.get(rowIndex);
        Object returnValue;
        switch (columnIndex) {
            case COLUMN_NO:
                returnValue = student.getIndex();
                break;
            case COLUMN_LAST_NAME:
                returnValue = student.getLastName();
                break;
            case COLUMN_FIRST_NAME:
                returnValue = student.getFirstName();
                break;
            case COLUMN_AVERAGE:
                returnValue = student.getAverage();
                break;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
        return returnValue;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Student student = listStudents.get(rowIndex);
        if (columnIndex == COLUMN_NO) {
            student.setIndex((int) value);
        }
    }
}
