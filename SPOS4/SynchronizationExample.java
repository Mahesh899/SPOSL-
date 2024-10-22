
import java.util.concurrent.Semaphore;

class SharedBuffer {

    private int[] buffer;
    private int count, in, out;
    private Semaphore mutex, empty, full;

    public SharedBuffer(int size) {
        buffer = new int[size];
        count = in = out = 0;
        mutex = new Semaphore(1); // Mutual exclusion for buffer access
        empty = new Semaphore(size); // Semaphore to track empty slots
        full = new Semaphore(0); // Semaphore to track filled slots
    }

    public void produce(int item) throws InterruptedException {
        empty.acquire(); // Wait if no empty slot
        mutex.acquire(); // Acquire exclusive access to buffer

        buffer[in] = item;
        in = (in + 1) % buffer.length;
        count++;
        System.out.println("Produced: " + item);

        mutex.release(); // Release exclusive access to buffer
        full.release(); // Signal that there's one more filled slot
    }

    public void consume() throws InterruptedException {
        full.acquire(); // Wait if no filled slot
        mutex.acquire(); // Acquire exclusive access to buffer

        int item = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        System.out.println("Consumed: " + item);

        mutex.release(); // Release exclusive access to buffer
        empty.release(); // Signal that there's one more empty slot
    }
}

class Producer extends Thread {

    private SharedBuffer buffer;

    public Producer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.produce(i);
                Thread.sleep((int) (Math.random() * 100));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {

    private SharedBuffer buffer;

    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.consume();
                Thread.sleep((int) (Math.random() * 100));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class SynchronizationExample {

    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer(5);

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
