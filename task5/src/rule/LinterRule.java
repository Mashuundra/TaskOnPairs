package rule;

import model.LintError;

public interface LinterRule {
    LintError check(String line, int lineNumber);
    String getName();
    String getDescription();
}