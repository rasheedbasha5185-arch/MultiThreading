package com.example.multithreading;

import java.util.concurrent.Semaphore;

class Resource {
    private final Semaphore semaphore = new Semaphore(3); // only 3 permits

    public void accessResource(String threadName) {
        try {
            System.out.println(threadName + " trying to acquire resource...");
            semaphore.acquire(); // acquire permit
            System.out.println("✅ " + threadName + " acquired resource.");

            // Simulate some work
            Thread.sleep(1000);

            System.out.println("🔓 " + threadName + " releasing resource.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release(); // release permit
        }
    }
}

public class SemaphoreDemo {
    public static void main(String[] args) {
        Resource resource = new Resource();

        // Start 6 threads (but only 3 allowed at once)
        for (int i = 1; i <= 6; i++) {
            String threadName = "Thread-" + i;
            new Thread(() -> resource.accessResource(threadName)).start();
        }
    }
}
