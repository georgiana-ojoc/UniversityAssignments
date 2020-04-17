package music_albums.optional;

import com.github.javafaker.Faker;
import music_albums.controller.AlbumController;
import music_albums.controller.ArtistController;
import music_albums.controller.ChartController;
import music_albums.data.Album;
import music_albums.data.Artist;
import music_albums.database.ConnectionSource;
import music_albums.database.SingletonConnectionSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    private Connection connection;
    private Faker faker;
    private int artistNumber;
    private int albumNumber;
    private int chartNumber;
    private int minimumChartSize;
    private int maximumChartSize;
    private List<Artist> artists;
    private List<Album> albums;

    public Generator(int artistNumber, int albumNumber, int chartNumber, int minimumChartSize, int maximumChartSize) {
        ConnectionSource connectionSource = new SingletonConnectionSource();
        connection = connectionSource.getConnection();
        faker = new Faker();
        this.artistNumber = artistNumber;
        this.albumNumber = albumNumber;
        this.chartNumber = chartNumber;
        this.minimumChartSize = minimumChartSize;
        this.maximumChartSize = maximumChartSize;
        artists = new ArrayList<>();
        albums = new ArrayList<>();
    }

    public void generate() {
        generateArtists();
        generateAlbums();
        generateCharts();
    }

    private void generateArtists() {
        ArtistController artistController = new ArtistController(connection);
        for (int index = 0; index < artistNumber; ++index) {
            String name = faker.rockBand().name();
            String country = null;
            do {
                country = faker.country().name();
            } while (country.length() > 30);
            artists.add(artistController.create(name, country));
        }
        System.out.println("Generated " + artistNumber + " artists.");
    }

    private void generateAlbums() {
        AlbumController albumController = new AlbumController(connection);
        for (int index = 0; index < albumNumber; ++index) {
            String name = faker.book().title();
            int artistID = artists.get(faker.random().nextInt(artists.size())).getID();
            int releaseYear = faker.random().nextInt(1975, 2020);
            albums.add(albumController.create(name, artistID, releaseYear));
        }
        System.out.println("Generated " + albumNumber + " albums.");
    }

    private void generateCharts() {
        ChartController chartController = new ChartController(connection);
        for (int chartID = 1; chartID <= chartNumber; ++chartID) {
            int chartSize = faker.random().nextInt(minimumChartSize, maximumChartSize + 1);
            if (chartSize > albums.size()) {
                chartSize = albums.size();
            }
            List<Integer> albumIDs = IntStream.rangeClosed(1, albums.size()).boxed().collect(Collectors.toList());
            Collections.shuffle(albumIDs);
            List<Integer> ranks = IntStream.rangeClosed(1, chartSize).boxed().collect(Collectors.toList());
            Collections.shuffle(ranks);
            for (int index = 0; index < chartSize; ++index) {
                int albumID = albumIDs.get(index);
                int rank = ranks.get(index);
                chartController.create(chartID, albumID, rank);
            }
        }
        System.out.println("Generated " + chartNumber + " charts.");
    }
}
