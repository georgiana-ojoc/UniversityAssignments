package com.georgiana.ojoc.repo.jpa;

import com.georgiana.ojoc.entity.Chart;
import com.georgiana.ojoc.repo.ChartInterfaceRepository;

import java.util.List;

public class ChartRepository extends AbstractRepository<Chart> implements ChartInterfaceRepository {
    public ChartRepository() { super(Chart.class); }

    @Override
    @SuppressWarnings("unchecked")
    public List<Chart> findByAlbum(int albumID) {
        return entityManagerFactory.createEntityManager()
                .createNamedQuery("Chart.findByAlbum")
                .setParameter("albumID", albumID)
                .getResultList();
    }
}
