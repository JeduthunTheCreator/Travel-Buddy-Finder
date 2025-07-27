package com.example.myrestservice.model;
import java.io.IOException;

public enum Source {
    COMB, FCST, OBS;

    public String toValue() {
        switch (this) {
            case COMB: return "comb";
            case FCST: return "fcst";
            case OBS: return "obs";
        }
        return null;
    }

    public static Source forValue(String value) throws IOException {
        if (value.equals("comb")) return COMB;
        if (value.equals("fcst")) return FCST;
        if (value.equals("obs")) return OBS;
        throw new IOException("Cannot deserialize Weather.Source");
    }
}
