import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Pagination {
    private static int course = 1;
    private static String title = null;

    private static TableModel createObjectDataModel() {
        return new CourseTableModel<Course>() {
            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Identifier";
                    case 1:
                        return "Surname";
                    case 2:
                        return "Name";
                    case 3:
                        return "Mark";
                    case 4:
                        return "Date";
                }
                return null;
            }

            @Override
            public Object getValueAt(Course course, int column) {
                switch (column) {
                    case 0:
                        return course.getIdentifier();
                    case 1:
                        return course.getSurname();
                    case 2:
                        return course.getName();
                    case 3:
                        return course.getMark();
                    case 4:
                        return course.getDate();
                }
                return null;
            }
        };
    }

    private static PaginationDataProvider<Course> createPaginationDataProvider() {
        final List<Course> courses = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                "STUDENT", "STUDENT")) {
            try (CallableStatement callableStatement = connection.prepareCall("{? = call creeaza_catalog(?)}")) {
                callableStatement.registerOutParameter(1, Types.VARCHAR);
                callableStatement.setInt(2, course);
                callableStatement.executeUpdate();
                title = callableStatement.getString(1);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + title)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        courses.add(new Course(resultSet.getString(1), resultSet.getString(2),
                                resultSet.getString(3), resultSet.getInt(4),
                                resultSet.getDate(5)));
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return new PaginationDataProvider<>() {
            @Override
            public int getRowCount() {
                return courses.size();
            }

            @Override
            public List<Course> getRows(int start, int end) {
                return courses.subList(start, end);
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.85)));
        return frame;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            course = Integer.parseInt(args[0]);
        }
        PaginationDataProvider<Course> paginationDataProvider = createPaginationDataProvider();
        JFrame frame = createFrame();
        JTable table = new JTable(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        PaginatedTableDecorator<Course> paginatedTableDecorator = PaginatedTableDecorator.decorate(table,
                paginationDataProvider, new int[] {5, 10, 20, 50, 75, 100}, 10);
        frame.add(paginatedTableDecorator.getContentPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}