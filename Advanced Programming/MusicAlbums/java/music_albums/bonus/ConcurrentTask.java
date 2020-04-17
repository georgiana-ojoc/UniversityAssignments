package music_albums.bonus;

import music_albums.controller.ArtistController;
import music_albums.data.Artist;

import java.sql.Connection;
import java.util.List;

public class ConcurrentTask implements Runnable {
    private String name;
    private Connection connection;

    public ConcurrentTask(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    @Override
    public void run() {
        ArtistController artistController = new ArtistController(connection);
        List<Artist> artists = artistController.findAll();
        System.out.println(name + " : selecting " + artists.size() +
                " artists from the table \"artists\" completed.");
    }
}
