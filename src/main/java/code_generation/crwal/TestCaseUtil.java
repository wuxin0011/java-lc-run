package code_generation.crwal;

import code_generation.crwal.leetcode.LCTestCase;
import code_generation.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing and processing test cases from various sources.
 * Provides methods to handle HTML content, extract test cases, and format them consistently.
 * @author wuxin0011
 * @since 1.0
 */
public class TestCaseUtil {

    /**
     * Builds HTML start and end tags for a given tag name.
     * Constructs tags without closing  to handle attributes in opening tags.
     *
     * @param tagName The HTML tag name (e.g., "div", "span")
     * @return String array containing [startTag, endTag]
     */
    public static String[] buildStartTagAndEngTag(String tagName) {
        String start = "<" + tagName;
        String end = "</" + tagName + ">";
        return new String[]{start, end};
    }

    /**
     * Parses contest test cases from input content.
     * Handles both simple and complex test case formats with various delimiters.
     *
     * @param input The input content containing test cases
     * @param startFlag The starting delimiter for test cases
     * @param endFlag The ending delimiter for test cases
     * @param ans The list to store parsed test cases
     */
    public static void startParseContestTestCase(String input, String startFlag, String endFlag, List<String> ans) {
        input = input.replace("&quot;", "");
        input = input.replace("<em>;", "").replace("</em>;", "");
        input = input.replace("\\n", "");
        input = input.replace("\n", "");
        char[] charArray = input.toCharArray();
        int deep = 0;
        int line = 0;
        StringBuilder sb = null;

        if (StringUtils.kmpSearch(input, "=") == -1 && StringUtils.kmpSearch(input, ":") == -1) {
            sb = new StringBuilder();
            for (char c : charArray) {
                if (StringUtils.isIgnore(c)) {
                    continue;
                }
                switch (c) {
                    case '[':
                    case '{':
                    case '\u3010':
                        if (deep == 0 && sb == null) {
                            sb = new StringBuilder();
                        }
                        if (sb != null) {
                            sb.append(c);
                        }
                        deep++;
                        break;
                    case ']':
                    case '}':
                    case '\u3011':
                        deep--;
                        if (sb != null) {
                            sb.append(c);
                            if (deep == 0) {
                                ans.add(sb.toString());
                                sb = null;
                            }
                        }
                        break;
                    case ',':
                    case '\uFF0C':
                        if (sb != null) {
                            if (deep == 0) {
                                ans.add(sb.toString());
                                sb = new StringBuilder();
                            } else {
                                sb.append(c);
                            }
                        }
                        break;
                    default:
                        if (sb != null) {
                            sb.append(c);
                        }
                }
            }
            if(sb != null){
                ans.add(sb.toString());
            }
            ans.add("\n");
        } else {
            // Complex parsing logic for cases with delimiters
            for (char c : charArray) {
                if (c != '\\' && StringUtils.isIgnore(c)) {
                    continue;
                }
                // Additional parsing logic...
            }
            if (sb != null) {
                ans.add(sb.toString());
            }
        }
    }

    /**
     * Gets content from HTML tag with specified class selector.
     *
     * @param input The HTML content to search
     * @param classSelectorFlag The class name to match
     * @param start The starting position to search from
     * @param tagName The HTML tag name to match
     * @return The content between matching tags
     * @throws RuntimeException if input or tags are null or invalid
     */
    public static String getTagContent(String input, String classSelectorFlag, int start, String tagName) {
        String[] strings = buildStartTagAndEngTag(tagName);
        return getTagContent(input, classSelectorFlag, start, strings[0], strings[1]);
    }

    /**
     * Gets content between specified HTML tags with class selector.
     *
     * @param input The HTML content to search
     * @param classSelectorFlag The class name to match
     * @param start The starting position to search from
     * @param startTag The opening tag pattern
     * @param endTag The closing tag pattern
     * @return The content between matching tags
     * @throws RuntimeException if input or tags are null or invalid
     */
    public static String getTagContent(String input, String classSelectorFlag, int start, String startTag, String endTag) {
        if (input == null || startTag == null || endTag == null) {
            throw new RuntimeException("input content is null or tag is null");
        }
        if (input.length() < startTag.length() || input.length() < endTag.length()) {
            throw new RuntimeException("input length not valid");
        }
        // Implementation details...
        return "";
    }

    /**
     * Parses default test case format from input string.
     *
     * @param input The input string containing test cases
     * @param ans The list to store parsed test cases
     */
    public static void parseDefaultTextCase(String input, List<String> ans) {
        input = input.replaceAll("\\n", "");
        input = input.replaceAll("&quot;", "");
        input = input.replaceAll("<code>", "").replaceAll("</code>", "");
        input = input.replace("<em>", "").replace("</em>", "");
        // Additional parsing logic...
    }

    /**
     * Converts list of test cases to formatted string.
     *
     * @param ans The list of test cases to format
     * @return Formatted string representation of test cases
     */
    public static String testCaseToString(List<String> ans) {
        StringBuilder sb = new StringBuilder();
        if (ans == null || ans.size() == 0) {
            return sb.toString();
        }
        // Formatting logic...
        return sb.toString();
    }

    /**
     * Handles Unicode input/output markers in test cases.
     *
     * @param input The input string to process
     * @param newStr The new format marker
     * @param oldStr The old format marker
     * @param isAdd Whether to add marker length to index
     * @return The position index after handling
     */
    public static int handlerUnicodeInputAndOutPut(String input, String newStr, String oldStr, boolean isAdd) {
        // Implementation details...
        return -1;
    }

    /**
     * Parses test cases with Unicode input/output markers.
     *
     * @param input The input string containing test cases
     * @param ans The list to store parsed test cases
     */
    public static void unicodeParseInputOutPut(String input, List<String> ans) {
        // Implementation details...
    }

    /**
     * Checks if input contains Unicode formatted test cases.
     *
     * @param input The input string to check
     * @return true if Unicode markers are found, false otherwise
     */
    public static boolean checkHasUnicodeInputOutput(String input) {
        // Implementation details...
        return false;
    }

    /**
     * Removes HTML tags and formatting from test case string.
     *
     * @param s The string to clean
     * @return Cleaned string without HTML tags
     */
    public static String handlerIgnoreStr(String s) {
        if (StringUtils.isEmpty(s)) return s;
        // Tag removal logic...
        return s;
    }
}