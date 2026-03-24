import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
public class UnstableRemoteService {
    public static String fetchRawData(int id) {
        long delay = 100 + (id % 3) * 50; // 100, 150, 200 ms
        try {
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "RawData_" + id;
    }
    // если его вызвать более 3 раз подряд из одного потока (симуляция rate limiting или битых данных)
    public static String processData(String rawData, int threadId) {
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ThreadLocal<Integer> counter = threadFailureCounter;
        if (counter.get() == null) {
            counter = ThreadLocal.withInitial(() -> 0);
        }

        if (counter.get() >= 3 && ThreadLocalRandom.current().nextInt(100) < 20) {
            threadFailureCounter.set(0);
            throw new RuntimeException("Temporary service degradation on thread " + threadId);
        }
        threadFailureCounter.set(counter.get() + 1);

        return "Processed_" + rawData + "_by_thread_" + threadId;
    }
    private static final ThreadLocal<Integer> threadFailureCounter = ThreadLocal.withInitial(() -> 0);
}
