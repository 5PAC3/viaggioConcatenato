# Viaggio Concatenato

Programma CLI Java per la gestione di viaggi con tappe concatenate.

## Funzionalita

- Inserimento di tappe in ordine (es. Milano, Torino, Genova, Savona)
- Calcolo distanza tra ogni tappa (km)
- Visualizzazione condizioni meteo (temperatura min/max, descrizione)
- Riepilogo km totali del viaggio

## Requisiti API

### 1. OpenStreetMap - Nominatim (Coordinate)
- **URL:** `https://nominatim.openstreetmap.org/search`
- **API Key:** Non richiesta
- **Documentazione:** https://nominatim.org/

### 2. OpenRouteService (Distanze su strada)
- **URL:** `https://api.openrouteservice.org/v2/directions/driving-car`
- **API Key:** Richiesta (gratuita su https://openrouteservice.org/dev/#/signup)
- **Documentazione:** https://openrouteservice.org/dev/#/api-docs/v2/directions/{profile}/post

### 3. OpenWeatherMap (Meteo)
- **URL:** `https://api.openweathermap.org/data/2.5/weather`
- **API Key:** Richiesta (gratuita su https://openweathermap.org/api)
- **Documentazione:** https://openweathermap.org/current

## Configurazione

Le API keys devono essere impostate come variabili ambiente:

```bash
export OPENWEATHERMAP_API_KEY="tua_api_key"
export OPENROUTESERVICE_API_KEY="tua_api_key"
```

Oppure crea un file `.env` nella root del progetto:

```bash
OPENWEATHERMAP_API_KEY=tua_api_key
OPENROUTESERVICE_API_KEY=tua_api_key
```

## Compilazione

```bash
cd viaggio
javac -d target/classes -cp "$HOME/.m2/repository/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar" \
  src/main/java/com/viaggio/model/*.java \
  src/main/java/com/viaggio/service/*.java \
  src/main/java/com/viaggio/Main.java
```

## Esecuzione

```bash
GSON_JAR="$HOME/.m2/repository/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"
java -cp "target/classes:$GSON_JAR" com.viaggio.Main
```

## Utilizzo

1. Avviare il programma
2. Inserire i nomi delle citta una alla volta
3. Digitare `fine` per terminare l'inserimento
4. Visualizzare il riepilogo con km, meteo e totale

### Esempio di output

```
=== GESTIONE VIAGGIO CONCATENATO ===

Inserisci nome citta (o 'fine' per terminare): Milano
Inserisci nome citta (o 'fine' per terminare): Torino
Inserisci nome citta (o 'fine' per terminare): fine

============================================================
RIEPILOGO VIAGGIO
============================================================

>>> MILANO
   (Partenza)
   Meteo: Pioggia leggera | Min: 12.0°C | Max: 17.0°C

>>> TORINO
   Km dall'ultima tappa: 126
   Meteo: Sereno | Min: 10.0°C | Max: 16.0°C

------------------------------------------------------------
KM TOTALI DEL VIAGGIO: 126
============================================================
```

## Struttura del Progetto

```
viaggio/
├── pom.xml
└── src/main/java/com/viaggio/
    ├── Main.java
    ├── model/
    │   ├── City.java
    │   ├── Coordinate.java
    │   ├── Meteo.java
    │   └── Tappa.java
    └── service/
        ├── GeoService.java      # Nominatim API
        ├── RouteService.java    # OpenRouteService API
        └── WeatherService.java   # OpenWeatherMap API
```

## Dipendenze

- Java 17+
- Gson 2.10.1
