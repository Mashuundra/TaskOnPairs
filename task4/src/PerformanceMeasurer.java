import java.util.function.Supplier;

public class PerformanceMeasurer {
    public static <T> long measure(Supplier<T> task) {
        long start = System.currentTimeMillis();
        task.get();
        return System.currentTimeMillis() - start;
    }
}