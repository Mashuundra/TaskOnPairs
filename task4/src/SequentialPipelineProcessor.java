import java.util.ArrayList;
import java.util.List;

public class SequentialPipelineProcessor implements PipelineProcessor {
    @Override
    public List<String> process(List<Integer> ids) {
        List<String> results = new ArrayList<>();
        for (int id : ids) {
            try {
                String raw = UnstableRemoteService.fetchRawData(id);
                String processed = UnstableRemoteService.processData(raw, Thread.currentThread().hashCode());
                results.add(processed);
            } catch (RuntimeException e) {
                results.add("ERROR");
            }
        }
        return results;
    }
}