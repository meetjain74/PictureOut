package com.designyourjourney.pictureout.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "plans_table")
public class Plan {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "planId")
    private int planId;

    @ColumnInfo(name = "name")
    private String planName;

    @ColumnInfo(name = "start")
    private String startDate;

    @ColumnInfo(name = "end")
    private String endDate;

    @TypeConverters(CityDataConverter.class)
    @ColumnInfo(name = "startCity")
    private City startCity;

    @TypeConverters(CityListDataConverter.class)
    @ColumnInfo(name = "destinations")
    private List<City> destinations;

    @TypeConverters(TravelSpotListDataConverter.class)
    @ColumnInfo(name = "travelSpots")
    private List<TravelSpot> travelSpots;

    public Plan() {
    }

    public Plan(int planId, String planName, String startDate, String endDate,
                City startCity, List<City> destinations, List<TravelSpot> travelSpots) {
        this.planId = planId;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startCity = startCity;
        this.destinations = destinations;
        this.travelSpots = travelSpots;
    }
    
    public Plan(Plan plan) {
        this.planId=0; // Does not copy the plan id (unique for everyone)
        this.planName=plan.getPlanName();
        this.startDate=plan.getStartDate();
        this.endDate=plan.getEndDate();
        this.startCity=plan.getStartCity();
        this.destinations=plan.getDestinations();
        this.travelSpots=plan.getTravelSpots();
    }

    public int getPlanId() {
        return planId;
    }

    public String getPlanName() {
        return planName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public City getStartCity() {
        return startCity;
    }

    public List<City> getDestinations() {
        return destinations;
    }

    public List<TravelSpot> getTravelSpots() {
        return travelSpots;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStartCity(City startCity) {
        this.startCity = startCity;
    }

    public void setDestinations(List<City> destinations) {
        this.destinations = destinations;
    }

    public void setTravelSpots(List<TravelSpot> travelSpots) {
        this.travelSpots = travelSpots;
    }
}
