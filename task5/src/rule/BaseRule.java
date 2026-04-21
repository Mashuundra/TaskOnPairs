package rule;

import model.LintError;

public abstract class BaseRule implements LinterRule {
    protected final String name;
    protected final String description;

    public BaseRule(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    protected LintError createError(int line, int col, String msg) {
        return new LintError(line, col, msg, this.name);
    }
}