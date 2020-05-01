package com.georgiana.ojoc.repo.jpa;

import com.georgiana.ojoc.entity.Artist;
import com.georgiana.ojoc.repo.ArtistInterfaceRepository;

import java.util.List;

public class ArtistRepository extends AbstractRepository<Artist> implements ArtistInterfaceRepository {
    public ArtistRepository() {
        super(Artist.class);
    }

    @SuppressWarnings("unchecked")
    public List<Artist> findByName(String name) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Artist.findByName")
                .setParameter("name", name)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Artist> findByCountry(String country) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Artist.findByCountry")
                .setParameter("country", country)
                .getResultList();
    }
}
