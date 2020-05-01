package com.georgiana.ojoc.repo.jpa;

import com.georgiana.ojoc.entity.Album;
import com.georgiana.ojoc.repo.AlbumInterfaceRepository;

import java.util.List;

public class AlbumRepository extends AbstractRepository<Album> implements AlbumInterfaceRepository {
    public AlbumRepository() { super(Album.class); }

    @Override
    @SuppressWarnings("unchecked")
    public List<Album> findByName(String name) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Album.findByName")
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Album> findByArtist(int artistID) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Album.findByArtist")
                .setParameter("artistID", artistID)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Album> findByReleaseYear(int releaseYear) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Album.findByReleaseYear")
                .setParameter("releaseYear", releaseYear)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Album> findByGenre(String genre) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Album.findByGenre")
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Album> findAll() {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Album.findAll")
                .getResultList();
    }
}
