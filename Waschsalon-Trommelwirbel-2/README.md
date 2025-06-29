# Trommelwirbel Laundromat Simulation
## Aufgabe 2
Stelle dein Waschsalon Szenario auf das Executor Framework und Lock/Condition um. Teste unterschiedliche ExecutorServices und prüfe, ob du den Code mit Lambda-Ausdrücken kompakter gestalten kannst. Abgabe bis Mittwoch, den 4.6.2025 9 Uhr (spätestens bis Semesterende).


## Project Structure

```
my-java-project
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── App.java
│   │   └── resources
│   │       └── application.properties
│   ├── test
│       └── java
│           └── com
│               └── example
│                   └── AppTest.java
├── pom.xml
└── README.md
```

## Getting Started

To get started with this project, you will need to have Maven installed on your machine. You can download it from [Maven's official website](https://maven.apache.org/).

### Building the Project

To build the project, navigate to the project directory and run:

```
mvn clean install
```

### Running the Application

To run the application, use the following command:

```
mvn exec:java -Dexec.mainClass="com.example.App"
```

### Running Tests

To run the tests, execute:

```
mvn test
```

## Configuration

The application configuration can be found in `src/main/resources/application.properties`. You can modify this file to change application-specific settings.

Ein Java Maven Projekt, das eine Waschsalon-Simulation mit dem Executor Framework, Lock/Condition Mechanismen und Lambda-Ausdrücken implementiert.

## Projektbeschreibung

Der Waschsalon "Trommelwirbel" besitzt 3 Waschmaschinen und bedient 40 Kunden mit jeweils 1-5 Ladungen Wäsche. Die Simulation demonstriert:

- Implement Executor
- Besser implementation of queue: customer will do all there washes within 1 time and no need to reque after finishing each load

## Simulation Parameter

- **Waschmaschinen**: 3
- **Kunden**: 40 mit jeweils 1-5 zufälligen Ladungen
- **Ankunftszeit**: 1-8 Sekunden (skaliert von Sekunden)
- **Waschzeit**: 5-15 Sekunden pro Ladung (skaliert von Sekunden)

## Getestete ExecutorService Typen

1. **FixedThreadPool**: Feste Anzahl von Threads
2. **CachedThreadPool**: Dynamische Thread-Erstellung
3. **WorkStealingPool**: Work-Stealing-Algorithmus

## Projekt ausführen

### Voraussetzungen
- Java 17+
- Maven 3.6+

### Kompilieren und ausführen
```bash
# Projekt kompilieren
mvn clean compile

# Tests ausführen
mvn test

# Simulation starten
mvn exec:java

# Oder JAR bauen und ausführen
mvn clean package
java -jar target/laundromat-simulation-1.0.0.jar
```