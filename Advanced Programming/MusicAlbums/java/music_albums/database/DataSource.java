package music_albums.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig configuration = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/music_albums?serverTimezone=UTC");
        configuration.setUsername("dba");
        configuration.setPassword("sql");
        configuration.addDataSourceProperty("cachePrepStmts", "true");
        configuration.addDataSourceProperty("prepStmtCacheSize", "250");
        configuration.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(configuration);
    }

    private DataSource() {
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        }
        catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
            return null;
        }
    }
}
