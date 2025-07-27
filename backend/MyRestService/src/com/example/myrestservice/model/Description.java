package com.example.myrestservice.model;
import java.io.IOException;

public enum Description {
    CLEARING_IN_THE_AFTERNOON, CLOUDY_SKIES_THROUGHOUT_THE_DAY, PARTLY_CLOUDY_THROUGHOUT_THE_DAY;

    public String toValue() {
        switch (this) {
            case CLEARING_IN_THE_AFTERNOON: return "Clearing in the afternoon.";
            case CLOUDY_SKIES_THROUGHOUT_THE_DAY: return "Cloudy skies throughout the day.";
            case PARTLY_CLOUDY_THROUGHOUT_THE_DAY: return "Partly cloudy throughout the day.";
        }
        return null;
    }

    public static Description forValue(String value) throws IOException {
        if (value.equals("Clearing in the afternoon.")) return CLEARING_IN_THE_AFTERNOON;
        if (value.equals("Cloudy skies throughout the day.")) return CLOUDY_SKIES_THROUGHOUT_THE_DAY;
        if (value.equals("Partly cloudy throughout the day.")) return PARTLY_CLOUDY_THROUGHOUT_THE_DAY;
        throw new IOException("Cannot deserialize Description");
    }
}