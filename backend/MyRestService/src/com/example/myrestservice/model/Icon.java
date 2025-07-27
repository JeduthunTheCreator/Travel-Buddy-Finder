
/**
 *
 * @author idemu
 */
package com.example.myrestservice.model;
import java.io.IOException;

public enum Icon {
    CLEAR_DAY, CLEAR_NIGHT, CLOUDY, FOG, PARTLY_CLOUDY_DAY, PARTLY_CLOUDY_NIGHT, SNOW;

    public String toValue() {
        switch (this) {
            case CLEAR_DAY: return "clear-day";
            case CLEAR_NIGHT: return "clear-night";
            case CLOUDY: return "cloudy";
            case FOG: return "fog";
            case PARTLY_CLOUDY_DAY: return "partly-cloudy-day";
            case PARTLY_CLOUDY_NIGHT: return "partly-cloudy-night";
            case SNOW: return "snow";
        }
        return null;
    }

    public static Icon forValue(String value) throws IOException {
        if (value.equals("clear-day")) return CLEAR_DAY;
        if (value.equals("clear-night")) return CLEAR_NIGHT;
        if (value.equals("cloudy")) return CLOUDY;
        if (value.equals("fog")) return FOG;
        if (value.equals("partly-cloudy-day")) return PARTLY_CLOUDY_DAY;
        if (value.equals("partly-cloudy-night")) return PARTLY_CLOUDY_NIGHT;
        if (value.equals("snow")) return SNOW;
        throw new IOException("Cannot deserialize Icon");
    }
}