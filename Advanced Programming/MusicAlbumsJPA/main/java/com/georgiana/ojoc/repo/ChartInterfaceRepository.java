package com.georgiana.ojoc.repo;

import com.georgiana.ojoc.entity.Chart;

import java.util.List;

public interface ChartInterfaceRepository {
    Chart create(Chart chart);

    List<Chart> findByAlbum(int albumID);
}
