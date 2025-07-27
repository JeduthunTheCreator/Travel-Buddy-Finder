package com.example.myrestservice.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author idemu
 */

import java.io.IOException;

public enum Conditions {
    CLEAR, OVERCAST, PARTIALLY_CLOUDY;

    public String toValue() {
        switch (this) {
            case CLEAR: return "Clear";
            case OVERCAST: return "Overcast";
            case PARTIALLY_CLOUDY: return "Partially cloudy";
        }
        return null;
    }

    public static Conditions forValue(String value) throws IOException {
        if (value.equals("Clear")) return CLEAR;
        if (value.equals("Overcast")) return OVERCAST;
        if (value.equals("Partially cloudy")) return PARTIALLY_CLOUDY;
        throw new IOException("Cannot deserialize Conditions");
    }
}