import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static int codeLines = 0;
    private static int commentLines = 0;

    public static int getCodeLines() {
        return codeLines;
    }

    public static void setCodeLines(int codeLines) {
        Main.codeLines = codeLines;
    }

    public static void AddCodeLine() {
        setCodeLines(getCodeLines() + 1);
    }

    public static int getCommentLines() {
        return commentLines;
    }

    public static void setCommentLines(int commentLines) {
        Main.commentLines = commentLines;
    }

    public static void AddCommentLine() {
        setCommentLines(getCommentLines() + 1);
    }

    private static final String REGEX_STR_QUOTES = "\"(.*?).*?\"";
    private static final String REGEX_LINE_COMMENT = "//..*";
    private static final String REGEX_BLOCK_COMMENT_LINE = "/\\*.+\\*/";
    private static final String REGEX_BLOCK_COMMENT_OPEN = "/\\*.+";
    private static final String REGEX_BLOCK_COMMENT_CLOSE = ".+\\*/";

    private static boolean isBlockCommentReplace = false;
    private static boolean isBlockOpening = false;

    public static boolean IsBlockCommentReplace() {
        return isBlockCommentReplace;
    }

    public static void setIsBlockCommentReplace(boolean isBlockCommentReplace) {
        Main.isBlockCommentReplace = isBlockCommentReplace;
    }

    public static boolean IsBlockOpening() {
        return isBlockOpening;
    }

    public static void setIsBlockOpening(boolean isBlockOpening) {
        Main.isBlockOpening = isBlockOpening;
    }

    private static boolean LineIsNotEmpty(String line) {
//        return line.trim().length() > 0;
        return !line.trim().isEmpty();
    }

    private static String RemoveInStringQuotes(String line) {
        return line.replaceAll(REGEX_STR_QUOTES, "");
    }

    private static String CheckBlockCommentInLine(String line) {
        line = line.replaceAll(REGEX_BLOCK_COMMENT_LINE, "/*REPLACED_BY_CHECKER*/");
        setIsBlockCommentReplace(line.contains("/*REPLACED_BY_CHECKER*/"));
        line = line.replaceAll(REGEX_BLOCK_COMMENT_LINE, "");
        return line;
    }

    private static boolean CheckLineComment(String line) {
        return line.contains("//");
    }

    private static String RemoveLineComment(String line) {
        return line.replaceAll(REGEX_LINE_COMMENT, "");
    }

    private static String CheckOpenMultipleLineBlockComment(String line) {
        line = line.replaceAll(REGEX_BLOCK_COMMENT_OPEN, "<--MULTIPLELINE--");
        setIsBlockOpening(line.contains("<--MULTIPLELINE--"));
        return line.replaceAll("<--MULTIPLELINE--", "");
    }

    private static String CheckBetweenMultipleLineBlockComment(String line) {
        return (IsBlockOpening() ? "" : line);
    }

    private static String CheckCloseMultipleLineBlockComment(String line) {
        line = line.replaceAll(REGEX_BLOCK_COMMENT_CLOSE, "--MULTIPLELINE-->");
        setIsBlockOpening(!line.contains("--MULTIPLELINE-->"));
        return line.replaceAll("--MULTIPLELINE-->", "");
    }

    private static void AddCodeLineIfNotEmpty(String line) {
        if (LineIsNotEmpty(line)) {
            AddCodeLine();
        }
    }

    private static void AddCommentLineIfBlockOpening(String line) {
        if (IsBlockOpening()) {
            AddCommentLine();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("[USAGE]: src-count.exe <path-to-file>");
        } else {
            try {
                List<String> lines = Files.readAllLines(Paths.get(args[0]));
                for (String line : lines) {
                    if (LineIsNotEmpty(line)) { // Line is not empty
                        line = RemoveInStringQuotes(line);
                        line = CheckBlockCommentInLine(line);
                        if (!isBlockOpening) {
                            if ((CheckLineComment(line)) || (IsBlockCommentReplace())) { // Comment line = Check comment // and /**/
                                AddCommentLine();
                                line = RemoveLineComment(line);
                                AddCodeLineIfNotEmpty(line);
                            } else { // Code line
                                line = CheckOpenMultipleLineBlockComment(line);
                                AddCommentLineIfBlockOpening(line);
                                AddCodeLineIfNotEmpty(line);
                            }
                        } else {
                            line = CheckCloseMultipleLineBlockComment(line);
                            line = CheckBetweenMultipleLineBlockComment(line);
                            AddCommentLine();
                            AddCodeLineIfNotEmpty(line);
                        }
                    }
                }
                System.out.println("-----------------");
                System.out.println("Code = " + getCodeLines() + " lines");
                System.out.println("Comment = " + getCommentLines() + " lines");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}