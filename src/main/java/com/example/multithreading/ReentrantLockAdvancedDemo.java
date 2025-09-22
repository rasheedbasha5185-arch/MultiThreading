package com.example.multithreading;

import java.util.concurrent.locks.ReentrantLock;

class SharedCounter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented count to " + count);
        } finally {
            lock.unlock();
        }
    }

    public void decrement() {
        lock.lock();
        try {
            count--;
            System.out.println(Thread.currentThread().getName() + " decremented count to " + count);
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

public class ReentrantLockAdvancedDemo {
    public static void main(String[] args) throws InterruptedException {
        SharedCounter counter = new SharedCounter();

        // 3 threads incrementing
        Thread t1 = new Thread(() -> performIncrement(counter, 5), "Increment-1");
        Thread t2 = new Thread(() -> performIncrement(counter, 5), "Increment-2");
        Thread t3 = new Thread(() -> performIncrement(counter, 5), "Increment-3");

        // 2 threads decrementing
        Thread t4 = new Thread(() -> performDecrement(counter, 5), "Decrement-1");
        Thread t5 = new Thread(() -> performDecrement(counter, 5), "Decrement-2");

        // Start all threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();

        System.out.println("Final counter value: " + counter.getCount());
    }

    private static void performIncrement(SharedCounter counter, int times) {
        for (int i = 0; i < times; i++) {
            counter.increment();
            sleep(100);
        }
    }

    private static void performDecrement(SharedCounter counter, int times) {
        for (int i = 0; i < times; i++) {
            counter.decrement();
            sleep(150);
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
