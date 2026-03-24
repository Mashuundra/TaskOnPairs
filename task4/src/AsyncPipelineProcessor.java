import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class AsyncPipelineProcessor implements PipelineProcessor {
    private final Executor executor;
    private final RetryHandler retryHandler;

    public AsyncPipelineProcessor(Executor executor) {
        this.executor = executor;
        this.retryHandler = new RetryHandler(3, 100, executor);
    }

    @Override
    public List<String> process(List<Integer> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> UnstableRemoteService.fetchRawData(id), executor)
                        .thenCompose(retryHandler::processWithRetry))
                .collect(Collectors.toCollection(ArrayList::new));

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}