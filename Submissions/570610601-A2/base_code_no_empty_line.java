import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
public class Main {
    private static int codeLines = 0;
    private static int commentLines = 0;
//    public static void setCodeLines(int codeLines) { /*DELETED*/
//        Main.codeLines = codeLines; /*DELETED*/
//    } /*DELETED*/
//    public static int getCommentLines() { /*DELETED*/
//        return commentLines; /*DELETED*/
//    } /*DELETED*/
//    public static void setCommentLines(int commentLines) { /*DELETED*/
//        Main.commentLines = commentLines; /*DELETED*/
//    } /*DELETED*/
    public static void AddCommentLine() {
        commentLines += 1; /*MODIFIED*/
    }
    public static void AddCodeLine() {
        codeLines += 1; /*MODIFIED*/
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
            line = "";
        return line;
        return line;
                ++closeBraces;
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("[USAGE]: src-count.exe <path-to-file>"); // Wrap .jar into .exe, named "src-count.exe"
        } else {
            try {
                List<String> lines = Files.readAllLines(Paths.get(args[0]));
                for (String line : lines) {
                    if (LineIsNotEmpty(line)) { // Line is not empty
                        line = RemoveBaseCodeOperationComment(line); // Remove specific base code comment (such as ADDED, MODIFIED, and DELETED)
                        line = RemoveInStringQuotes(line);
                        line = CheckBlockCommentInLine(line);
                        if (!isBlockOpening) { // This line does not include opening double quotes (for multiple lines block comment)
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
                }
//                System.out.println("-----------------"); /*DELETED*/
                System.out.format("%20s%5d%7s\n", "Code = ", codeLines, " lines"); /*MODIFIED*/
                System.out.format("%20s%5d%7s\n", "Comment = ", commentLines, " lines"); /*MODIFIED*/
                PrintHeaderMessage("> USE OF BASE CODE ", maxFunctionName.length()); /*MODIFIED*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}