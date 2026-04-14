package com.viaggio.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viaggio.model.Coordinate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouteService {

    private static final String ORS_URL = "https://api.openrouteservice.org/v2/directions/driving-car";
    private final String apiKey;

    public RouteService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Restituisce la distanza in km su strada tra due coordinate,
     * usando l'API OpenRouteService.
     * In caso di errore (es. API non raggiungibile o chiave non valida),
     * effettua il fallback sulla distanza in linea d'aria (Haversine).
     */
    public double calcolaDistanzaStrada(Coordinate from, Coordinate to) {
        try {
            String urlString = String.format(
                    "%s?api_key=%s&start=%f,%f&end=%f,%f",
                    ORS_URL,
                    apiKey,
                    from.getLon(), from.getLat(),
                    to.getLon(), to.getLat()
            );

            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("  [RouteService] HTTP " + responseCode + " - fallback su Haversine");
                return fallback(from, to);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // La risposta GeoJSON ha: features[0].properties.segments[0].distance (in metri)
            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
            double distanzaMetri = json
                    .getAsJsonArray("features")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("properties")
                    .getAsJsonArray("segments")
                    .get(0).getAsJsonObject()
                    .get("distance").getAsDouble();

            return Math.round(distanzaMetri / 1000.0);

        } catch (Exception e) {
            System.err.println("  [RouteService] Errore: " + e.getMessage() + " - fallback su Haversine");
            return fallback(from, to);
        }
    }

    /** Distanza in linea d'aria (Haversine) usata come fallback. */
    private double fallback(Coordinate from, Coordinate to) {
        DistanceCalculator calc = new DistanceCalculator();
        return calc.calcolaDistanzaArrotondata(from, to);
    }
}
