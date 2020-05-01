package com.georgiana.ojoc.repo.jdbc;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements Closeable {
    private static Database database = null;
    private Connection connection;

    private Database() {
        try {
            String url = "jdbc:mysql://localhost:3306/music_albums?serverTimezone=UTC";
            String username = "dba";
            String password = "sql";
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException exception) {
            System.out.println(exception.getErrorCode() + " : " + exception.getMessage());
        }
    }

    public synchronized static Database getDatabase() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (database != null) {
                connection.close();
            }
        }
        catch (SQLException ignored) {
        }
    }
}
