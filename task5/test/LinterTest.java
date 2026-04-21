import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import model.LintError;
import rule.*;

class LinterTest {
    private Linter linter;

    @BeforeEach
    void setUp() {
        linter = new Linter();
        linter.addRule(new NoTrailingWhitespaceRule())
                .addRule(new IndentationRule())
                .addRule(new LineLengthRule())
                .addRule(new ExtraSemicolonRule())
                .addRule(new OperatorSpacingRule());
    }

    @Test
    void testNoTrailingWhitespace_NoError() {
        String code = "x = 10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("no-trailing-whitespace")));
    }

    @Test
    void testNoTrailingWhitespace_SpacesAtEnd() {
        String code = "x = 10   ";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("no-trailing-whitespace")));
    }

    @Test
    void testNoTrailingWhitespace_TabAtEnd() {
        String code = "x = 10\t\t";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("no-trailing-whitespace")));
    }

    @Test
    void testIndentation_NoError() {
        String code = "    x = 10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("indentation")));
    }

    @Test
    void testIndentation_TwoSpaces() {
        String code = "  x = 10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("indentation")));
    }

    @Test
    void testIndentation_SixSpaces() {
        String code = "      x = 10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("indentation")));
    }

    @Test
    void testIndentation_Tab() {
        String code = "\tx = 10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("indentation")));
    }

    @Test
    void testLineLength_NoError() {
        String code = "x = " + "a".repeat(76);
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("line-length")));
    }

    @Test
    void testLineLength_WithError() {
        String code = "x = " + "a".repeat(77);
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("line-length")));
    }

    @Test
    void testExtraSemicolon_NoError() {
        String code = "x = 10\ny = 20";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("extra-semicolon")));
    }

    @Test
    void testExtraSemicolon_DoubleSemicolon() {
        String code = "x = 10;;";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("extra-semicolon")));
    }

    @Test
    void testExtraSemicolon_EmptyStatement() {
        String code = "x = 10\n;\ny = 20";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("extra-semicolon")));
    }

    @Test
    void testExtraSemicolon_AfterBrace() {
        String code = "if (x > 0) {\n    result = x\n};";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("extra-semicolon")));
    }

    @Test
    void testOperatorSpacing_NoError() {
        String code = "x = 10\ny == 20\nz != 30";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().noneMatch(e -> e.getRuleName().equals("operator-spacing")));
    }

    @Test
    void testOperatorSpacing_NoSpaceAroundEquals() {
        String code = "x=10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("operator-spacing")));
    }

    @Test
    void testOperatorSpacing_NoSpaceAroundDoubleEquals() {
        String code = "x==10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("operator-spacing")));
    }

    @Test
    void testOperatorSpacing_NoSpaceAroundNotEquals() {
        String code = "x!=10";
        List<LintError> errors = linter.lint(code);
        assertTrue(errors.stream().anyMatch(e -> e.getRuleName().equals("operator-spacing")));
    }
}