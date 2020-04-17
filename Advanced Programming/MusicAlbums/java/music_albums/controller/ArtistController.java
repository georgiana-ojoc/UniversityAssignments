package music_albums.controller;

import music_albums.data.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistController extends Controller {
    public ArtistController(Connection connection) {
        super(connection);
    }

    public Artist create(String name, String country) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Artist artist = null;
        try {
            String query = "INSERT IGNORE INTO artists (name, country) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, country);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                artist = new Artist(resultSet.getInt(1), name, country);
            }
            else {
                updateAutoIncrement();
            }
        }
        catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            }
            catch (SQLException ignored) {
            }
        }
        return artist;
    }

    private void updateAutoIncrement() {
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        int value = 1;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(id) + 1 FROM artists");
            if (resultSet.next()) {
                value = resultSet.getInt(1);
            }
            preparedStatement = connection.prepareStatement("ALTER TABLE artists AUTO_INCREMENT = ?");
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
            catch (SQLException ignored) {
            }
        }
    }

    public Artist findByName(String name) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Artist artist = null;
        try {
            preparedStatement = connection.prepareStatement
                    ("SELECT id, name, country FROM artists WHERE UPPER(name) = UPPER(?)");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                artist = new Artist(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("country"));
            }
        }
        catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            }
            catch (SQLException ignored) {
            }
        }
        return artist;
    }

    public List<Artist> findAll() {
        List<Artist> artists = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM artists ORDER BY id")) {
            if (resultSet.next()) {
                artists = new ArrayList<>();
                artists.add(new Artist(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("country")));
                while (resultSet.next()) {
                    artists.add(new Artist(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("country")));
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        return artists;
    }

    public void deleteAll() {
        try(Statement delete = connection.createStatement();
            Statement alter = connection.createStatement()) {
            delete.executeUpdate("DELETE FROM artists WHERE 1 = 1");
            alter.executeUpdate("ALTER TABLE artists AUTO_INCREMENT = 1");
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
    }
}
