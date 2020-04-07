import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                    "STUDENT","STUDENT");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cursuri");
            while (resultSet.next()) {
                System.out.println("Cursul \"" + resultSet.getString("titlu_curs") + "\" are "
                        + resultSet.getInt("credite") + " credite.");
            }
            connection.close();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
