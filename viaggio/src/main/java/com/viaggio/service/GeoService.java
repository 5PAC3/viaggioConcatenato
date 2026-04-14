package com.viaggio.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.viaggio.model.Coordinate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeoService {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public Coordinate getCoordinate(String citta) throws Exception {
        String encodedCity = URLEncoder.encode(citta, "UTF-8");
        String urlString = NOMINATIM_URL + "?q=" + encodedCity + "&format=json&addressdetails=1";

        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "ViaggioConcatenato/1.0 (mailto:tuo.email@dominio.com)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

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

        JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
        if (jsonArray.size() == 0) {
            throw new RuntimeException("Citta non trovata: " + citta);
        }

        JsonElement first = jsonArray.get(0);
        double lat = first.getAsJsonObject().get("lat").getAsDouble();
        double lon = first.getAsJsonObject().get("lon").getAsDouble();

        return new Coordinate(lat, lon);
    }
}
