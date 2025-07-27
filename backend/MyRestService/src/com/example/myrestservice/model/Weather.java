package com.example.myrestservice.model;

/**
 *
 * @author idemu
 */
public class Weather {
    private long queryCost;
    private double latitude;
    private double longitude;
    private String resolvedAddress;
    private String address;
    private String timezone;
    public enum Source {};
    private double tzoffset;
    private String description;
    private CurrentConditions[] days;
    private Object[] alerts;
    private CurrentConditions currentConditions;

    public long getQueryCost() {
        return queryCost;
    }

    public void setQueryCost(long value) {
        this.queryCost = value;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double value) {
        this.latitude = value;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double value) {
        this.longitude = value;
    }

    public String getResolvedAddress() {
        return resolvedAddress;
    }

    public void setResolvedAddress(String value) {
        this.resolvedAddress = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String value) {
        this.timezone = value;
    }

    public double getTzoffset() {
        return tzoffset;
    }

    public void setTzoffset(double value) {
        this.tzoffset = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public CurrentConditions[] getDays() {
        return days;
    }

    public void setDays(CurrentConditions[] value) {
        this.days = value;
    }

    public Object[] getAlerts() {
        return alerts;
    }

    public void setAlerts(Object[] value) {
        this.alerts = value;
    }


    public CurrentConditions getCurrentConditions() {
        return currentConditions;
    }

    public void setCurrentConditions(CurrentConditions value) {
        this.currentConditions = value;
    }
}

