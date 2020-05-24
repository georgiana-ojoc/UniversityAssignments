import java.sql.*;

public class Exception {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                "STUDENT", "STUDENT")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT recomandari(?) FROM DUAL")) {
                preparedStatement.setInt(1, -1);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println(resultSet.getString(1).replaceAll("[{]", "{ ")
                                .replaceAll(":", " : ")
                                .replaceAll(",", ",\n")
                                .replaceAll("}", " }"));
                    }
                } catch (SQLException exception) {
                    System.out.println(exception.getMessage());
                }
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
