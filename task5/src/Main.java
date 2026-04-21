import model.LintError;
import rule.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Linter linter = new Linter();
        linter.addRule(new NoTrailingWhitespaceRule())
                .addRule(new IndentationRule())
                .addRule(new LineLengthRule())
                .addRule(new ExtraSemicolonRule())
                .addRule(new OperatorSpacingRule());

        List<LintError> errors = new ArrayList<>();
        errors.addAll(linter.lint("x = 10   "));
        errors.addAll(linter.lint("  x = 10"));
        errors.addAll(linter.lint("x = " + "a".repeat(81)));
        errors.addAll(linter.lint("x = 10;;"));
        errors.addAll(linter.lint("x=10"));

        for (LintError e : errors) {
            System.out.println(e);
        }
    }
}