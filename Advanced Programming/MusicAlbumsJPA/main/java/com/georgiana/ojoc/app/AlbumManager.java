package com.georgiana.ojoc.app;

import com.georgiana.ojoc.entity.Album;
import com.georgiana.ojoc.entity.Artist;
import com.georgiana.ojoc.entity.Chart;
import com.georgiana.ojoc.repo.ArtistInterfaceRepository;
import com.georgiana.ojoc.repo.ArtistRepositoryFactory;
import com.georgiana.ojoc.repo.jpa.AlbumRepository;
import com.georgiana.ojoc.repo.jpa.ArtistRepository;
import com.georgiana.ojoc.repo.jpa.ChartRepository;
import com.georgiana.ojoc.util.Generator;
import com.georgiana.ojoc.util.HopcroftKarp;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class AlbumManager {
    private void printInsertMessage(String name, String table) {
        System.out.println("Inserted \"" + name + "\" into \"" + table + "\".");
    }

    private void tables() {
        ArtistRepository artistRepository = new ArtistRepository();
        AlbumRepository albumRepository = new AlbumRepository();
        ChartRepository chartRepository = new ChartRepository();
        Artist georgeEzra = artistRepository.create(new Artist("George Ezra", "UK"));
        printInsertMessage(georgeEzra.getName(), "artists");
        Album wantedOnVoyage = albumRepository.create(new Album("Wanted on Voyage", georgeEzra.getID(), 2014, "pop-folk"));
        printInsertMessage(wantedOnVoyage.getName(), "albums");
        Album stayingAtTamara = albumRepository.create(new Album("Staying at Tamara's", georgeEzra.getID(), 2018, "pop-rock"));
        printInsertMessage(stayingAtTamara.getName(), "albums");
        Artist imagineDragons = artistRepository.create(new Artist("Imagine Dragons", "USA"));
        printInsertMessage(imagineDragons.getName(), "artists");
        Album nightVisions = albumRepository.create(new Album("Night Visions", imagineDragons.getID(), 2012, "electronic-rock"));
        printInsertMessage(nightVisions.getName(), "albums");
        Album evolve = albumRepository.create(new Album("Evolve", imagineDragons.getID(), 2017, "indie-rock"));
        printInsertMessage(evolve.getName(), "albums");
        Album origins = albumRepository.create(new Album("Origins", imagineDragons.getID(), 2018, "pop-rock"));
        printInsertMessage(origins.getName(), "albums");
        chartRepository.create(new Chart(1, nightVisions.getID(), 1));
        printInsertMessage(imagineDragons.getName(), "charts");
        chartRepository.create(new Chart(1, wantedOnVoyage.getID(), 2));
        printInsertMessage(wantedOnVoyage.getName(), "charts");
        chartRepository.create(new Chart(2, stayingAtTamara.getID(), 1));
        chartRepository.create(new Chart(2, origins.getID(), 2));
        chartRepository.create(new Chart(2, wantedOnVoyage.getID(), 3));
        System.out.println("Find George Ezra by identifier:");
        System.out.println(artistRepository.findByID(georgeEzra.getID()));
        System.out.println("Find albums by Imagine Dragons:");
        List<Album> albums = albumRepository.findByArtist(imagineDragons.getID());
        for (Album album : albums) {
            System.out.println(album);
        }
        System.out.println("Find rankings of \"Wanted on Voyage\":");
        List<Chart> charts = chartRepository.findByAlbum(wantedOnVoyage.getID());
        for (Chart chart : charts) {
            System.out.println(chart);
        }
        System.out.println("Find pop-rock albums:");
        albums = albumRepository.findByGenre("pop-rock");
        for (Album album : albums) {
            System.out.println(album);
        }
    }

    private void optional() {
        String method = "jdbc";
        JSONParser parser = new JSONParser();
        try {
            Object file = parser.parse(new FileReader("configuration.json"));
            JSONObject configurations = (JSONObject) file;
            method = (String) configurations.get("method");
        }
        catch (IOException | ParseException ignored) { }
        System.out.println("The used method is " + method + '.');
        ArtistRepositoryFactory artistRepositoryFactory = new ArtistRepositoryFactory();
        ArtistInterfaceRepository artistRepository = artistRepositoryFactory.create(method);
        Artist oneRepublic = artistRepository.create(new Artist("One Republic", "USA"));
        printInsertMessage(oneRepublic.getName(), "artists");
    }

    private void bonus() {
        Generator generator = new Generator
                (20, 100, 10, 5, 10);
        generator.generate();
        HopcroftKarp hopcroftKarp = new HopcroftKarp(new AlbumRepository().findAll());
        System.out.println("The maximum cardinality stable matching contains " + hopcroftKarp.getMatchingCardinal() +
                " albums.");
    }

    public static void main(String[] args) {
        AlbumManager albumManager = new AlbumManager();
        albumManager.tables();
        albumManager.optional();
        albumManager.bonus();
    }
}
