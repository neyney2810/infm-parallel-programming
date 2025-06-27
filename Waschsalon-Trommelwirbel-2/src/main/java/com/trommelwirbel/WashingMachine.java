package com.trommelwirbel;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a washing machine in the laundromat
 */
public class WashingMachine {
    private static final Logger logger = LoggerFactory.getLogger(WashingMachine.class);

    private final int machineId;
    private final Lock machineLock;
    private volatile Customer currentCustomer;
    private volatile LocalDateTime washStartTime;
    private final AtomicInteger totalWashesCompleted;
    private final Random random;

    public WashingMachine(int machineId) {
        this.machineId = machineId;
        this.machineLock = new ReentrantLock();
        this.totalWashesCompleted = new AtomicInteger(0);
        this.random = new Random();
    }

    public int getMachineId() {
        return machineId;
    }

    public Lock getLock() {
        return machineLock;
    }

    public boolean isAvailable() {
        return currentCustomer == null;
    }

    public synchronized Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void processWashing(Customer customer) {
        machineLock.lock();
        try {
            while (customer.hasRemainingLoads()) {
                // Simulate washing process
                this.currentCustomer = customer;
                this.washStartTime = LocalDateTime.now();
                customer.setFirstWashStartTime(washStartTime);
                logger.info("Machine {} started washing for customer {} (load {}/{})",
                        this.getMachineId(), customer.getName(),
                        customer.getTotalLoads() - customer.getRemainingLoads() + 1, customer.getTotalLoads());
                // Random wash duration between 5-15 seconds
                int washDurationSeconds = random.nextInt(11) + 5;

                Thread.sleep(washDurationSeconds * 1000);

                logger.info("Machine {} finished washing for customer {} (remaining loads: {})",
                        this.getMachineId(), customer.getName(), customer.getRemainingLoads());
                Thread.sleep(1000);
                customer.decrementRemainingLoads();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Washing interrupted", e);
        } finally {
            this.currentCustomer = null;
            this.washStartTime = null;
            this.totalWashesCompleted.incrementAndGet();
            machineLock.unlock();
        }
    }

    public LocalDateTime getWashStartTime() {
        return washStartTime;
    }

    public int getTotalWashesCompleted() {
        return totalWashesCompleted.get();
    }

    @Override
    public String toString() {
        return String.format("WashingMachine[id=%d, available=%s, washes=%d]",
                machineId, isAvailable(), totalWashesCompleted.get());
    }
}