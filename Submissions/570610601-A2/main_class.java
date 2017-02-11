public class Main {
    private static int codeLines = 0;
    private static int commentLines = 0;
    private static int numberOfFunctions = 0; /*ADDED*/
    private static int addedLines = 0; /*ADDED*/
    private static int modifiedLines = 0; /*ADDED*/
    private static int deletedLines = 0; /*ADDED*/
    private static List<Integer> functionLines = new ArrayList<Integer>(); /*ADDED*/
    private static List<String> functionNames = new ArrayList<String>(); /*ADDED*/
    private static int openBraces = 0; /*ADDED*/
    private static int closeBraces = 0; /*ADDED*/
    private static int headerLines = 0; /*ADDED*/
    private static int importLines = 0; /*ADDED*/
    public static void AddCommentLine() {
        commentLines += 1; /*MODIFIED*/
    }
    public static void AddCodeLine() {
        codeLines += 1; /*MODIFIED*/
        if (isFunctionOpening) { /*ADDED*/
            functionLines.set(numberOfFunctions - 1, functionLines.get(numberOfFunctions - 1) + 1); /*ADDED*/
        } /*ADDED*/
    }
    public static String CheckAddedLine(String line) { /*ADDED*/
        if (line.matches(".*/\\*ADDED\\*/.*")) { /*ADDED*/
            addedLines += 1; /*ADDED*/
        } /*ADDED*/
        line = line.replaceAll(REGEX_ADDED, ""); /*ADDED*/
        return line; /*ADDED*/
    } /*ADDED*/
    public static String CheckModifiedLine(String line) { /*ADDED*/
        if (line.matches(".*/\\*MODIFIED\\*/.*")) { /*ADDED*/
            modifiedLines += 1; /*ADDED*/
        } /*ADDED*/
        line = line.replaceAll(REGEX_MODIFIED, ""); /*ADDED*/
        return line; /*ADDED*/
    } /*ADDED*/
    public static String CheckDeletedLine(String line) { /*ADDED*/
        if (line.matches(".*/\\*DELETED\\*/.*")) { /*ADDED*/
            deletedLines += 1; /*ADDED*/
        } /*ADDED*/
        line = line.replaceAll(REGEX_DELETED, ""); /*ADDED*/
        return line; /*ADDED*/
    } /*ADDED*/
    private static final String REGEX_STR_QUOTES = "\"(.*?).*?\"";
    private static final String REGEX_LINE_COMMENT = ""
    private static final String REGEX_BLOCK_COMMENT_LINE = "/\\*.+\\*/";
    private static final String REGEX_BLOCK_COMMENT_OPEN = "/\\*.+";
    private static final String REGEX_BLOCK_COMMENT_CLOSE = ".+\\*/";
    private static final String REGEX_ADDED = "/\\*ADDED\\*/"; /*ADDED*/
    private static final String REGEX_MODIFIED = "/\\*MODIFIED\\*/"; /*ADDED*/
    private static final String REGEX_DELETED = "/\\*DELETED\\*/"; /*ADDED*/
    private static final String REGEX_FUNCTION = "\\sp.*\\((.*?)\\).*\\{"; /*ADDED*/
    private static final String REGEX_HEADER = "/" + "/" + "/" + "/" + "/" + "/" + "/" + ".+"; /*ADDED*/
    private static final String REGEX_IMPORT = "^import.*"; /*ADDED*/
    private static boolean isBlockCommentReplace = false;
    private static boolean isBlockOpening = false;
    private static boolean isFunctionOpening = false; /*ADDED*/
    private static boolean isHeaderOpening = false; /*ADDED*/
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
        return !line.trim().isEmpty();
    }
    private static String RemoveBaseCodeOperationComment(String line) { /*ADDED*/
        line = CheckAddedLine(line); /*ADDED*/
        line = CheckModifiedLine(line); /*ADDED*/
        line = CheckDeletedLine(line); /*ADDED*/
        return line; /*ADDED*/
    } /*ADDED*/
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
        return line.contains("")
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
            CheckFunctionOpen(line); /*ADDED*/
        }
    }
    private static void AddCommentLineIfBlockOpening(String line) {
        if (IsBlockOpening()) {
            AddCommentLine();
        }
    }
    private static String ClearHeader(String line) { /*ADDED*/
        if (line.matches(REGEX_HEADER)) { /*ADDED*/
            ++headerLines; /*ADDED*/
            isHeaderOpening = !isHeaderOpening; /*ADDED*/
        } /*ADDED*/
        else if (isHeaderOpening) { /*ADDED*/
            ++headerLines; /*ADDED*/
            line = "";
        } /*ADDED*/
        line = line.replaceAll(REGEX_HEADER, ""); /*ADDED*/
        return line;
    } /*ADDED*/
    private static String ClearImport(String line) { /*ADDED*/
        if (line.matches(REGEX_IMPORT)) { /*ADDED*/
            ++importLines; /*ADDED*/
        } /*ADDED*/
        line = line.replaceAll(REGEX_IMPORT, ""); /*ADDED*/
        return line;
    } /*ADDED*/
    private static void CheckFunctionOpen(String line) { /*ADDED*/
        String tmp = line.replaceAll(REGEX_FUNCTION, "!@#THISISFUNCTION#@!"); /*ADDED*/
        if (tmp.contains("!@#THISISFUNCTION#@!")) { /*ADDED*/
            ++numberOfFunctions; /*ADDED*/
            isFunctionOpening = true; /*ADDED*/
            if (line.contains("{")) { /*ADDED*/
                ++openBraces; /*ADDED*/
            } /*ADDED*/
            line = line.replaceAll("\\(.*", ""); /*ADDED*/
            String[] splited = line.split("\\s+"); /*ADDED*/
            functionNames.add(splited[splited.length - 1]); /*ADDED*/
            functionLines.add(1); /*ADDED*/
        } else { /*ADDED*/
            if (line.contains("{") && isFunctionOpening) { /*ADDED*/
                ++openBraces; /*ADDED*/
            } /*ADDED*/
            if (line.contains("}") && isFunctionOpening) { /*ADDED*/
                ++closeBraces;
            } /*ADDED*/
            if (openBraces == closeBraces) { /*ADDED*/
                openBraces = closeBraces = 0; /*ADDED*/
                isFunctionOpening = false; /*ADDED*/
            } /*ADDED*/
        } /*ADDED*/
    } /*ADDED*/
    private static void PrintHeaderMessage(String input, int maxFunctionNameLength) { /*ADDED*/
        System.out.print(input); /*ADDED*/
        for (int i = 0; i < maxFunctionNameLength + 20 - input.length(); i++) { /*ADDED*/
            System.out.print("-"); /*ADDED*/
        } /*ADDED*/
        System.out.println(); /*ADDED*/
    } /*ADDED*/
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("[USAGE]: src-count.exe <path-to-file>"); 
        } else {
            try {
                List<String> lines = Files.readAllLines(Paths.get(args[0]));
                for (String line : lines) {
                    if (LineIsNotEmpty(line)) { 
                        line = RemoveBaseCodeOperationComment(line); 
                        line = ClearHeader(line); /*ADDED*/
                        line = ClearImport(line); /*ADDED*/
                        line = RemoveInStringQuotes(line);
                        line = CheckBlockCommentInLine(line);
                        System.out.println(line);
                        if (!isBlockOpening) { 
                            if ((CheckLineComment(line)) || (IsBlockCommentReplace())) { 
                                AddCommentLine();
                                line = RemoveLineComment(line);
                                AddCodeLineIfNotEmpty(line);
                            } else { 
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
                String maxFunctionName = Collections.max(functionNames, Comparator.comparing(s -> s.length())); /*ADDED*/
                PrintHeaderMessage("> FUNCTION LIST ", maxFunctionName.length()); /*ADDED*/
                String formatOutput = "%" + (maxFunctionName.length() > "[Function Name]".length() ? maxFunctionName.length() : "[Function Name]".length() ) + "s"; /*ADDED*/
                System.out.format(formatOutput + "\n", "[Function Name]"); /*ADDED*/
                for (int i = 0; i < functionNames.size(); i++) { /*ADDED*/
                    System.out.format(formatOutput + "%10d%10s\n", functionNames.get(i), functionLines.get(i), "lines"); /*ADDED*/
                }
                PrintHeaderMessage("> HEADER ", maxFunctionName.length()); /*ADDED*/
                System.out.format("%20s%5d%7s\n", "Header comment = ", headerLines, " lines"); /*ADDED*/
                System.out.format("%20s%5d%7s\n", "Import = ", importLines, " lines"); /*ADDED*/
                PrintHeaderMessage("> PROGRAM ", maxFunctionName.length()); /*ADDED*/
                System.out.format("%20s%5d%7s\n", "Code = ", codeLines, " lines"); /*MODIFIED*/
                System.out.format("%20s%5d%7s\n", "Comment = ", commentLines, " lines"); /*MODIFIED*/
                System.out.format("%20s%5d%7s\n", "Total = ", (codeLines + commentLines), " lines"); /*ADDED*/
                PrintHeaderMessage("> USE OF BASE CODE ", maxFunctionName.length()); /*MODIFIED*/
                System.out.format("%20s%5d%7s\n", "Added = ", addedLines, " lines"); /*ADDED*/
                System.out.format("%20s%5d%7s\n", "Modified = ", modifiedLines, " lines"); /*ADDED*/
                System.out.format("%20s%5d%7s\n", "Deleted = ", deletedLines, " lines"); /*ADDED*/
                PrintHeaderMessage("", maxFunctionName.length()); /*ADDED*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}