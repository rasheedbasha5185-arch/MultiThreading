package com.example.multithreading;

import java.util.*;
import java.util.stream.Collectors;

// Renamed Order class to PurchaseOrder
class PurchaseOrder {
    private final String id;
    private final double amount;

    public PurchaseOrder(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
}

public class ParallelStreamDemo {
    public static void main(String[] args) {
        // Sample list of purchase orders
        List<PurchaseOrder> orders = Arrays.asList(
                new PurchaseOrder("Order-1", 100),
                new PurchaseOrder("Order-2", 200),
                new PurchaseOrder("Order-3", 300),
                new PurchaseOrder("Order-4", 400),
                new PurchaseOrder("Order-5", 500)
        );

        // Sequential processing (normal stream)
        long startSeq = System.currentTimeMillis();
        List<String> processedSeq = orders.stream()
                .map(order -> processOrder(order))
                .collect(Collectors.toList());
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential processing time: " + (endSeq - startSeq) + " ms");

        // Parallel processing (parallel stream)
        long startPar = System.currentTimeMillis();
        List<String> processedPar = orders.parallelStream()
                .map(order -> processOrder(order))
                .collect(Collectors.toList());
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel processing time: " + (endPar - startPar) + " ms");

        // Print processed results
        System.out.println("Processed Orders: " + processedPar);
    }

    // Simulate time-consuming order processing
    private static String processOrder(PurchaseOrder order) {
        try {
            System.out.println(Thread.currentThread().getName() + " processing " + order.getId());
            Thread.sleep(500); // simulate processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return order.getId() + " done";
    }
}
