package code_generation.contest;

import java.text.ParseException;
import java.util.List;

/**
 * Interface defining operations for parsing test cases from different sources.
 * Implementations should handle the extraction of test cases from raw input strings.
 * @author: wuxin0011
 * @since 1.0
 */
public interface TestCase {

    /**
     * Parses test cases from contest/problem input format.
     *
     * @param input the raw input string containing contest test cases
     * @return list of parsed test case strings
     * @throws ParseException if the input cannot be properly parsed
     * @throws NullPointerException if the input parameter is null
     *
     * <ul>
     *   <li>Handle the specific format used in coding contests</li>
     *   <li>Extract individual test cases from the input</li>
     *   <li>Return test cases in the order they appear in the input</li>
     * </ul>
     */
    List<String> parseContest(String input) throws ParseException;

    /**
     * Parses test cases from default/problem input format.
     *
     * @param input the raw input string containing default test cases
     * @return list of parsed test case strings
     * @throws ParseException if the input cannot be properly parsed
     * @throws NullPointerException if the input parameter is null
     *
     * <ul>
     *   <li>Handle standard problem test case formats</li>
     *   <li>Extract individual test cases from the input</li>
     *   <li>Return test cases in the order they appear in the input</li>
     * </ul>
     */
    List<String> parseDefault(String input) throws ParseException;
}