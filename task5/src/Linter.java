import java.util.*;
import model.LintError;
import rule.LinterRule;

public class Linter {
    private final List<LinterRule> rules = new ArrayList<>();

    public Linter() {}

    public Linter addRule(LinterRule rule) {
        rules.add(rule);
        return this;
    }

    public List<LintError> lint(String code) {
        List<LintError> errors = new ArrayList<>();
        String[] lines = code.split("\\R");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (LinterRule rule : rules) {
                LintError error = rule.check(line, i + 1);
                if (error != null) {
                    errors.add(error);
                }
            }
        }
        return errors;
    }
}