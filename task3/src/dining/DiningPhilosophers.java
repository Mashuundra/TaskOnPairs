package dining;

public class DiningPhilosophers {
    public static void main(String[] args) {
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
            philosophers[i] = new Philosopher(i, left, right, startTime);
            philosophers[i].start();
        }

        System.out.println("Программа запущена. Философы обедают.");
    }
}