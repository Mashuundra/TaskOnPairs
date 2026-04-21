package rule;

import model.LintError;

import java.util.regex.*;

public class NoTrailingWhitespaceRule extends BaseRule {
    private final Pattern pattern = Pattern.compile("[ \\t]+$");

    public NoTrailingWhitespaceRule() {
        super("no-trailing-whitespace", "Нет пробелов в конце строк");
    }

    @Override
    public LintError check(String line, int lineNumber) {
        Matcher m = pattern.matcher(line);
        if (m.find()) {
            return createError(lineNumber, m.start() + 1, "Лишние пробелы в конце строки");
        }
        return null;
    }
}