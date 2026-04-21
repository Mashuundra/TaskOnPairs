package rule;

import model.LintError;

import java.util.regex.*;

public class LineLengthRule extends BaseRule {
    private final int maxLength = 80;
    private final Pattern longLinePattern;

    public LineLengthRule() {
        super("line-length", "Длина строки не более 80 символов");
        this.longLinePattern = Pattern.compile("^.{" + (maxLength + 1) + ",}$");
    }

    @Override
    public LintError check(String line, int lineNumber) {
        Matcher m = longLinePattern.matcher(line);
        if (m.find()) {
            return createError(lineNumber, maxLength + 1,
                    String.format("Строка длиной %d символов (максимум %d)", line.length(), maxLength));
        }
        return null;
    }
}