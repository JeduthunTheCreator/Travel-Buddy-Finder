package com.example.myrestservice.controller;

import com.example.myrestservice.FileStorage.FileStorage;
import com.example.myrestservice.FileStorage.TripDetail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @GetMapping("/byUser")
    public ResponseEntity<List<TripDetail>> fetchTripsByUser(@RequestParam("userId") String userId) {
        List<TripDetail> trips = FileStorage.fetchTripsByUser(userId);
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/details")
    public ResponseEntity<?> fetchTripDetails(@RequestParam("tripId") String tripId) {
        TripDetail tripDetail = FileStorage.fetchTripDetails(tripId);
        if (tripDetail != null) {
            JSONObject tripJson = new JSONObject(tripDetail);
            return ResponseEntity.ok(tripJson.toString());
        } else {
            return ResponseEntity.status(404).body("Trip not found");
        }
    }

    @GetMapping("/byLocation")
    public ResponseEntity<?> fetchTripsByLocation(@RequestParam("location") String location) {
        List<TripDetail> trips = FileStorage.fetchTripsByLocation(location);
        JSONArray tripsJson = new JSONArray();
        for (TripDetail trip : trips) {
            tripsJson.put(new JSONObject(trip));
        }
        return ResponseEntity.ok(tripsJson.toString());
    }

    @PostMapping("/expressInterest")
    public ResponseEntity<?> expressInterest(@RequestParam("userId") String userId,
                                             @RequestParam("tripId") String tripId) {
        try {
            boolean success = FileStorage.expressInterest(userId, tripId);
            if (success) {
                return ResponseEntity.ok("Interest expressed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not express interest");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}
