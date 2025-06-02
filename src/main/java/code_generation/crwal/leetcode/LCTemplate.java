package code_generation.crwal.leetcode;

import code_generation.contest.ParseCodeDefaultTemplate;

/**
 * Specialized template parser for LeetCode problems with Java code detection.
 * Extends {@link ParseCodeDefaultTemplate} to provide LeetCode-specific Java code parsing.
 * @author wuxin0011
 * @since 1.0
 */
public class LCTemplate extends ParseCodeDefaultTemplate {

    /**
     * Default pattern for identifying Java code blocks in LeetCode responses.
     * Matches the JSON structure containing Java code snippets.
     */
    public static final String JAVA_CODE_PATTERN = "\"langSlug\":\"java\",\"code\":";

    /**
     * Constructs a new LCTemplate with the default Java code pattern.
     */
    public LCTemplate() {
        this(JAVA_CODE_PATTERN);
    }

    /**
     * Constructs a new LCTemplate with a custom code pattern.
     *
     * @param startFlag The pattern string used to identify the beginning of code blocks.
     *                  If null, will use the default {@link #JAVA_CODE_PATTERN}.
     */
    public LCTemplate(String startFlag) {
        super(startFlag != null ? startFlag : JAVA_CODE_PATTERN);
    }
}
