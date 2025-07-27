package com.example.myrestservice.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.UUID;

public class RandomNumberAPI {
    private final String path = "http://api.random.org/json-rpc/4/invoke";

    public String getRandomNumber() throws IOException {
        String request = "{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"method\": \"generateUUIDs\",\n"
                + "    \"params\": {\n"
                + "        \"apiKey\": \"37498010-14e1-4721-b266-ab74141ec980\",\n"
                + "        \"n\": 1\n"
                + "    },\n"
                + "    \"id\": 15998\n"
                + "}";

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
            osw.write(request);
        }

        StringBuilder responseContent = new StringBuilder();
        int responseCode = connection.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);
        try {
            InputStream inputStream = responseCode == HttpURLConnection.HTTP_OK
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (inputStream != null) {
                try (BufferedReader response = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = response.readLine()) != null) {
                        responseContent.append(line);
                    }
                }
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                JSONObject jsonObj = new JSONObject(responseContent.toString());
                return jsonObj.getJSONObject("result").getJSONObject("random").getJSONArray("data").getString(0);
            } else {
                System.out.println("Error response: " + responseContent);
                return UUID.randomUUID().toString(); // Fallback to local UUID generation
            }
        } catch (IOException e) {
            e.printStackTrace();
            return UUID.randomUUID().toString(); // Fallback in case of exception
        }
    }

    public static void main(String[] args) throws IOException {
        RandomNumberAPI random = new RandomNumberAPI();
        System.out.println(random.getRandomNumber());
    }
}
