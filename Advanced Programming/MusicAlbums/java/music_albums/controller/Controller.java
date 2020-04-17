package music_albums.controller;

import java.sql.Connection;

public abstract class Controller {
    protected Connection connection;

    public Controller(Connection connection) {
        this.connection = connection;
    }
}
