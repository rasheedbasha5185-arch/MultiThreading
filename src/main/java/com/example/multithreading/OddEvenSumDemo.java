package com.example.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OddEvenSumDemo {

    public static void main(String[] args) {
        int max = 10;
        NumberPrinter printer = new NumberPrinter(max);

        Thread oddThread = new Thread(() -> {
            try {
                printer.printOdd();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread evenThread = new Thread(() -> {
            try {
                printer.printEven();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread sumThread = new Thread(() -> {
            try {
                printer.printSum();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        oddThread.start();
        evenThread.start();
        sumThread.start();
    }
}

// ------------------ Helper ------------------
class NumberPrinter {
    private final int max;
    private int number = 1;
    private final Object lock = new Object();
    private int lastOdd = 0;

    private final BlockingQueue<int[]> sumQueue = new LinkedBlockingQueue<>();
    private String turn = "odd"; // controls which thread runs

    public NumberPrinter(int max) {
        this.max = max;
    }

    public void printOdd() throws InterruptedException {
        while (number <= max) {
            synchronized (lock) {
                while (!turn.equals("odd")) {
                    lock.wait();
                }
                if (number % 2 == 1) {
                    System.out.println("Odd: " + number);
                    lastOdd = number;
                    number++;
                    turn = "even"; // now Even’s turn
                    lock.notifyAll();
                }
            }
        }
    }

    public void printEven() throws InterruptedException {
        while (number <= max) {
            synchronized (lock) {
                while (!turn.equals("even")) {
                    lock.wait();
                }
                if (number % 2 == 0) {
                    System.out.println("Even: " + number);
                    sumQueue.put(new int[]{lastOdd, number});
                    number++;
                    turn = "sum"; // now Sum’s turn
                    lock.notifyAll();
                }
            }
        }
    }

    public void printSum() throws InterruptedException {
        while (true) {
            int[] pair = sumQueue.take();
            synchronized (lock) {
                while (!turn.equals("sum")) {
                    lock.wait();
                }
                int sum = pair[0] + pair[1];
                System.out.println("Sum: " + sum);

                if (pair[1] >= max) {
                    return; // stop after last even
                }
                turn = "odd"; // back to Odd
                lock.notifyAll();
            }
        }
    }
}
