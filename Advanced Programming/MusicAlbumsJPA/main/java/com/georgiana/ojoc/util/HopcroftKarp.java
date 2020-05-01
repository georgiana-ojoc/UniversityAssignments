package com.georgiana.ojoc.util;

import com.georgiana.ojoc.entity.Album;
import com.georgiana.ojoc.repo.jpa.AlbumRepository;

import java.util.*;

public class HopcroftKarp {
    private List<Album> albums;
    private Set<Integer> artists;
    private Set<String> genres;
    private Map<Integer, String> artistsMatching;
    private Map<String, Integer> genresMatching;
    private Map<Integer, Integer> artistsDistance;
    private Map<Integer, Set<String>> artistsNeighbors;

    public HopcroftKarp(List<Album> albums) {
        this.albums = albums;
        artists = new HashSet<>();
        genres = new HashSet<>();
        artistsMatching = new HashMap<>();
        genresMatching = new HashMap<>();
        artistsDistance = new HashMap<>();
        artistsNeighbors = new HashMap<>();
        for (Album album : albums) {
            int artistID = album.getArtistID();
            String genre = album.getGenre();
            artists.add(artistID);
            genres.add(genre);
            Set<String> neighborGenres = artistsNeighbors.get(artistID);
            if (neighborGenres == null) {
                neighborGenres = new HashSet<>();
            }
            neighborGenres.add(album.getGenre());
            artistsNeighbors.put(artistID, neighborGenres);
        }
    }

    private boolean BFS() {
        Queue<Integer> artistsQueue = new LinkedList<>();
        for (int artist : artists) {
            if (artistsMatching.get(artist) == null) {
                artistsDistance.put(artist, 0);
                artistsQueue.add(artist);
            }
            else {
                artistsDistance.put(artist, Integer.MAX_VALUE);
            }
        }
        artistsDistance.put(null, Integer.MAX_VALUE);
        while (!artistsQueue.isEmpty()) {
            Integer artist = artistsQueue.poll();
            if (artistsDistance.get(artist) < artistsDistance.get(null)) {
                for (String genre : artistsNeighbors.get(artist)) {
                    Integer matchingArtist = genresMatching.get(genre);
                    if (artistsDistance.get(matchingArtist) == Integer.MAX_VALUE) {
                        artistsDistance.put(matchingArtist, artistsDistance.get(artist) + 1);
                        artistsQueue.add(matchingArtist);
                    }
                }
            }
        }
        return artistsDistance.get(null) != Integer.MAX_VALUE;
    }

    private boolean DFS(Integer artist) {
        if (artist != null) {
            for (String genre : artistsNeighbors.get(artist)) {
                if (artistsDistance.get(genresMatching.get(genre)) == artistsDistance.get(artist) + 1) {
                    if (DFS(genresMatching.get(genre))) {
                        artistsMatching.put(artist, genre);
                        genresMatching.put(genre, artist);
                        return true;
                    }
                }
            }
            artistsDistance.put(artist, Integer.MAX_VALUE);
            return false;
        }
        return true;
    }

    public int getMatchingCardinal() {
        int count = 0;
        while (BFS()) {
            for (Integer artist : artists) {
                if (artistsMatching.get(artist) == null) {
                    if (DFS(artist)) {
                        ++count;
                    }
                }
            }
        }
        for (String genre : genres) {
            if (genresMatching.get(genre) != null) {
                for (Album album : albums) {
                    if (album.getGenre().equals(genre) && album.getArtistID() == genresMatching.get(genre)) {
                        System.out.println(album);
                        break;
                    }
                }
            }
        }
        return count;
    }
}
