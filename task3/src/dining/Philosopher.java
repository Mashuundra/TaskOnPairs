package dining;

public class Philosopher extends Thread {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private final long startTime;
    private final boolean useDeadlockStrategy;

    public Philosopher(int id, Fork leftFork, Fork rightFork, long startTime, boolean useDeadlockStrategy) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.startTime = startTime;
        this.useDeadlockStrategy = useDeadlockStrategy;
    }

    private void log(String action) {
        long elapsed = System.currentTimeMillis() - startTime;
        synchronized (System.out) {
            System.out.printf("[%4d ms] Философ %d %s%n", elapsed, id, action);
        }
    }

    private void think() throws InterruptedException {
        log("думает");
        if (!useDeadlockStrategy) {
            Thread.sleep(10);
        }
    }

    private void eat() throws InterruptedException {
        log("голоден");

        if (useDeadlockStrategy) {
            synchronized (leftFork) {
                log("взял левую вилку " + leftFork.getId());
                Thread.sleep(10);
                synchronized (rightFork) {
                    log("взял правую вилку " + rightFork.getId());
                    log("ест");
                    Thread.sleep(100);
                }
            }
        } else {
            Fork first = leftFork.getId() < rightFork.getId() ? leftFork : rightFork;
            Fork second = leftFork.getId() < rightFork.getId() ? rightFork : leftFork;

            synchronized (first) {
                log("взял вилку " + first.getId());
                synchronized (second) {
                    log("взял вилку " + second.getId());
                    log("ест");
                    Thread.sleep(100);
                }
            }
        }

        log("положил вилки");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10 * id);
        } catch (InterruptedException e) {
            return;
        }

        while (true) {
            try {
                think();
                eat();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}