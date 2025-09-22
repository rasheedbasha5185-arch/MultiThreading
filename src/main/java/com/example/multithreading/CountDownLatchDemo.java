package com.example.multithreading;

import java.util.concurrent.CountDownLatch;

class Worker implements Runnable {
    private final CountDownLatch latch;
    private final String name;

    public Worker(CountDownLatch latch, String name) {
        this.latch = latch;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " starting task...");
            Thread.sleep((long) (Math.random() * 2000) + 500); // simulate work
            System.out.println(name + " completed task.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown(); // signal task completion
        }
    }
}

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        int workerCount = 3;
        CountDownLatch latch = new CountDownLatch(workerCount);

        // Start worker threads
        for (int i = 1; i <= workerCount; i++) {
            new Thread(new Worker(latch, "Worker-" + i)).start();
        }

        // Main thread waits for all workers to complete
        System.out.println("Main thread waiting for workers to finish...");
        latch.await();
        System.out.println("All workers completed. Main thread proceeding.");
    }
}
