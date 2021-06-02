package com.designyourjourney.pictureout.db;

import androidx.room.TypeConverter;

import com.designyourjourney.pictureout.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class CityListDataConverter implements Serializable {
    @TypeConverter
    public String CityListToString(List<City> cities) {
        if (cities==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<City>>() {
        }.getType();
        String json = gson.toJson(cities,type);
        return json;
    }

    @TypeConverter
    public List<City> StringToCityList(String data) {
        if (data==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<City>>() {
        }.getType();
        List<City> cities = gson.fromJson(data,type);
        return cities;
    }
}
