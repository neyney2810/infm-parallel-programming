package com.waschsalon;

import java.util.Random;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = LoggerConfig.getLogger(App.class.getName());

    public static void main(String[] args) {
        logger.info("🎯 Starte Simulation des Waschsalons 'Trommelwirbel'");
        logger.info(new String(new char[60]).replace("\0", "="));

        Waschsalon waschsalon = new Waschsalon();
        Random random = new Random();

        // Thread for customer arrivals
        Thread kundenGenerator = new Thread(() -> {
            try {
                for (int i = 1; i <= 40; i++) {
                    Kunde kunde = new Kunde(i, waschsalon);
                    waschsalon.kundeKommtAn(kunde);

                    int wartezeit = 1 + random.nextInt(8);
                    logger.info("⏱️  Nächster Kunde kommt in " + wartezeit + " Sekunden...");
                    Thread.sleep(wartezeit * 1000);
                }
                logger.info("🚪 Alle 40 Kunden sind im Waschsalon angekommen!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("❌ Kundengenerator wurde unterbrochen");
            }
        }, "Kundengenerator");

        kundenGenerator.start();

        Thread statusMonitor = new Thread(() -> {
            try {
                Thread.sleep(300000); // 5 minutes
                logger.info("\n" + new String(new char[60]).replace("\0", "="));
                while (Thread.getAllStackTraces().keySet().stream().anyMatch(t -> t.getName().startsWith("Kunde-"))) {
                    logger.info("⏳ Kundengenerator läuft noch. Warte 5 Minuten...");
                    Thread.sleep(300000); // 5 minutes
                }
                logger.info("✅ Kein Kundengenerator-Thread mehr aktiv. Zeige Tagesstatistik:");
                waschsalon.zeigeTagesstatistik();
                logger.info("🛑 Beende alle aktiven Threads...");
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "StatusMonitor");

        statusMonitor.start();

    }
}