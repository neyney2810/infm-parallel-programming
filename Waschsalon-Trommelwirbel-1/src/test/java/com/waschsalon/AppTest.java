package com.waschsalon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {

    @Test
    public void testWaschsalonInitialization() {
        // Test that the Waschsalon initializes correctly
        Waschsalon waschsalon = new Waschsalon();
        assertNotNull(waschsalon, "Waschsalon should be initialized successfully");
    }
}