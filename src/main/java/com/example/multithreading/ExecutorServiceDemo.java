package com.example.multithreading;

import java.util.concurrent.*;

// Simulates a task, e.g., processing an order
class OrderTask implements Callable<String> {
    private final String orderId;
    private final int processingTime; // in ms

    public OrderTask(String orderId, int processingTime) {
        this.orderId = orderId;
        this.processingTime = processingTime;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " started processing " + orderId);
        Thread.sleep(processingTime); // simulate task duration
        return orderId + " processed in " + processingTime + " ms by " + Thread.currentThread().getName();
    }
}

public class ExecutorServiceDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Step 1: Create a fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Step 2: Wrap it in CompletionService to handle tasks as they finish
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        // Step 3: Submit tasks (simulate variable processing time)
        completionService.submit(new OrderTask("Order-1", 1200));
        completionService.submit(new OrderTask("Order-2", 800));
        completionService.submit(new OrderTask("Order-3", 2000));
        completionService.submit(new OrderTask("Order-4", 600));
        completionService.submit(new OrderTask("Order-5", 1000));

        // Step 4: Collect results as they complete
        for (int i = 0; i < 5; i++) {
            Future<String> future = completionService.take(); // blocks until a task completes
            System.out.println("✅ Result: " + future.get());
        }

        // Step 5: Shutdown executor
        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }

        System.out.println("All tasks completed. Main thread exiting.");
    }
}
