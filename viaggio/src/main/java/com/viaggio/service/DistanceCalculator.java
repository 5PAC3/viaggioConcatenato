package com.viaggio.service;

import com.viaggio.model.Coordinate;
import com.viaggio.model.Tappa;

public class DistanceCalculator {

    private static final double RAGGIO_TERRA = 6371.0;

    public double calcolaDistanza(Coordinate from, Coordinate to) {
        double lat1 = Math.toRadians(from.getLat());
        double lat2 = Math.toRadians(to.getLat());
        double deltaLat = Math.toRadians(to.getLat() - from.getLat());
        double deltaLon = Math.toRadians(to.getLon() - from.getLon());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAGGIO_TERRA * c;
    }

    public double calcolaDistanzaArrotondata(Coordinate from, Coordinate to) {
        return Math.round(calcolaDistanza(from, to));
    }
}
