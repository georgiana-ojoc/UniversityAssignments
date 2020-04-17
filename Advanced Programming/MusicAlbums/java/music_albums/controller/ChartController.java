package music_albums.controller;

import music_albums.data.Chart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChartController extends Controller {
    public ChartController(Connection connection) {
        super(connection);
    }

    public void create(int chartID, int albumID, int rank) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT IGNORE INTO charts (id_chart, id_album, place) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, chartID);
            preparedStatement.setInt(2, albumID);
            preparedStatement.setInt(3, rank);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (!resultSet.next()) {
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
    }

    private void updateAutoIncrement() {
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        int value = 1;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(id) + 1 FROM charts");
            if (resultSet.next()) {
                value = resultSet.getInt(1);
            }
            preparedStatement = connection.prepareStatement("ALTER TABLE charts AUTO_INCREMENT = ?");
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

    public Chart findByAlbum(int albumID) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Chart chart = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM charts WHERE id_album = ?");
            preparedStatement.setInt(1, albumID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                chart = new Chart(resultSet.getInt("id"), resultSet.getInt("id_chart"),
                        resultSet.getInt("id_album"), resultSet.getInt("place"));
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
        return chart;
    }

    public List<Chart> findAll() {
        List<Chart> charts = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM charts ORDER BY id_album")) {
            if (resultSet.next()) {
                charts = new ArrayList<>();
                charts.add(new Chart(resultSet.getInt("id"), resultSet.getInt("id_chart"),
                        resultSet.getInt("id_album"), resultSet.getInt("place")));
                while (resultSet.next()) {
                    charts.add(new Chart(resultSet.getInt("id"), resultSet.getInt("id_chart"),
                            resultSet.getInt("id_album"), resultSet.getInt("place")));
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
        return charts;
    }

    public void deleteAll() {
        try(Statement delete = connection.createStatement();
            Statement alter = connection.createStatement()) {
            delete.executeUpdate("DELETE FROM charts WHERE 1 = 1");
            alter.executeUpdate("ALTER TABLE charts AUTO_INCREMENT = 1");
        } catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
    }
}
