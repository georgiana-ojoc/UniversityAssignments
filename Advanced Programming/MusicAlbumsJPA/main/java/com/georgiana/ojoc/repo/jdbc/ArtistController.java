package com.georgiana.ojoc.repo.jdbc;

import com.georgiana.ojoc.entity.Artist;
import com.georgiana.ojoc.repo.ArtistInterfaceRepository;

import java.sql.*;

public class ArtistController implements ArtistInterfaceRepository {
    private Connection connection;

    public ArtistController(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public Artist create(Artist artist) {
        return create(artist.getName(), artist.getCountry());
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
                artist = new Artist(name, country);
                artist.setID(resultSet.getInt(1));
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
}
