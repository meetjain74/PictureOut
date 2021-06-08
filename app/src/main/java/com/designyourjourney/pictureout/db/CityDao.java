package com.designyourjourney.pictureout.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM cities_table")
    List<City> getAllCities();

    @Insert
    void insertCity(City city);
}
