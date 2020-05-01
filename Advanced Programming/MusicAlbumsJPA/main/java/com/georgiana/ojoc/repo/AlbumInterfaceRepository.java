package com.georgiana.ojoc.repo;

import com.georgiana.ojoc.entity.Album;

import java.util.List;

public interface AlbumInterfaceRepository {
    Album create(Album album);

    List<Album> findByName(String name);

    List<Album> findByArtist(int artistID);

    List<Album> findByReleaseYear(int releaseYear);

    List<Album> findByGenre(String genre);

    List<Album> findAll();
}
