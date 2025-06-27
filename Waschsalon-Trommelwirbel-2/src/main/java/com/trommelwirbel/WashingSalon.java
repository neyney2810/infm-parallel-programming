package com.trommelwirbel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class WashingSalon {
    private static final Logger logger = LoggerFactory.getLogger(WashingSalon.class);

    private final List<WashingMachine> washingMachines;
    private final Queue<Customer> waitingQueue;
    private final List<Customer> completedCustomers;
    private final Lock queueLock;
    private final Condition machineAvailable;
    private final Random random;

    // Statistics
    public WashingSalon() {
        this.washingMachines = List.of(
                new WashingMachine(1),
                new WashingMachine(2),
                new WashingMachine(3));
        this.waitingQueue = new LinkedList<>();
        this.completedCustomers = Collections.synchronizedList(new ArrayList<>());
        this.queueLock = new ReentrantLock();
        this.machineAvailable = queueLock.newCondition();
        this.random = new Random();

        logger.info("Trommelwirbel initialized with {} washing machines", washingMachines.size());
    }

    public void runSimulation() {
        logger.info("Starting Simulation");

        // Test different ExecutorService implementations
        testWithExecutorService("FixedThreadPool", Executors.newFixedThreadPool(5));
        testWithExecutorService("CachedThreadPool", Executors.newCachedThreadPool());
    }

    private void testWithExecutorService(String executorType, ExecutorService executorService) {
        logger.info("\n=== Testing with {} ===", executorType);

        reset();

        try {
            // Submit customer arrival task
            CompletableFuture<Void> customerArrivalTask = CompletableFuture.runAsync(
                    () -> simulateCustomerArrivals(), executorService);

            // Submit machine processing tasks using lambda expressions
            List<CompletableFuture<Void>> machineProcessingTasks = washingMachines.stream()
                    .map(machine -> CompletableFuture.runAsync(
                            () -> processMachineQueue(machine), executorService))
                    .collect(Collectors.toList());

            // Wait for customer arrivals to finish
            customerArrivalTask.get();

            waitForAllCustomersToComplete();

            CompletableFuture.allOf(machineProcessingTasks.toArray(new CompletableFuture[0]))
                    .get(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            logger.error("Error during simulation with {}: {}", executorType, e.getMessage());
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        logger.info("Finished testing with {}", executorType);
    }

    private void simulateCustomerArrivals() {
        for (int i = 0; i < 40; i++) {
            try {
                // Random arrival time between 1-8 seconds
                int arrivalDelaySeconds = random.nextInt(8) + 1;
                Thread.sleep(arrivalDelaySeconds * 1000);

                // Create customer with random loads (1-5)
                int loads = random.nextInt(5) + 1;
                String customerName = "K" + (i + 1);
                Customer customer = new Customer(customerName, loads);

                // Add customer to queue using lambda-based synchronized operation
                queueLock.lock();
                try {
                    waitingQueue.offer(customer);
                    machineAvailable.signalAll();
                    logger.info("Customer {} arrived with {} loads. Queue: [{}]",
                            customer.getName(), loads, waitingQueue.stream()
                                    .map(Customer::getName)
                                    .collect(Collectors.joining(", ")));
                } finally {
                    queueLock.unlock();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Customer arrival simulation interrupted");
                break;
            }
        }

        logger.info("All 40 customers have arrived");
    }

    private void processMachineQueue(WashingMachine machine) {
        logger.info("Machine {} started processing", machine.getMachineId());

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Customer customer = getNextCustomerForMachine();
                if (customer == null) {
                    if (shouldEndSimulation()) {
                        break;
                    }
                    continue;
                }

                processWashLoad(machine, customer);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("Machine {} processing interrupted", machine.getMachineId());
                break;
            }
        }

        logger.info("Machine {} finished processing", machine.getMachineId());
    }

    private Customer getNextCustomerForMachine() throws InterruptedException {
        queueLock.lock();
        try {
            while (waitingQueue.isEmpty() && !shouldEndSimulation()) {
                machineAvailable.await(1, TimeUnit.SECONDS);
            }
            return waitingQueue.poll();
        } finally {
            queueLock.unlock();
        }
    }

    private void processWashLoad(WashingMachine machine, Customer customer) throws InterruptedException {
        machine.getLock().lock();
        try {
            machine.processWashing(customer);
            handleCustomerAfterWash(customer);

        } finally {
            machine.getLock().unlock();
        }
    }

    private void handleCustomerAfterWash(Customer customer) {
        if (customer.isComplete()) {
            // Customer finished all loads
            customer.setCompletionTime(LocalDateTime.now());
            completedCustomers.add(customer);

            logger.info("Customer {} completed all {} loads", customer.getName(), customer.getTotalLoads());
        } else {
            // Customer has more loads, add back to queue
            queueLock.lock();
            try {
                waitingQueue.offer(customer);
                machineAvailable.signalAll();
            } finally {
                queueLock.unlock();
            }
        }
    }

    private boolean shouldEndSimulation() {
        return completedCustomers.size() >= 40 && waitingQueue.isEmpty() &&
                washingMachines.stream().allMatch(WashingMachine::isAvailable);
    }

    private void waitForAllCustomersToComplete() throws InterruptedException {
        while (completedCustomers.size() < 40) {
            Thread.sleep(1000);
            logger.debug("Waiting for customers to complete. Completed: {}/40", completedCustomers.size());
        }
    }

    private void reset() {
        // Reset all statistics and state for new test
        completedCustomers.clear();
        waitingQueue.clear();

        // Reset washing machines
        washingMachines.forEach(machine -> {
            machine.getTotalWashesCompleted(); // Reset is handled internally
        });
    }
}