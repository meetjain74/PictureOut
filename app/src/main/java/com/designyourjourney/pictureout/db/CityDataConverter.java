package com.designyourjourney.pictureout.db;

import androidx.room.TypeConverter;

import com.designyourjourney.pictureout.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CityDataConverter {
    @TypeConverter
    public String CityToString(City city) {
        if (city==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<City>() {
        }.getType();
        String json = gson.toJson(city,type);
        return json;
    }

    @TypeConverter
    public City StringToCity(String data) {
        if (data==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<City>() {
        }.getType();
        City city = gson.fromJson(data,type);
        return city;
    }
}
