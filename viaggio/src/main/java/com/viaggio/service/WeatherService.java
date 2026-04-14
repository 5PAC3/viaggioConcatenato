package com.viaggio.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viaggio.model.Coordinate;
import com.viaggio.model.Meteo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {
    private static final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast";

    public Meteo getMeteo(Coordinate coordinate) throws Exception {
        String urlString = OPEN_METEO_URL + "?latitude=" + coordinate.getLat() 
                + "&longitude=" + coordinate.getLon() 
                + "&daily=temperature_2m_max,temperature_2m_min,weather_code&timezone=auto&forecast_days=1";

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
        JsonObject daily = json.getAsJsonObject("daily");

        JsonArray tempMaxArr = daily.getAsJsonArray("temperature_2m_max");
        JsonArray tempMinArr = daily.getAsJsonArray("temperature_2m_min");
        JsonArray weatherCodeArr = daily.getAsJsonArray("weather_code");

        double tempMax = tempMaxArr.get(0).getAsDouble();
        double tempMin = tempMinArr.get(0).getAsDouble();
        int weatherCode = weatherCodeArr.get(0).getAsInt();

        String condizione = getCondizioneMeteo(weatherCode);

        return new Meteo(condizione, tempMin, tempMax);
    }

    private String getCondizioneMeteo(int code) {
        return switch (code) {
            case 0 -> "Sereno";
            case 1, 2, 3 -> "Parzialmente nuvoloso";
            case 45, 48 -> "Nebbia";
            case 51, 53, 55 -> "Pioggia leggera";
            case 61, 63, 65 -> "Pioggia";
            case 66, 67 -> "Pioggia gelata";
            case 71, 73, 75 -> "Neve";
            case 77 -> "Granelli di neve";
            case 80, 81, 82 -> "Rovesci";
            case 85, 86 -> "Rovesci di neve";
            case 95 -> "Temporale";
            case 96, 99 -> "Temporale con grandine";
            default -> "Sconosciuto";
        };
    }
}
