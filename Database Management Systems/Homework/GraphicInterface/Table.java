import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class Table extends JFrame {

    public Table() {
        super("Studenti");

        createIndex();
        List<Student> listStudents = createListStudents();

        TableModel tableModel = new StudentTableModel(listStudents);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Filter");
        panel.add(label, BorderLayout.WEST);

        final TableRowSorter<TableModel> tableRowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(tableRowSorter);

        final JTextField filterText = new JTextField("");
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                update();
            }

            private void update() {
                String text = filterText.getText();
                if (text.length() == 0) {
                    tableRowSorter.setRowFilter(null);
                }
                else {
                    try {
                        tableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                    catch (PatternSyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        panel.add(filterText, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);

        HighlightRenderer highlightRenderer = new HighlightRenderer(filterText);
        table.setDefaultRenderer(Object.class, highlightRenderer);
        add(new JScrollPane(table), BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void createIndex() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                    "STUDENT","STUDENT");
            Statement statement = connection.createStatement();
            statement.execute("DROP INDEX medii");
            statement.execute("CREATE INDEX medii ON note(id_student)");
            connection.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<Student> createListStudents() {
        List<Student> listStudents = new ArrayList<>();
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                    "STUDENT","STUDENT");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.id, nume, prenume, " +
                    "TRUNC(AVG(valoare), 2) medie FROM studenti s JOIN note n ON s.id = n.id_student " +
                    "GROUP BY s.id, nume, prenume");
            while (resultSet.next()) {
                listStudents.add(new Student(resultSet.getInt("id"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getDouble("medie")));
            }
            connection.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listStudents;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Table().setVisible(true));
    }
}
