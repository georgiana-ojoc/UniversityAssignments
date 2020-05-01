package com.georgiana.ojoc.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Closeable;

public class PersistenceUtil implements Closeable {
    private static PersistenceUtil instance = null;
    private EntityManagerFactory entityManagerFactory = null;

    private PersistenceUtil() {
        entityManagerFactory = Persistence.createEntityManagerFactory("MusicAlbumsPU");
    }

    public static PersistenceUtil getInstance() {
        if (instance == null) {
            instance = new PersistenceUtil();
        }
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() { return entityManagerFactory; }

    @Override
    public void close() {
        if (instance != null) {
            entityManagerFactory.close();
        }
    }
}
