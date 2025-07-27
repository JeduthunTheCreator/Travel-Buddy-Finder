package com.example.myrestservice.controller;

import com.example.myrestservice.service.HotelAPI;
import com.example.myrestservice.service.RandomNumberAPI;
import com.example.myrestservice.service.WeatherAPI;
import com.example.myrestservice.FileStorage.FileStorage;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/myrest")
public class MyrestResource {

    @GetMapping("/tripDetails")
    public ResponseEntity<String> getTripDetails(

            @RequestParam("city") String city,
            @RequestParam("country") String country,
            @RequestParam("datetime") String datetime) {
        try {
            LocalDateTime parsedDate = parseDate(datetime);

            RandomNumberAPI random = new RandomNumberAPI();
            String userID = random.getRandomNumber();
            String tripID = random.getRandomNumber();

            WeatherAPI weatherAPI = new WeatherAPI();
            String weatherData = weatherAPI.getWeatherData(city, country);

            HotelAPI hotelAPI = new HotelAPI();
            String hotelData = hotelAPI.getHotelData(city);

            JSONObject tripDetails = new JSONObject();
            tripDetails.put("userID", userID);
            tripDetails.put("tripID", tripID);
            tripDetails.put("location", city + "," + country);
            tripDetails.put("datetime", parsedDate.toString());
            tripDetails.put("weather", new JSONObject(weatherData));
            tripDetails.put("hotels", new JSONObject(hotelData));

            FileStorage.storeTripDetails(userID, tripID, city + ", " + country, parsedDate.toString(), weatherData, hotelData);

            return ResponseEntity.ok(tripDetails.toString());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid date format: " + e.getMessage());
        } catch (IOException e) {
            JSONObject errorDetails = new JSONObject();
            errorDetails.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorDetails.toString());
        }
    }

    private LocalDateTime parseDate(String datetimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a");
        try {
            return LocalDateTime.parse(datetimeStr, formatter);
        } catch (DateTimeParseException e) {
            // Handling the exception if the date format is incorrect
            throw new IllegalArgumentException("Invalid date format: " + datetimeStr);
        }
    }
}
