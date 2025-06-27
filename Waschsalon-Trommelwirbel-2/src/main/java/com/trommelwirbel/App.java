package com.trommelwirbel;

import com.trommelwirbel.service.LaundryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Welcome to Trommelwirbel Laundromat Simulation!");
        logger.info("This simulation will test different ExecutorService implementations");
        logger.info("with 40 customers, 3 washing machines, and concurrent processing.");

        try {
            LaundryService laundryService = new LaundryService();
            laundryService.runSimulation();

            logger.info("Simulation completed successfully!");

        } catch (Exception e) {
            logger.error("Simulation failed with error: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}