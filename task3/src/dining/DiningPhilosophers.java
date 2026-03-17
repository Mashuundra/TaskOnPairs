package dining;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DiningPhilosophers {
    // true = deadlock, false = решение
    private static final boolean DEADLOCK_MODE = false;

    public static void main(String[] args) {
        System.out.println(DEADLOCK_MODE);

        final int PHILOSOPHERS_COUNT = 5;
        Fork[] forks = new Fork[PHILOSOPHERS_COUNT];
        for (int i = 0; i < PHILOSOPHERS_COUNT; i++) {
            forks[i] = new Fork(i);
        }

        long startTime = System.currentTimeMillis();

        Philosopher[] philosophers = new Philosopher[PHILOSOPHERS_COUNT];
        for (int i = 0; i < PHILOSOPHERS_COUNT; i++) {
            Fork left = forks[i];
            Fork right = forks[(i + 1) % PHILOSOPHERS_COUNT];
            philosophers[i] = new Philosopher(i, left, right, startTime, DEADLOCK_MODE);
            philosophers[i].start();
        }

        System.out.println("Программа запущена. Философы обедают.");

        Thread monitor = new Thread(() -> {
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            while (true) {
                long[] deadlockedThreads = threadBean.findDeadlockedThreads();
                if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                    System.out.println("ПРОИЗОШЁЛ ДЕДЛОК");
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }
}