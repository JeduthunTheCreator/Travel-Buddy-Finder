package com.example.myrestservice.service;

import com. google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.example.myrestservice.model.Weather;

public class WeatherAPI {
    private final String path = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private final String apiKey = "M82Q85B6B99D4WY8BZKKMGCV3"; 

    public String getWeatherData(String city, String country) throws IOException {
        String response = "";
        try {
            String requestUrl = path + city + "," + country + "?key=" + apiKey;
            URL url = new URL(requestUrl);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response += line + "\n";
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(WeatherAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public static void main(String args[]) throws IOException {
        WeatherAPI weatherAPI = new WeatherAPI();
        String response = weatherAPI.getWeatherData("London", "UK");
        Gson gson = new Gson();
        Weather weather = gson.fromJson(response, Weather.class);

        // Print out some of the weather information
        System.out.println("Address: " + weather.getResolvedAddress());
        System.out.println("Timezone: " + weather.getTimezone());
        System.out.println("Description: " + weather.getDescription());
        System.out.println("Current Temperature: " + weather.getCurrentConditions().getTemp());
    }
}
