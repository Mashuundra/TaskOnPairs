import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class RetryHandler {
    private final int maxAttempts;
    private final long delayMs;
    private final Executor executor;

    public RetryHandler(int maxAttempts, long delayMs, Executor executor) {
        this.maxAttempts = maxAttempts;
        this.delayMs = delayMs;
        this.executor = executor;
    }

    public CompletableFuture<String> processWithRetry(String rawData) {
        return attempt(rawData, 1);
    }

    private CompletableFuture<String> attempt(String rawData, int attempt) {
        return CompletableFuture.supplyAsync(() -> {
            int threadId = Thread.currentThread().hashCode();
            return UnstableRemoteService.processData(rawData, threadId);
        }, executor).handle((result, ex) -> {
            if (ex == null) {
                return CompletableFuture.completedFuture(result);
            }
            if (attempt < maxAttempts) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(delayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, executor).thenCompose(ignored -> attempt(rawData, attempt + 1));
            } else {
                return CompletableFuture.completedFuture("ERROR");
            }
        }).thenCompose(f -> f);
    }
}