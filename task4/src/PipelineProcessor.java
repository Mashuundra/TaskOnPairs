import java.util.List;

public interface PipelineProcessor {
    List<String> process(List<Integer> ids);
}