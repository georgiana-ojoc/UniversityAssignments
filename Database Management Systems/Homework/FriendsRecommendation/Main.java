import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String name;
        String surname;
        String group;
        if (args.length < 3) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduceti numele: ");
            name = scanner.nextLine();
            System.out.print("Introduceti prenumele: ");
            surname = scanner.nextLine();
            System.out.print("Introduceti grupa: ");
            group = scanner.nextLine();
        }
        else {
            name = args[0];
            surname = args[1];
            group = args[2];
        }
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                    "STUDENT","STUDENT");
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id FROM studenti WHERE TRIM(UPPER(nume)) = TRIM(UPPER(?))" +
                            "AND TRIM(UPPER(prenume)) = TRIM(UPPER(?)) AND TRIM(UPPER(grupa)) = TRIM(UPPER(?))");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, group);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            else {
                throw new Exception("Studentul " + name + ' ' + surname + " din grupa " + group +
                        " nu este in baza de date.");
            }
            preparedStatement = connection.prepareStatement("SELECT recomandari(?) FROM DUAL");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString(1).replaceAll("[{]", "{ ")
                        .replaceAll(":", " : ")
                        .replaceAll(",", ",\n")
                        .replaceAll("}", " }"));
            }
            else {
                throw new Exception("Studentul " + name + ' ' + surname + " din grupa " + group +
                        " nu are recomandari de prieteni in baza de date.");
            }
            connection.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
