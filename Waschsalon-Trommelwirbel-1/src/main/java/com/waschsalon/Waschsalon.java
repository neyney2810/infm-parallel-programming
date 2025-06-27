package com.waschsalon;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Waschsalon {
    private static final Logger logger = LoggerConfig.getLogger(Waschsalon.class.getName());
    private final List<Waschmaschine> waschmaschinen;
    private final Object lock = new Object();

    public Waschsalon() {
        waschmaschinen = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            waschmaschinen.add(new Waschmaschine(i, this));
        }
        logger.info("🏪 Waschsalon 'Trommelwirbel' eröffnet mit 3 Waschmaschinen!");
    }

    public void kundeKommtAn(Kunde kunde) {
        synchronized (lock) {
            logger.info("👋 Kunde " + kunde.getId() + " betritt den Waschsalon mit " +
                    kunde.getAnzahlLadungen() + " Ladung(en)");

            // Starte den Kunden-Thread
            new Thread(kunde, "Kunde-" + kunde.getId()).start();
        }
    }

    public Waschmaschine getVerfuegbareWaschmaschine(Kunde kunde) throws InterruptedException {
        synchronized (lock) {
            while (true) {

                // Suche nach einer verfügbaren Waschmaschine
                for (Waschmaschine maschine : waschmaschinen) {
                    printWaschmaschinenWithStatus();

                    if (maschine.istVerfuegbar()) {
                        logger.info("✅ Kunde " + kunde.getId() + " hat Waschmaschine " + maschine.getId() +
                                " gefunden und startet den Waschgang");
                        return maschine;
                    }
                }

                logger.info("⏳ Keine Waschmaschine verfügbar. Kunde " + kunde.getId() +
                        " wartet auf eine freie Maschine...");
                String aktiveThreads = Thread.getAllStackTraces().keySet().stream()
                        .filter(thread -> thread.getName().startsWith("Kunde-"))
                        .map(Thread::getName)
                        .reduce((t1, t2) -> t1 + ", " + t2)
                        .orElse("Keine aktiven Kunde-Threads");
                logger.info("🧵 Aktive Kunde: " + aktiveThreads);

                lock.wait(); // Warten, bis eine Maschine frei wird
            }
        }
    }

    public void zeigeTagesstatistik() {
        logger.info("\n📊 Tagesstatistik des Waschsalons 'Trommelwirbel':");
        logger.info("   - 40 Kunden bedient");
        logger.info("   - 3 Waschmaschinen im Einsatz");
        logger.info("   - Simulation beendet");
    }

    public synchronized Object getLock() {
        return lock;
    }

    public synchronized void printWaschmaschinenWithStatus() {
        StringBuilder statusBuilder = new StringBuilder("Waschmaschinen Status: ");
        for (Waschmaschine maschine : waschmaschinen) {
            String status = maschine.istVerfuegbar() ? "Verfügbar"
                    : "Kunde " + maschine.getAktuellerKunde().getId();
            statusBuilder.append("Maschine ").append(maschine.getId()).append(" - ").append(status).append("; ");
        }
        logger.info(statusBuilder.toString());
    }
}