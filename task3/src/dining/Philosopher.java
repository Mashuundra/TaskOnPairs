package dining;

import java.util.Random;

public class Philosopher extends Thread {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private final Random random = new Random();
    private final long startTime;

    public Philosopher(int id, Fork leftFork, Fork rightFork, long startTime) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.startTime = startTime;
    }

    private void log(String action) {
        long elapsed = System.currentTimeMillis() - startTime;
        synchronized (System.out) {
            System.out.printf("[%4d ms] Философ %d %s%n", elapsed, id, action);
        }
    }

    private void think() throws InterruptedException {
        log("думает");
        Thread.sleep(random.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        log("голоден");

        Fork first = leftFork.getId() < rightFork.getId() ? leftFork : rightFork;
        Fork second = leftFork.getId() < rightFork.getId() ? rightFork : leftFork;

        synchronized (first) {
            log("взял вилку " + first.getId());
            synchronized (second) {
                log("взял вилку " + second.getId());
                log("ест");
                Thread.sleep(random.nextInt(1000));
            }
        }

        log("положил вилки");
    }

    @Override
    public void run() {
        while (true) {
            try {
                think();
                eat();
            } catch (InterruptedException e) {
                log("прерван");
                break;
            }
        }
    }
}