import java.io.*;
import java.sql.*;
import java.time.LocalDate;

public class Main {
    public static long serialize(Connection connection, Object object) {
        long insertedIdentifier = 1;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT MAX(identifier) + 1 FROM objects");
            if (resultSet.next()) {
                insertedIdentifier = resultSet.getInt(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO objects VALUES (?, ?, ?)", new String[]{"identifier"})) {
            preparedStatement.setLong(1, insertedIdentifier);
            preparedStatement.setString(2, object.getClass().getName());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            preparedStatement.setBlob(3, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getLong(1);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public static Object deserialize(Connection connection, long identifier) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT object FROM objects " +
                "WHERE identifier = ?")) {
            preparedStatement.setLong(1, identifier);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    byte[] bytes = resultSet.getBytes(1);
                    if (bytes != null) {
                        return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
                    }
                }
                else {
                    resultSet.close();
                    preparedStatement.close();
                    throw new Exception("No object with identifier " + identifier + " was found in database.");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
                "STUDENT","STUDENT");) {
            long identifier = serialize(connection, new Student("Georgiana", "Ojoc", LocalDate.of(1999, 7, 30), 2));
            Student student = (Student) deserialize(connection, identifier);
            System.out.println(student);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
