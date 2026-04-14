package com.viaggio.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viaggio.model.Meteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static final String API_KEY = "1b81611e53310aed347fb05deacf47f7";
    private static final String OPEN_WEATHER_URL = 
        "https://api.openweathermap.org/data/2.5/weather";

    public Meteo getMeteo(String city) throws Exception {

        String urlString = OPEN_WEATHER_URL 
                + "?q=" + city
                + "&units=metric"
                + "&lang=it"
                + "&appid=" + API_KEY;

        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP error: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

        // 📌 Estrarre dati
        JsonObject main = json.getAsJsonObject("main");
        JsonObject weather = json.getAsJsonArray("weather").get(0).getAsJsonObject();

        double temp = main.get("temp").getAsDouble();
        double tempMin = main.get("temp_min").getAsDouble();
        double tempMax = main.get("temp_max").getAsDouble();

        String condizione = weather.get("description").getAsString();

        return new Meteo(condizione, tempMin, tempMax);
    }
}