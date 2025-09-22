package com.example.multithreading;

class MyCyclicBarrier {
    private final int totalThreads;
    private int waitingThreads = 0;

    public MyCyclicBarrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void await() throws InterruptedException {
        waitingThreads++;
        System.out.println(Thread.currentThread().getName() + " reached the barrier (" + waitingThreads + "/" + totalThreads + ")");

        if (waitingThreads < totalThreads) {
            wait(); // wait until all threads reach
        } else {
            System.out.println("All threads reached barrier. Proceeding...");
            notifyAll(); // release all waiting threads
            waitingThreads = 0; // reset for potential reuse
        }
    }
}

class TaskWorker implements Runnable {
    private final MyCyclicBarrier barrier;
    private final String name;

    public TaskWorker(MyCyclicBarrier barrier, String name) {
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " doing pre-barrier work...");
            Thread.sleep((long) (Math.random() * 2000) + 500); // simulate preparation
            barrier.await();
            System.out.println(name + " proceeding after barrier.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class CustomCyclicBarrierDemo {
    public static void main(String[] args) {
        int numThreads = 3;
        MyCyclicBarrier barrier = new MyCyclicBarrier(numThreads);

        for (int i = 1; i <= numThreads; i++) {
            new Thread(new TaskWorker(barrier, "Thread-" + i)).start();
        }
    }
}
