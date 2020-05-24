import java.sql.*;

public class DynamicParameter {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                "STUDENT", "STUDENT")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM studenti WHERE id = ?")) {
                preparedStatement.setInt(1, 1);
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
