import java.sql.*;

public class SQLInjection {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                "STUDENT", "STUDENT")) {
            String identifier = "1 OR 1 = 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM studenti WHERE id = " + identifier)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString(3) + ' ' + resultSet.getString(4));
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
    }
}
