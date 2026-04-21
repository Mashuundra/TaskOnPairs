package rule;

import model.LintError;

import java.util.regex.*;

public class OperatorSpacingRule extends BaseRule {
    private final Pattern noSpacePattern = Pattern.compile("\\b\\w+[=!<>]=?\\w+\\b");

    public OperatorSpacingRule() {
        super("operator-spacing", "Пробелы вокруг операторов");
    }

    @Override
    public LintError check(String line, int lineNumber) {
        if (line.trim().startsWith("//")) return null;

        Matcher m = noSpacePattern.matcher(line);
        if (m.find()) {
            return createError(lineNumber, m.start() + 1,
                    "Отсутствуют пробелы вокруг оператора");
        }
        return null;
    }
}