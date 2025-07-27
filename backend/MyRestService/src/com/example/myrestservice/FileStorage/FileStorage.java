package com.example.myrestservice.FileStorage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class FileStorage {

    private static final Logger LOGGER = Logger.getLogger(FileStorage.class.getName());
    private static final String CREDENTIALS_FILE = "C:\\Users\\idemu\\IdeaProjects\\MyRestService\\credentials.csv";
    private static final String TRIP_DETAILS_FILE = "C:\\Users\\idemu\\IdeaProjects\\MyRestService\\trip_details.csv";
    private static final String USER_INTERESTS_FILE = "C:\\Users\\idemu\\IdeaProjects\\MyRestService\\user_interests.csv";


    // Method to create CSV files
    public static void createFile() {
        try {
            createFile(CREDENTIALS_FILE, "username,password\n");
            createFile(TRIP_DETAILS_FILE, "user_id,trip_id,location,datetime,weather,hotel\n");
            createFile(USER_INTERESTS_FILE, "user_id,trip_id,interested_trip\n");
        } catch (IOException e) {
            System.out.println("Error creating files: " + e.getMessage());
        }
    }

    // Method to save user credentials in the file
    public static void saveCredentials(String username, String password) {
        File file = new File(CREDENTIALS_FILE);
        LOGGER.info("Attempting to write to file: " + file.getAbsolutePath());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(username + "," + hashPassword(password) + "\n");
        } catch (IOException | NoSuchAlgorithmException e) {
            LOGGER.severe("Error writing to file: " + e.getMessage());
        }
    }
    // Method to verify user credentials from the file
    public static boolean verifyCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username) && credentials[1].equals(hashPassword(password))) {
                    return true;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    public static List<TripDetail> fetchTrips(String userId, String tripId) {
        List<TripDetail> trips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRIP_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(userId) && details[1].equals(tripId)) {
                    trips.add(new TripDetail(details[0], details[1], details[2], details[3], details[4], details[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return trips;
    }

    public static TripDetail fetchTripDetails(String tripId) {
        try (BufferedReader br = new BufferedReader(new FileReader(TRIP_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(tripId)) {
                    return new TripDetail(details[0], details[1], details[2], details[3], details[4], details[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    public static List<TripDetail> fetchTripsByLocation(String location) {
        List<TripDetail> trips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRIP_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[2].contains(location)) {
                    trips.add(new TripDetail(details[0], details[1], details[2], details[3], details[4], details[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return trips;
    }

    public static List<TripDetail> fetchTripsByUser(String userId) {
        List<TripDetail> trips = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRIP_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(userId)) {
                    trips.add(new TripDetail(details[0], details[1], details[2], details[3], details[4], details[5]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return trips;
    }
    public static boolean expressInterest(String userId, String tripId) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_INTERESTS_FILE, true))) {
            String interestRecord = userId + "," + tripId + ",Yes\n";
            bw.write(interestRecord);
            return true; // Successfully written to file
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            return false; // Failed to write to file
        }
    }
    public static void storeTripDetails(String userId, String tripId, String location,
                                        String datetime, String weather, String hotel) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRIP_DETAILS_FILE, true))) {
            String tripRecord = String.join(",", userId, tripId, location, datetime, weather, hotel) + "\n";
            bw.write(tripRecord);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void createFile(String fileName, String header) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(header);
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static void main(String[] args) {
        createFile();
    }
}
