package com.designyourjourney.pictureout.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class TravelSpotListDataConverter implements Serializable {
    @TypeConverter
    public String TravelSpotListToString(List<TravelSpot> travelSpots) {
        if (travelSpots==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<City>>() {
        }.getType();
        String json = gson.toJson(travelSpots,type);
        return json;
    }

    @TypeConverter
    public List<TravelSpot> StringToCityList(String data) {
        if (data==null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<TravelSpot>>() {
        }.getType();
        List<TravelSpot> travelSpots = gson.fromJson(data,type);
        return travelSpots;
    }
}
