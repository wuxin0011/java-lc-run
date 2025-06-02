package code_generation.crwal.nowcoder;

import code_generation.contest.TestCase;
import code_generation.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of TestCase interface for parsing NowCoder practice problem test cases.
 * Handles extraction and processing of input/output examples from NowCoder problem JSON data.
 * @author wuxin0011
 * @since 1.0
 */
public class PracticeTestCase implements TestCase {

    /**
     * JSON key for input test cases
     */
    static final String INPUT = "input";

    /**
     * JSON key for output test cases
     */
    static final String OUTPUT = "output";

    /**
     * Parses test cases from contest/problem input (implements TestCase interface).
     * Delegates to parseDefault method for actual parsing.
     *
     * @param input The raw input string containing test case data
     * @return List of parsed test cases in alternating input/output format
     * @throws RuntimeException if the input format is invalid
     */
    @Override
    public List<String> parseContest(String input) {
        return parseDefault(input);
    }

    /**
     * Parses test cases from default problem input format.
     * Locates the "samples" section in the JSON and delegates to parse().
     *
     * @param input The raw input string containing test case data
     * @return List of parsed test cases
     * @throws RuntimeException if "samples" section not found
     */
    @Override
    public List<String> parseDefault(String input) {
        int i = StringUtils.kmpSearch(input, "samples");
        if (i == -1) {
            throw new RuntimeException("Test case samples not found in input");
        }
        return parse(input.substring(i));
    }

    /**
     * Parses test cases from JSON array string.
     * Processes the JSON structure to extract individual test cases.
     *
     * @param input The JSON string containing test cases (starting from "samples")
     * @return List of parsed test cases in alternating input/output format
     */
    static List<String> parse(String input) {
        int deep = 0;
        StringBuilder sb = null;
        List<String> inputs = new ArrayList<>();
        int o = 0;

        // Parse JSON structure
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (StringUtils.isIgnore(c)) {
                continue;
            }
            if (c == '[') {
                o++;
                continue;
            }
            if (c == ']') {
                o--;
                if (o == 0) break;
                continue;
            }
            if (c == '{') {
                deep++;
                if (deep == 1) {
                    sb = new StringBuilder();
                }
                if (sb != null) {
                    sb.append(c);
                }
            } else if (c == '}') {
                deep--;
                if (sb != null) {
                    sb.append(c);
                }
                if (deep == 0 && sb != null) {
                    inputs.add(sb.toString());
                    sb = null;
                }
            } else if (c == ',') {
                if (deep > 0 && sb != null) {
                    sb.append(c);
                }
            } else {
                if (sb != null) {
                    sb.append(c);
                }
            }
        }

        // Process extracted test cases
        List<String> result = new ArrayList<>();
        for (String an : inputs) {
            getCase(an, result);
        }
        StringUtils.handlerResult(result);
        return result;
    }

    /**
     * Extracts input and output cases from a test case JSON object.
     *
     * @param s The JSON string representing a single test case
     * @param ans The list to store extracted test cases
     */
    public static void getCase(String s, List<String> ans) {
        handlerInputOutput(s, StringUtils.kmpSearch(s, INPUT), INPUT.length(), ans);
        handlerInputOutput(s, StringUtils.kmpSearch(s, OUTPUT), OUTPUT.length(), ans);
        ans.add("\n");  // Add separator between test cases
    }

    /**
     * Handles extraction of input or output from a test case JSON string.
     *
     * @param s The JSON string containing the test case data
     * @param st The starting index of the value to extract
     * @param len The length of the key being extracted
     * @param ans The list to store the extracted value
     * @throws IndexOutOfBoundsException if the key is not found or index is invalid
     */
    public static void handlerInputOutput(String s, int st, int len, List<String> ans) {
        if (st == -1 || st + len >= s.length()) {
            throw new IndexOutOfBoundsException("Invalid test case format - missing input/output");
        }
        st += len;
        int deep = 0;
        StringBuilder sb = new StringBuilder();

        // Extract JSON value
        for (int i = st; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ':') {
                continue;
            }
            if (c == '{' || c == '[') {
                deep++;
            } else if (c == '}' || c == ']') {
                deep--;
                if (deep == 0) {
                    sb.append(c);
                    break;
                }
            } else if (c == ',') {
                if (deep == 0) {
                    break;
                }
            }
            sb.append(c);
        }
        ans.add(sb.toString());
    }
}