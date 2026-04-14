package com.viaggio;

import com.viaggio.model.Tappa;
import com.viaggio.service.TripManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TripManager tripManager = new TripManager();

        System.out.println("=== GESTIONE VIAGGIO CONCATENATO ===\n");

        while (true) {
            System.out.print("Inserisci nome citta (o 'fine' per terminare): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("fine") || input.equalsIgnoreCase("stop")) {
                break;
            }

            if (input.isEmpty()) {
                System.out.println("Inserisci un nome valido.");
                continue;
            }

            try {
                tripManager.aggiungiTappa(input);
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }

        if (tripManager.getNumeroTappe() == 0) {
            System.out.println("\nNessuna tappa inserita. Arrivederci!");
            return;
        }

        try {
            tripManager.caricaMeteo();
        } catch (Exception e) {
            System.out.println("Errore nel caricamento meteo: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("RIEPILOGO VIAGGIO");
        System.out.println("=".repeat(60));

        List<Tappa> tappe = tripManager.getTappeConDistanze();

        for (Tappa tappa : tappe) {
            System.out.println("\n>>> " + tappa.getCitta().getNome().toUpperCase());

            if (!tappa.isOrigine()) {
                System.out.println("   Km dall'ultima tappa: " + (int) tappa.getKmDaPrecedente());
            } else {
                System.out.println("   (Partenza)");
            }

            if (tappa.getCitta().getMeteo() != null) {
                System.out.println("   Meteo: " + tappa.getCitta().getMeteo());
            } else {
                System.out.println("   Meteo: non disponibile");
            }
        }

        System.out.println("\n" + "-".repeat(60));
        System.out.println("KM TOTALI DEL VIAGGIO: " + (int) tripManager.getKmTotali());
        System.out.println("=".repeat(60));

        scanner.close();
    }
}
