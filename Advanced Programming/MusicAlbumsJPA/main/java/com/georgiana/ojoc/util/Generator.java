package com.georgiana.ojoc.util;

import com.georgiana.ojoc.entity.Album;
import com.georgiana.ojoc.entity.Artist;
import com.georgiana.ojoc.entity.Chart;
import com.georgiana.ojoc.repo.jpa.AlbumRepository;
import com.georgiana.ojoc.repo.jpa.ArtistRepository;
import com.georgiana.ojoc.repo.jpa.ChartRepository;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    private ArtistRepository artistRepository;
    private AlbumRepository albumRepository;
    private ChartRepository chartRepository;
    private List<Artist> artists;
    private List<Album> albums;
    private Faker faker;
    private int artistNumber;
    private int albumNumber;
    private int chartNumber;
    private int minimumChartSize;
    private int maximumChartSize;

    public Generator(int artistNumber, int albumNumber, int chartNumber, int minimumChartSize, int maximumChartSize) {
        artistRepository = new ArtistRepository();
        albumRepository = new AlbumRepository();
        chartRepository = new ChartRepository();
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        faker = new Faker();
        this.artistNumber = artistNumber;
        this.albumNumber = albumNumber;
        this.chartNumber = chartNumber;
        this.minimumChartSize = minimumChartSize;
        this.maximumChartSize = maximumChartSize;
    }

    public void generate() {
        generateArtists();
        generateAlbums();
        generateCharts();
    }

    private void generateArtists() {
        for (int index = 0; index < artistNumber; ++index) {
            String name = faker.rockBand().name();
            String country;
            do {
                country = faker.country().name();
            } while (country.length() > 30);
            artists.add(artistRepository.create(new Artist(name, country)));
        }
        System.out.println("Generated " + artistNumber + " artists.");
    }

    private void generateAlbums() {
        for (int index = 0; index < albumNumber; ++index) {
            String name;
            int artistID;
            boolean found;
            do {
                name = faker.book().title();
                artistID = artists.get(faker.random().nextInt(artists.size())).getID();
                List<Album> existingAlbums = albumRepository.findByArtist(artistID);
                if (existingAlbums == null) {
                    break;
                }
                found = false;
                for (Album album : existingAlbums) {
                    if (name.equalsIgnoreCase(album.getName())) {
                        found = true;
                        break;
                    }
                }
            } while (found);
            int releaseYear = faker.random().nextInt(1975, 2020);
            String genre = faker.music().genre();
            albums.add(albumRepository.create(new Album(name, artistID, releaseYear, genre)));
        }
        System.out.println("Generated " + albumNumber + " albums.");
    }

    private void generateCharts() {
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
                chartRepository.create(new Chart(chartID, albumID, rank));
            }
        }
        System.out.println("Generated " + chartNumber + " charts.");
    }
}
