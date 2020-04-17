package music_albums.database;

import java.sql.Connection;

public class SingletonConnectionSource implements ConnectionSource {
    @Override
    public Connection getConnection() {
        return Database.getDatabase().getConnection();
    }

    @Override
    public String getType() {
        return "Singleton";
    }
}
