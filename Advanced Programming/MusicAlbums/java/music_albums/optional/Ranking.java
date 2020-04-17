package music_albums.optional;

import freemarker.template.*;
import music_albums.Main;
import music_albums.controller.AlbumController;
import music_albums.controller.ArtistController;
import music_albums.controller.ChartController;
import music_albums.data.Album;
import music_albums.data.Artist;
import music_albums.data.Chart;
import music_albums.database.ConnectionSource;
import music_albums.database.SingletonConnectionSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.*;

public class Ranking {
    private Connection connection;
    private List<Artist> artistsRanked;

    public Ranking() {
        ConnectionSource connectionSource = new SingletonConnectionSource();
        connection = connectionSource.getConnection();
        artistsRanked = new ArrayList<>();
    }

    public void computeRanking() {
        ArtistController artistController = new ArtistController(connection);
        artistsRanked = artistController.findAll();
        AlbumController albumController = new AlbumController(connection);
        List<Album> albums = albumController.findAll();
        ChartController chartController = new ChartController(connection);
        List<Chart> charts = chartController.findAll();
        for (Chart chart : charts) {
            Album album = albums.get(chart.getAlbumID() - 1);
            Artist artist = artistsRanked.get(album.getArtistID() - 1);
            int index = artistsRanked.indexOf(artist);
            artist.setScore(artist.getScore() + chart.getRank());
            artistsRanked.set(index, artist);
        }
        Collections.sort(this.artistsRanked);
        int rank = 0;
        for (Artist artist : artistsRanked) {
            int index = artistsRanked.indexOf(artist);
            artist.setRank(++rank);
            artistsRanked.set(index, artist);
        }
    }

    public void createHTMLReport() {
        try (Writer fileWriter = new FileWriter(new File("report.html"))) {
            Configuration configuration = new Configuration();
            configuration.setClassForTemplateLoading(Main.class, "templates");
            configuration.setDefaultEncoding("UTF-8");
            configuration.setLocale(Locale.US);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            Template template = configuration.getTemplate("report.ftl");
            Map<String, Object> input = new HashMap<>();
            input.put("artists", artistsRanked);
            template.process(input, fileWriter);
        }
        catch (IOException | TemplateException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder
                (String.format("\n%-10s%-50s%-50s%-5s\n", "Rank", "Artist", "Country", "Score"));
        for (Artist artist : artistsRanked) {
            result.append(String.format("%-10d%-50s%-50s%-5d\n",
                    artist.getRank(), artist.getName(), artist.getCountry(), artist.getScore()));
        }
        result.append("\n");
        return result.toString();
    }
}
