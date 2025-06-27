package com.trommelwirbel;

import com.trommelwirbel.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the laundromat simulation components
 */
public class LaundryServiceTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Test Customer", 3);
    }

    @Test
    void testCustomerCreation() {
        assertEquals("Test Customer", customer.getName());
        assertEquals(3, customer.getTotalLoads());
        assertEquals(3, customer.getRemainingLoads());
        assertTrue(customer.hasRemainingLoads());
        assertFalse(customer.isComplete());
    }
}