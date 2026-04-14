package com.viaggio.model;

public class Meteo {
    private final String condizione;
    private final double tempMin;
    private final double tempMax;

    public Meteo(String condizione, double tempMin, double tempMax) {
        this.condizione = condizione;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public String getCondizione() {
        return condizione;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    @Override
    public String toString() {
        return String.format("%s | Min: %.1f°C | Max: %.1f°C", condizione, tempMin, tempMax);
    }
}
