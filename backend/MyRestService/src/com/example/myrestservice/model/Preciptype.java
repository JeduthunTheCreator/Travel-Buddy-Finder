package com.example.myrestservice.model;

/**
 *
 * @author idemu
 */
import java.io.IOException;

public enum Preciptype {
    RAIN, SNOW;

    public String toValue() {
        switch (this) {
            case RAIN: return "rain";
            case SNOW: return "snow";
        }
        return null;
    }

    public static Preciptype forValue(String value) throws IOException {
        if (value.equals("rain")) return RAIN;
        if (value.equals("snow")) return SNOW;
        throw new IOException("Cannot deserialize Preciptype");
    }
}