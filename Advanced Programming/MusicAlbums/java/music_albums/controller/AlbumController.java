package music_albums.controller;

import music_albums.data.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumController extends Controller {
    public AlbumController(Connection connection) {
        super(connection);
    }

    public Album create(String name, int artistID, int releaseYear) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Album album = null;
        try {
            String query = "INSERT IGNORE INTO albums (name, id_artist, release_year) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, artistID);
            preparedStatement.setInt(3, releaseYear);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                album = new Album(resultSet.getInt(1), name, artistID, releaseYear);
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
        return album;
    }

    private void updateAutoIncrement() {
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        int value = 1;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(id) + 1 FROM albums");
            if (resultSet.next()) {
                value = resultSet.getInt(1);
            }
            preparedStatement = connection.prepareStatement("ALTER TABLE albums AUTO_INCREMENT = ?");
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

    public Album findByArtist(int artistID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Album album = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM albums WHERE id_artist = ?");
            preparedStatement.setInt(1, artistID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                album = new Album(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("id_artist"), resultSet.getInt("release_year"));
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
        return album;
    }

    public List<Album> findAll() {
        List<Album> albums = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM albums ORDER BY id_artist")) {
            if (resultSet.next()) {
                albums = new ArrayList<>();
                albums.add(new Album(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("id_artist"), resultSet.getInt("release_year")));
                while (resultSet.next()) {
                    albums.add(new Album(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getInt("id_artist"), resultSet.getInt("release_year")));
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        return albums;
    }

    public void deleteAll() {
        try(Statement delete = connection.createStatement();
            Statement alter = connection.createStatement()) {
            delete.executeUpdate("DELETE FROM albums WHERE 1 = 1");
            alter.executeUpdate("ALTER TABLE albums AUTO_INCREMENT = 1");
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
    }
}
