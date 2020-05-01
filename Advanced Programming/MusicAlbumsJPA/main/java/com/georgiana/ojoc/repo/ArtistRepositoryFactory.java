package com.georgiana.ojoc.repo;

import com.georgiana.ojoc.repo.jdbc.ArtistController;
import com.georgiana.ojoc.repo.jdbc.Database;
import com.georgiana.ojoc.repo.jpa.ArtistRepository;

public class ArtistRepositoryFactory implements AbstractFactory<ArtistInterfaceRepository> {
    @Override
    public ArtistInterfaceRepository create(String method) {
        if (method.equalsIgnoreCase("jdbc")) {
            return new ArtistController(Database.getDatabase().getConnection());
        }
        else if (method.equalsIgnoreCase("jpa")) {
            return new ArtistRepository();
        }
        return null;
    }
}
