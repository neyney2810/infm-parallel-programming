package com.trommelwirbel;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a customer in the laundromat with their washing loads
 */
public class Customer {
    private static final AtomicInteger customerCounter = new AtomicInteger(0);

    private final int customerId;
    private final String name;
    private final int totalLoads;
    private final AtomicInteger remainingLoads;
    private final LocalDateTime arrivalTime;
    private volatile LocalDateTime firstWashStartTime;
    private volatile LocalDateTime completionTime;

    public Customer(String name, int totalLoads) {
        this.customerId = customerCounter.incrementAndGet();
        this.name = name;
        this.totalLoads = totalLoads;
        this.remainingLoads = new AtomicInteger(totalLoads);
        this.arrivalTime = LocalDateTime.now();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public int getTotalLoads() {
        return totalLoads;
    }

    public int getRemainingLoads() {
        return remainingLoads.get();
    }

    public boolean hasRemainingLoads() {
        return remainingLoads.get() > 0;
    }

    public void decrementRemainingLoads() {
        remainingLoads.decrementAndGet();
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDateTime getFirstWashStartTime() {
        return firstWashStartTime;
    }

    public void setFirstWashStartTime(LocalDateTime firstWashStartTime) {
        if (this.firstWashStartTime == null) {
            this.firstWashStartTime = firstWashStartTime;
        }
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public boolean isComplete() {
        return remainingLoads.get() == 0;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, name=%s, loads=%d/%d]",
                customerId, name, totalLoads - remainingLoads.get(), totalLoads);
    }
}