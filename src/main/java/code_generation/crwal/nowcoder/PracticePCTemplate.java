package code_generation.crwal.nowcoder;

import code_generation.contest.ParseCodeDefaultTemplate;
import code_generation.contest.ParseCodeInfo;

/**
 * A specialized code template parser for NowCoder practice problems in Java.
 * Extends the basic ParseCodeDefaultTemplate to handle NowCoder-specific code template formats.
 * @author wuxin0011
 * @since 1.0
 */
public class PracticePCTemplate extends ParseCodeDefaultTemplate {

    /**
     * Default pattern identifying Java code templates in NowCoder practice problems
     */
    public static final String JAVA_CODE_PATTERN = "\"langName\":\"Java\",\"template\"";

    /**
     * Constructs a PracticePCTemplate with the default Java code pattern.
     */
    public PracticePCTemplate() {
        this(JAVA_CODE_PATTERN);
    }

    /**
     * Constructs a PracticePCTemplate with a custom start pattern.
     * @param startFlag The pattern string used to identify the beginning of the code template
     */
    public PracticePCTemplate(String startFlag) {
        super(startFlag);
    }

    /**
     * Parses the code template from input string and processes it for NowCoder practice problems.
     * Removes the "// write code here" placeholder comment from the method body.
     *
     * @param input The raw input string containing the code template
     * @param startFlag The pattern marking the start of the code template
     * @return ParseCodeInfo object containing the parsed method signature and body
     * @throws NullPointerException if either input or startFlag is null
     *
     * @see ParseCodeDefaultTemplate#parseCodeTemplate(String, String)
     */
    @Override
    public ParseCodeInfo parseCodeTemplate(String input, String startFlag) {
        ParseCodeInfo parseCodeInfo = super.parseCodeTemplate(input, startFlag);
        String method = parseCodeInfo.getMethod();
        method = method.replace("// write code here","");
        parseCodeInfo.setMethod(method);
        return parseCodeInfo;
    }
}