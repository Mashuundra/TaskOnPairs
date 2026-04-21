package model;

public class LintError {
    private final int line;
    private final int column;
    private final String message;
    private final String ruleName;

    public LintError(int line, int column, String message, String ruleName) {
        this.line = line;
        this.column = column;
        this.message = message;
        this.ruleName = ruleName;
    }

    public String getRuleName() { return ruleName; }

    @Override
    public String toString() {
        return String.format("Ошибка в строке %d символе %d: %s",
                line, column, message);
    }
}