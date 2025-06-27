# My Java Project
## Aufgabe 1

Der Waschsalon "Trommelwirbel" besitzt 3 Waschmaschinen.
Es kommen 40 Kund:innen mit zufällig 1 bis 5 Ladungen Wäsche. Regelmässig nach einer zufälligen Zeit zwischen 1 und 8 Minuten kommt ein:e Kund:in herein. 1 einzelne Wäsche dauert zufällig zwischen 5 und 15 Minuten Waschzeit.
Setze diese Situation mit Threads, synchronized, wait und notify um. Skaliere Minuten in Sekunden in der Simulation. Schicke deinen Source Code und einen Beispieldurchlauf bis zum 15.5.2025 9:00 Uhr (spätestens bis zum 19.5.2025 9.00 Uhr) an christian.meder@gmail.com.

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
