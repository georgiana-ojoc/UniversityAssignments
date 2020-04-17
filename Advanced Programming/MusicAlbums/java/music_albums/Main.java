package music_albums;

import music_albums.bonus.ConcurrentTaskExecutor;
import music_albums.controller.AlbumController;
import music_albums.controller.ArtistController;
import music_albums.controller.ChartController;
import music_albums.data.Album;
import music_albums.data.Artist;
import music_albums.data.Chart;
import music_albums.database.ConnectionPoolConnectionSource;
import music_albums.database.ConnectionSource;
import music_albums.database.SingletonConnectionSource;
import music_albums.optional.Generator;
import music_albums.optional.Ranking;

import java.sql.Connection;

public class Main {
    private static void compulsory() {
        ConnectionSource connectionSource = new SingletonConnectionSource();
        Connection connection = connectionSource.getConnection();
        ArtistController artistController = new ArtistController(connection);
        artistController.create("Depeche Mode", "Italy");
        artistController.create("Depeche Mode", "Italy");
        Artist artist = artistController.findByName("Depeche Mode");
        if (artist != null) {
            System.out.println(artist);
            AlbumController albumController = new AlbumController(connection);
            albumController.create("Karmacode", artist.getID(), 2006);
            Album album = albumController.findByArtist(artist.getID());
            if (album != null) {
                System.out.println(album);
                ChartController chartController = new ChartController(connection);
                chartController.create(1, album.getID(), 1);
                Chart chart = chartController.findByAlbum(album.getID());
                if (chart != null) {
                    System.out.println(chart);
                    chartController.deleteAll();
                    albumController.deleteAll();
                    artistController.deleteAll();
                }
            }
        }
    }

    private static void optional() {
        Generator generator = new Generator
                (20, 100, 10, 5, 10);
        generator.generate();
        Ranking ranking = new Ranking();
        ranking.computeRanking();
        System.out.println(ranking);
        ranking.createHTMLReport();
    }

    private static void bonus() {
        executor(new ConnectionPoolConnectionSource());
    }

    private static void executor(ConnectionSource connectionSource) {
        ConcurrentTaskExecutor singletonExecutor = new ConcurrentTaskExecutor(connectionSource);
        singletonExecutor.executeTasks(5000);
    }

    public static void main(String[] args) {
        compulsory();
        optional();
        bonus();
    }
}
