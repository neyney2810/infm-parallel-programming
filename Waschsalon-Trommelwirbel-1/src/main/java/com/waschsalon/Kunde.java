package com.waschsalon;

import java.util.Random;
import java.util.logging.Logger;

public class Kunde implements Runnable {
    private static final Logger logger = LoggerConfig.getLogger(Kunde.class.getName());
    private final int id;
    private final int anzahlLadungen;
    private final Waschsalon waschsalon;
    private final Random random;

    public Kunde(int id, Waschsalon waschsalon) {
        this.id = id;
        this.waschsalon = waschsalon;
        this.random = new Random();
        this.anzahlLadungen = 1 + random.nextInt(5); // 1-5 Ladungen
    }

    @Override
    public void run() {
        try {
            for (int ladung = 1; ladung <= anzahlLadungen; ladung++) {
                logger.info("🧺 Kunde " + id + " möchte Ladung " + ladung + "/" + anzahlLadungen + " waschen");

                Waschmaschine maschine = waschsalon.getVerfuegbareWaschmaschine(this);

                maschine.starten(this);

                if (ladung < anzahlLadungen) {
                    Thread.sleep(10); // 1 Sekunde Pause
                }
            }

            logger.info(
                    "🎉 Kunde " + id + " hat alle " + anzahlLadungen + " Ladung(en) gewaschen");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("❌ Kunde " + id + " wurde unterbrochen");
        }
    }

    public int getId() {
        return id;
    }

    public int getAnzahlLadungen() {
        return anzahlLadungen;
    }
}