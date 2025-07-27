package com.example.myrestservice.service;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelAPI {

    private final String path = "http://engine.hotellook.com/api/v2/lookup.json";

    public String getHotelData(String city) throws IOException {
        String response;

        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            String request = "?query=" + encodedCity + "&lang=en&lookFor=both&limit=10";

            URL url = new URL(path + request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestMethod("GET");

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line).append("\r\n");
                }
            }
            response = responseBuilder.toString();
        } catch (IOException ex) {
            Logger.getLogger(HotelAPI.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; // Rethrow the exception to indicate failure
        }

        return response;
    }

    public static void main(String args[]) throws IOException {
        HotelAPI hotelAPI = new HotelAPI();
        String response = hotelAPI.getHotelData("London");

        System.out.println(response);
        JSONObject jsonObject = new JSONObject(response);
        JSONObject results = jsonObject.getJSONObject("results");
        JSONArray hotels = results.getJSONArray("hotels");

        for (int i = 0; i < hotels.length(); i++) {
            JSONObject hotel = hotels.getJSONObject(i);

            // Retrieve other hotel details
            String hotelName = hotel.getString("fullName");
            JSONObject location = hotel.getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lon");

            System.out.println("Hotel Name: " + hotelName + ", Latitude: " + latitude + ", Longitude: " + longitude);
        }
    }
}
