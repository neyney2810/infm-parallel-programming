package com.waschsalon;

import java.util.Random;
import java.util.logging.Logger;

public class Waschmaschine {
    private static final Logger logger = LoggerConfig.getLogger(Waschmaschine.class.getName());
    private int id;
    private boolean istBesetzt;
    private Kunde aktuellerKunde;
    private Random random;
    private Waschsalon waschsalon;

    public Waschmaschine(int id, Waschsalon waschsalon) {
        this.waschsalon = waschsalon;
        this.id = id;
        this.istBesetzt = false;
        this.aktuellerKunde = null;
        this.random = new Random();
    }

    public synchronized boolean istVerfuegbar() {
        return !istBesetzt;
    }

    public synchronized void starten(Kunde kunde) {
        if (istBesetzt) {
            logger.warning("❗ Waschmaschine " + id + " ist bereits belegt!");
            throw new IllegalStateException("Waschmaschine " + id + " ist bereits belegt!");
        }

        this.istBesetzt = true;
        this.aktuellerKunde = kunde;

        int waschzeit = 5 + random.nextInt(11);

        logger.info("🍀 Kunde " + kunde.getId() + " startet Waschgang in Maschine " + id);
        logger.info("   - Waschzeit: " + waschzeit + " Sekunden");

        Thread waschingThread = new Thread(() -> {
            try {
                Thread.sleep(waschzeit * 1000);
                synchronized (this) {
                    logger.info("✅ Waschgang von Kunde " + aktuellerKunde.getId() +
                            " in Maschine " + id + " beendet (" + waschzeit + "s)");
                    istBesetzt = false;
                    aktuellerKunde = null;
                    logger.info("🔄 Waschmaschine " + id + " ist jetzt wieder verfügbar");

                    Thread.getAllStackTraces().keySet().stream()
                            .filter(thread -> thread.getName().startsWith("Kunde-"))
                            .forEach(thread -> logger.info("   - Thread: " + thread.getName()));
                }
                // Notify the Waschsalon that a machine is now available
                synchronized (waschsalon.getLock()) {
                    waschsalon.getLock().notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("❌ Waschmaschine " + id + " wurde unterbrochen");
            }
        }, "Waschmaschine-" + id);
        waschingThread.start();
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }

    public synchronized boolean isIstBesetzt() {
        return istBesetzt;
    }

    public synchronized void setIstBesetzt(boolean istBesetzt) {
        this.istBesetzt = istBesetzt;
    }

    public synchronized Kunde getAktuellerKunde() {
        return aktuellerKunde;
    }

    public synchronized void setAktuellerKunde(Kunde aktuellerKunde) {
        this.aktuellerKunde = aktuellerKunde;
    }

    public synchronized Waschsalon getWaschsalon() {
        return waschsalon;
    }

    public synchronized void setWaschsalon(Waschsalon waschsalon) {
        this.waschsalon = waschsalon;
    }
}