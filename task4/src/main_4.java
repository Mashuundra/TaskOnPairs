import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class main_4 {
    public static void main(String[] args) {
        List<Integer> ids20 = generateIds(20);
        List<Integer> ids50 = generateIds(50);
        List<Integer> ids100 = generateIds(100);

        System.out.println("Последовательная обработка");
        measure("Последовательная 20", new SequentialPipelineProcessor(), ids20);
        measure("Последовательная 50", new SequentialPipelineProcessor(), ids50);
        measure("Последовательная 100", new SequentialPipelineProcessor(), ids100);

        System.out.println("\nФиксированные пулы потоков");
        for (int threads : new int[]{2, 4, 8}) {
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            AsyncPipelineProcessor processor = new AsyncPipelineProcessor(pool);
            measure("Асинхронный fixedPool(" + threads + ") 20", processor, ids20);
            measure("Асинхронный fixedPool(" + threads + ") 50", processor, ids50);
            measure("Асинхронный fixedPool(" + threads + ") 100", processor, ids100);
            pool.shutdown();
            awaitTermination(pool);
        }

        System.out.println("\nКэшируемый пул потоков");
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        AsyncPipelineProcessor cachedProcessor = new AsyncPipelineProcessor(cachedPool);
        measure("Асинхронный cachedPool 20", cachedProcessor, ids20);
        measure("Асинхронный cachedPool 50", cachedProcessor, ids50);
        measure("Асинхронный cachedPool 100", cachedProcessor, ids100);
        cachedPool.shutdown();
        awaitTermination(cachedPool);

        System.out.println("\nОбщий ForkJoinPool");
        Executor commonPool = ForkJoinPool.commonPool();
        AsyncPipelineProcessor commonProcessor = new AsyncPipelineProcessor(commonPool);
        measure("Асинхронный commonPool 20", commonProcessor, ids20);
        measure("Асинхронный commonPool 50", commonProcessor, ids50);
        measure("Асинхронный commonPool 100", commonProcessor, ids100);
    }

    private static List<Integer> generateIds(int count) {
        return IntStream.range(0, count).boxed().collect(Collectors.toList());
    }

    private static void measure(String testName, PipelineProcessor processor, List<Integer> ids) {
        long duration = PerformanceMeasurer.measure(() -> processor.process(ids));
        System.out.printf("%s выполнено за %d ms%n", testName, duration);
    }

    private static void awaitTermination(ExecutorService pool) {
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}