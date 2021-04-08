package com.shop;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDAO {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * FROM food WHERE identifier IN (:foodIdentifiers)")
    List<Food> getAllByIdentifiers(Integer[] foodIdentifiers);

    @Query("SELECT * FROM food WHERE name LIKE :name LIMIT 1")
    Food findByName(String name);

    @Insert
    void insertAll(List<Food> foods);

    @Delete
    void delete(Food food);

    @Query("DELETE FROM food")
    void deleteAll();
}
