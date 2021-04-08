package com.shop;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Food.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract FoodDAO foodDAO();
}
