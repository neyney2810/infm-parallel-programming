package com.waschsalon;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfig {
    private static FileHandler fileHandler;

    static {
        try {
            // Configure the FileHandler to overwrite "waschsalon.log"
            fileHandler = new FileHandler("waschsalon.log", false); // Overwrite mode
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            System.err.println("Failed to initialize logger file handler: " + e.getMessage());
        }
    }

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }
        return logger;
    }
}