package com.example.multithreading;

import java.util.LinkedList;
import java.util.Queue;
// Step 1: Implement Custom BlockingQueue
class CustomBlockingQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public CustomBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // wait until space is available
        }
        queue.add(item);
        notifyAll(); // notify consumers
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // wait until item available
        }
        T item = queue.poll();
        notifyAll(); // notify producers
        return item;
    }
}

// Step 2: Domain class (Order)
class Order {
    private final String id;
    private final String customer;

    public Order(String id, String customer) {
        this.id = id;
        this.customer = customer;
    }

    public String getId() { return id; }
    public String getCustomer() { return customer; }
}

// Step 3: Producer
class Producer implements Runnable {
    private final CustomBlockingQueue<Order> queue;

    public Producer(CustomBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                Order order = new Order("Order-" + i, "Customer-" + i);
                queue.put(order);
                System.out.println(Thread.currentThread().getName() + " placed " + order.getId());
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Step 4: Consumer
class Consumer implements Runnable {
    private final CustomBlockingQueue<Order> queue;

    public Consumer(CustomBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                System.out.println(Thread.currentThread().getName() + " processing " + order.getId() +
                        " for " + order.getCustomer());
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Step 5: Demo
public class ProducerConsumerCustomDemo {
    public static void main(String[] args) {
        CustomBlockingQueue<Order> queue = new CustomBlockingQueue<>(3);

        Thread producer = new Thread(new Producer(queue), "Producer");
        Thread consumer1 = new Thread(new Consumer(queue), "Consumer-1");
        Thread consumer2 = new Thread(new Consumer(queue), "Consumer-2");

        producer.start();
        consumer1.start();
        consumer2.start();
    }
}
