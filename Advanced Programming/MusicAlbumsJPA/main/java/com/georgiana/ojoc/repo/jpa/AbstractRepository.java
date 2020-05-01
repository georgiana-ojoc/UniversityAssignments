package com.georgiana.ojoc.repo.jpa;

import com.georgiana.ojoc.util.PersistenceUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class AbstractRepository<Type> {
    private Class<Type> type;
    protected EntityManagerFactory entityManagerFactory;

    public AbstractRepository(Class<Type> type) {
        this.type = type;
        entityManagerFactory = PersistenceUtil.getInstance().getEntityManagerFactory();
    }

    public Type create(Type entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return entity;
    }

    public Type findByID(int id) {
        return entityManagerFactory.createEntityManager().find(type, id);
    }
}
