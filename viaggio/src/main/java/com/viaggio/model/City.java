package com.viaggio.model;

public class City {
    private final String nome;
    private final Coordinate coordinate;
    private Meteo meteo;

    public City(String nome, Coordinate coordinate) {
        this.nome = nome;
        this.coordinate = coordinate;
    }

    public String getNome() {
        return nome;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Meteo getMeteo() {
        return meteo;
    }

    public void setMeteo(Meteo meteo) {
        this.meteo = meteo;
    }
}
