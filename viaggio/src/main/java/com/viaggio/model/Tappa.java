package com.viaggio.model;

public class Tappa {
    private final City citta;
    private final double kmDaPrecedente;
    private final boolean isOrigine;

    public Tappa(City citta, double kmDaPrecedente, boolean isOrigine) {
        this.citta = citta;
        this.kmDaPrecedente = kmDaPrecedente;
        this.isOrigine = isOrigine;
    }

    public City getCitta() {
        return citta;
    }

    public double getKmDaPrecedente() {
        return kmDaPrecedente;
    }

    public boolean isOrigine() {
        return isOrigine;
    }
}
