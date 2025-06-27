package com.trommelwirbel.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a washing machine in the laundromat
 */
public class WashingMachine {
    private final int machineId;
    private final Lock machineLock;
    private volatile Customer currentCustomer;
    private volatile LocalDateTime washStartTime;
    private final AtomicInteger totalWashesCompleted;

    public WashingMachine(int machineId) {
        this.machineId = machineId;
        this.machineLock = new ReentrantLock();
        this.totalWashesCompleted = new AtomicInteger(0);
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

    public void startWash(Customer customer) {
        this.currentCustomer = customer;
        this.washStartTime = LocalDateTime.now();
        customer.setFirstWashStartTime(washStartTime);
    }

    public void finishWash(int washDurationSeconds) {
        this.currentCustomer = null;
        this.washStartTime = null;
        this.totalWashesCompleted.incrementAndGet();
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