package com.example.myrestservice.FileStorage;

public class TripDetail {
    private String userId;
    private String tripId;
    private String location;
    private String datetime;
    private String weather;
    private String hotel;

    public TripDetail(String userId, String tripId, String location, String datetime, String weather, String hotel) {
        this.userId = userId;
        this.tripId = tripId;
        this.location = location;
        this.datetime = datetime;
        this.weather = weather;
        this.hotel = hotel;
    }

}
