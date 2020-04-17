package music_albums.database;

import java.sql.Connection;

public interface ConnectionSource {
    public Connection getConnection();

    public String getType();
}
