package rule;

import model.LintError;

import java.util.regex.*;

public class IndentationRule extends BaseRule {
    private final Pattern mixedPattern = Pattern.compile("^(\\t+)( +)|( +)(\\t+)");
    private final Pattern spacePattern = Pattern.compile("^( +)");
    private final Pattern tabPattern = Pattern.compile("^(\\t+)");

    public IndentationRule() {
        super("indentation", "Отступы должны быть единообразными");
    }

    @Override
    public LintError check(String line, int lineNumber) {
        if (line.trim().isEmpty()) return null;

        Matcher mixedMatcher = mixedPattern.matcher(line);
        if (mixedMatcher.find()) {
            return createError(lineNumber, 1, "Смешанные отступы (пробелы и табуляции)");
        }

        Matcher spaceMatcher = spacePattern.matcher(line);
        if (spaceMatcher.find()) {
            int spaces = spaceMatcher.group(1).length();
            if (spaces % 4 != 0) {
                return createError(lineNumber, 1,
                        String.format("Отступ из %d пробелов не кратен 4", spaces));
            }
        }

        return null;
    }
}