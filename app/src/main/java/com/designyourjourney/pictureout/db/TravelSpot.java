package com.designyourjourney.pictureout.db;

public class TravelSpot {
    private String cityName; // Name of the city in which it is located

    private String name; // Travel spot name (ex - monument name, temple name ,etc)

    private String start; // Travel starts here at this time and date

    private String end; // Travel ends here at this date and time

    private String imageURL; // URL for the image of this place

    private double latitude; // Latitude details of this place

    private double longitude; // Longitude details of this place

    private double rating; // Rating of this place (out of 5)

    private String description; // Description about this place

    TravelSpot() {}

    public TravelSpot(String cityName, String name, String start, String end, String imageURL,
                      double latitude, double longitude, double rating, String description) {
        this.cityName = cityName;
        this.name = name;
        this.start = start;
        this.end = end;
        this.imageURL = imageURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return "TravelSpot{" +
                "cityName='" + cityName + '\'' +
                ", name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                '}';
    }
}
