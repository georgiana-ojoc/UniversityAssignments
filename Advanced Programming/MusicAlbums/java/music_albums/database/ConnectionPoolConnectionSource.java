package music_albums.database;

import java.sql.Connection;

public class ConnectionPoolConnectionSource implements ConnectionSource {
    @Override
    public Connection getConnection() {
        return DataSource.getConnection();
    }

    @Override
    public String getType() {
        return "Connection Pool";
    }
}
