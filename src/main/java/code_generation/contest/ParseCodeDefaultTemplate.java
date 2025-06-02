package code_generation.contest;

import code_generation.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link ParseCodeTemplate} for parsing and processing code templates.
 * Handles Java code extraction, class/method identification, and template generation.
 * @author: wuxin0011
 * @since 1.0
 */
public class ParseCodeDefaultTemplate implements ParseCodeTemplate {

    /**
     * Default class names to search for in code templates.
     */
    private final static String[] defaultNames = {"Solution"};

    /**
     * Default language tag for code extraction.
     */
    private final static String LANG = "java";

    /**
     * Special identifier for constructor methods.
     */
    public final static String ConstructorClass = ParseCodeInfo.ConstructorClass;

    /**
     * Comment start delimiter.
     */
    public static final String leftFlag = "/*";

    /**
     * Comment end delimiter.
     */
    public static final String rightFlag = "*/";

    /**
     * Container for parsed code information.
     */
    public ParseCodeInfo info;

    /**
     * Raw input code to be parsed.
     */
    public String input;

    /**
     * Flag marking the start of relevant code section.
     */
    public String startFlag;

    /**
     * Flag indicating whether to use new handler logic.
     */
    public boolean isNewHandler;

    /**
     * Constructs a parser with default start flag.
     *
     * @param startFlag the marker indicating start of relevant code
     */
    public ParseCodeDefaultTemplate(String startFlag) {
        this("", startFlag, null);
    }

    /**
     * Constructs a parser with expected class names.
     *
     * @param startFlag the marker indicating start of relevant code
     * @param expectClassNames array of expected class names to search for
     */
    public ParseCodeDefaultTemplate(String startFlag, String[] expectClassNames) {
        this("", startFlag, expectClassNames);
    }

    /**
     * Primary constructor with full configuration.
     *
     * @param input the raw code input to parse
     * @param startFlag the marker indicating start of relevant code
     * @param expectClassNames array of expected class names (uses defaults if null/empty)
     */
    public ParseCodeDefaultTemplate(String input, String startFlag, String[] expectClassNames) {
        expectClassNames = (expectClassNames == null || expectClassNames.length == 0)
                ? defaultNames : expectClassNames;
        this.info = new ParseCodeInfo(expectClassNames);
        this.startFlag = startFlag;
        this.info.setOrigin(input);
    }

    /**
     * Parses code template with new handler flag.
     *
     * @param input the code to parse
     * @param startFlag the marker indicating start of relevant code
     * @param isNewHandler whether to use new handler logic
     * @return parsed code information, or null on failure
     */
    public ParseCodeInfo parseCodeTemplate(String input, String startFlag, boolean isNewHandler) {
        this.isNewHandler = isNewHandler;
        return parseCodeTemplate(input, startFlag);
    }

    /**
     * Parses code template with specified start flag.
     *
     * @param input the code to parse
     * @param startFlag the marker indicating start of relevant code
     * @return parsed code information, or null on failure
     */
    public ParseCodeInfo parseCodeTemplate(String input, String startFlag) {
        try {
            this.input = input;
            this.startFlag = startFlag;
            this.info.setOrigin(input);
            init();
            return this.info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parses code template using stored start flag.
     *
     * @param input the code to parse
     * @return parsed code information, or null on failure
     */
    @Override
    public ParseCodeInfo parseCodeTemplate(String input) {
        return this.parseCodeTemplate(input, this.startFlag);
    }

    /**
     * Executes the full parsing process:
     * 1. Identifies class name
     * 2. Extracts method signatures
     * 3. Determines method names
     */
    public void parseProcess() {
        Objects.requireNonNull(this.info, "Parser not properly initialized");
        setClassName();
        setMethod();
        setMethodName();
    }

    /**
     * Initializes parsing by extracting language-specific code.
     */
    public void init() {
        this.input = getLangCode(LANG, saveLangCode(this.input));
        parseProcess();
    }

    /**
     * Identifies and sets the class name from the input code.
     */
    public void setClassName() {
        String in = this.input;
        // First check for known non-constructor classes
        for (String name : this.info.getNoConstructorName()) {
            int i = StringUtils.kmpSearch(in, name);
            if (i != -1) {
                this.info.setClassName(name);
                this.info.setConstructor(false);
                return;
            }
        }

        // Fall back to pattern matching for class declaration
        Pattern compile = Pattern.compile("(public|private)?\\s+class\\s+(\\S+)\\s+\\{(.*?)");
        Matcher matcher = compile.matcher(in);
        String className = "";

        if (matcher.find()) {
            className = matcher.group();
        } else {
            // Fallback for minimal class declarations
            int i = StringUtils.kmpSearch(in, "{");
            if (i != -1) {
                className = in.substring(0, i - 1);
            }
        }

        // Clean up the extracted class name
        className = className.replaceAll("[\"'{}]", "")
                .replaceAll("class|public|private|protected", "")
                .trim();

        this.info.setClassName(className);
        this.info.setConstructor(true);
    }

    /**
     * Extracts and processes method signatures from the code.
     */
    public void setMethod() {
        String methodStr = this.input;
        String className = this.info.getClassName();

        // Isolate method section by removing class declaration
        int classPos = StringUtils.kmpSearch(methodStr, className);
        if (classPos != -1) {
            methodStr = methodStr.substring(classPos + className.length());
        }

        // Extract method bodies using brace counting
        int deep = 0;
        StringBuilder sb = null;
        for (int i = 0; i < methodStr.length(); i++) {
            char c = methodStr.charAt(i);
            if (c == '{') {
                deep++;
                if (deep == 1 && sb == null) {
                    sb = new StringBuilder();
                } else if (deep >= 2 && sb != null) {
                    sb.append(c);
                }
            } else if (c == '}') {
                deep--;
                if (deep == 0) {
                    break;
                }
                if (sb != null) {
                    sb.append(c);
                }
            } else {
                if (sb != null) sb.append(c);
            }
        }

        methodStr = sb != null ? sb.toString() : null;
        methodStr = StringUtils.handerMethodString(methodStr);
        methodStr = handlerReturnType(methodStr);
        methodStr = removeComment(methodStr);
        this.info.setMethod(methodStr);
    }

    /**
     * Determines and sets method names, handling constructor case specially.
     */
    public void setMethodName() {
        if (this.info.isConstructor()) {
            this.info.setMethodName(ConstructorClass);
        } else {
            this.info.setMethodName(StringUtils.getMethodName(this.info.getMethod()));
        }
    }

    /**
     * Processes return types in method signatures.
     *
     * @param methodStr the raw method string to process
     * @return processed method string with return statements
     */
    public String handlerReturnType(String methodStr) {
        Objects.requireNonNull(methodStr, "Method string cannot be null");

        String[] split = methodStr.split("\\}");
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("(protected|private|public)\\s+([a-zA-Z<>]+)\\s+(\\w+)\\((.*)\\)");

        for (String s : split) {
            if (StringUtils.isEmpty(s)) continue;

            if(s.contains(this.info.getClassName())) {
                s += buildReturnType("");
                result.append(s).append("\n\n");
                continue;
            }

            String returnStmt = "";
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                returnStmt = buildReturnType(matcher.group(2));
            } else {
                StringBuilder returnType = new StringBuilder();
                int deep = 0;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c == '<') {
                        deep++;
                        returnType.append(c);
                    } else if (c == '>') {
                        deep--;
                        returnType.append(c);
                        if (deep == 0) break;
                    } else if (c == ' ' && deep == 0) {
                        break;
                    } else {
                        returnType.append(c);
                    }
                }
                returnStmt = buildReturnType(returnType.toString());
            }
            result.append(s).append(returnStmt).append("\n\n");
        }
        return result.toString();
    }

    /**
     * Builds standard method termination string.
     *
     * @param s the return statement to include
     * @return formatted method termination
     */
    private String build(String s) {
        boolean isConstructor = this.info.isConstructor();
        final String padding = isConstructor ? "\t  " : "\t";
        return String.format("\n\n%s       %s \n%s}",
                isConstructor ? "\t  " : " ", s, padding);
    }

    /**
     * Generates appropriate return statement based on return type.
     *
     * @param returnType the detected return type
     * @return formatted return statement
     */
    public String buildReturnType(String returnType) {
        returnType = returnType.trim();
        switch (returnType) {
            case "byte": case "Byte":
            case "short": case "Short":
            case "int": case "Integer":
            case "double": case "Double":
            case "float": case "Float":
            case "long": case "Long":
            case "BigInteger": case "BigDecimal":
            case "AtomicLong": case "AtomicInger":
                return build("return 0;");
            case "boolean":
                return build("return false;");
            case "char": case "Character":
                return build("return ' ';");
            case "void": case "": // constructor case
                return build("");
            default:
                return build("return null;");
        }
    }

    /**
     * Removes block comments from method strings.
     *
     * @param methodStr the method string to clean
     * @return cleaned method string
     */
    public static String removeComment(String methodStr) {
        int commentLeft = StringUtils.kmpSearch(methodStr, leftFlag);
        int commentRight = StringUtils.kmpSearch(methodStr, rightFlag);
        if (commentLeft != -1 && commentRight != -1) {
            methodStr = methodStr.substring(rightFlag.length() + commentRight);
        }
        return methodStr;
    }

    /**
     * Finds handler code block by scanning backwards from given position.
     *
     * @param codeInfo the full code to search
     * @param ed the end position to start scanning from
     * @return extracted handler code block
     */
    public static String findNewHandlerCode(String codeInfo, int ed) {
        int deep = 0;
        StringBuilder sb = new StringBuilder();
        boolean find = false;
        final String classStr = "class";

        for (int i = ed; i >= classStr.length(); i--) {
            char c = codeInfo.charAt(i);
            if (c == '}') {
                deep++;
                find = true;
            } else if (c == '{') {
                deep--;
                find = true;
            }
            sb.append(c);
            if (codeInfo.startsWith(classStr, i)) {
                break;
            }
        }
        return sb.reverse().toString();
    }

    /**
     * Extracts language-specific code snippets from JSON input.
     *
     * @param codeInfo JSON string containing code snippets
     * @return list of language-specific code blocks
     */
    public static List<String> saveLangCode(String codeInfo) {
        String codes = StringUtils.jsonStrGetValueByKey(codeInfo, "codeSnippets", true, false);
        codes = codes.substring(StringUtils.kmpSearch(codes, "["));
        List<String> langsCode = new ArrayList<>();
        int deep = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < codes.length(); i++) {
            char c = codes.charAt(i);
            if ((c == '[' || c == ']') && deep == 0) continue;

            if (c == '{') {
                deep++;
            } else if (c == '}' && deep > 0) {
                sb.append("}");
                deep--;
            }

            if (deep > 0) sb.append(c);

            if (deep == 0 && sb.length() > 0) {
                langsCode.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        return langsCode;
    }

    /**
     * Gets code for specified language from extracted snippets.
     *
     * @param langTag the language identifier (e.g., "java")
     * @param langCodes list of language-specific code blocks
     * @return the matching code block, or null if not found
     */
    public static String getLangCode(String langTag, List<String> langCodes) {
        for (String codeInfo : langCodes) {
            String langSlug = StringUtils.jsonStrGetValueByKey(codeInfo, "langSlug");
            if (StringUtils.isEmpty(langSlug)) continue;

            langSlug = langSlug.replaceAll("[{}\"\\\\']", "").trim();
            if (langSlug.equalsIgnoreCase(langTag)) {
                return StringUtils.jsonStrGetValueByKey(codeInfo, "code");
            }
        }
        return null;
    }
}