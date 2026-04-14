package com.viaggio.service;

import com.viaggio.model.City;
import com.viaggio.model.Coordinate;
import com.viaggio.model.Tappa;

import java.util.ArrayList;
import java.util.List;

public class TripManager {
    private final List<City> tappe = new ArrayList<>();
    private final GeoService geoService;
    private final WeatherService weatherService;
    private final DistanceCalculator distanceCalculator;

    public TripManager() {
        this.geoService = new GeoService();
        this.weatherService = new WeatherService();
        this.distanceCalculator = new DistanceCalculator();
    }

    public void aggiungiTappa(String nomeCitta) throws Exception {
        System.out.println("Cerco coordinate per: " + nomeCitta + "...");
        Coordinate coord = geoService.getCoordinate(nomeCitta);
        City city = new City(nomeCitta, coord);
        tappe.add(city);
        System.out.println("  -> Trovata: lat=" + coord.getLat() + ", lon=" + coord.getLon());
    }

    public void caricaMeteo() throws Exception {
        System.out.println("\nCaricamento meteo...");
        for (City city : tappe) {
            System.out.println("Meteo per " + city.getNome() + "...");
            var meteo = weatherService.getMeteo(city.getCoordinate());
            city.setMeteo(meteo);
        }
    }

    public List<Tappa> getTappeConDistanze() {
        List<Tappa> risultato = new ArrayList<>();
        Coordinate precedente = null;

        for (City city : tappe) {
            double km = 0;
            boolean isOrigine = (precedente == null);

            if (precedente != null) {
                km = distanceCalculator.calcolaDistanzaArrotondata(precedente, city.getCoordinate());
            }

            risultato.add(new Tappa(city, km, isOrigine));
            precedente = city.getCoordinate();
        }

        return risultato;
    }

    public double getKmTotali() {
        double totale = 0;
        Coordinate precedente = null;

        for (City city : tappe) {
            if (precedente != null) {
                totale += distanceCalculator.calcolaDistanzaArrotondata(precedente, city.getCoordinate());
            }
            precedente = city.getCoordinate();
        }

        return totale;
    }

    public int getNumeroTappe() {
        return tappe.size();
    }
}
