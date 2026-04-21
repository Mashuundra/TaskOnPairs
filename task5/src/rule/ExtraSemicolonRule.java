package rule;

import model.LintError;

import java.util.regex.*;

public class ExtraSemicolonRule extends BaseRule {
    private final Pattern extraSemicolonPattern = Pattern.compile(
            ";;|^\\s*;\\s*$|;\\s*}|};"
    );

    public ExtraSemicolonRule() {
        super("extra-semicolon", "Запрещены лишние точки с запятой");
    }

    @Override
    public LintError check(String line, int lineNumber) {
        if (line.trim().isEmpty() || line.trim().startsWith("//")) {
            return null;
        }

        Matcher m = extraSemicolonPattern.matcher(line);
        if (m.find()) {
            return createError(lineNumber, m.start() + 1,
                    "Обнаружена лишняя точка с запятой");
        }
        return null;
    }
}