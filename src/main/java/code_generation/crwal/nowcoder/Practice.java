package code_generation.crwal.nowcoder;

import code_generation.contest.ParseCodeInfo;
import code_generation.contest.ParseCodeTemplate;
import code_generation.contest.TestCase;
import code_generation.crwal.TestCaseUtil;
import code_generation.utils.StringUtils;

import java.text.ParseException;

/**
 * Represents a coding practice problem and its associated components.
 * This class handles parsing and storing information about a practice problem including:
 * - Code templates
 * - Test cases
 * - Problem input
 * - Parsed code information
 * @author wuxin0011
 * @since 1.0
 */
public class Practice {

    /**
     * The code template parser for this practice problem
     */
    public ParseCodeTemplate codeTemplate;

    /**
     * The test case parser for this practice problem
     */
    public TestCase testCase;

    /**
     * The raw input string containing the practice problem data
     */
    public String input;

    /**
     * The parsed code information including method signatures and structures
     */
    public ParseCodeInfo codeInfo;

    /**
     * The formatted test cases as a string
     */
    public String test;

    /**
     * Constructs a Practice instance with the given input data.
     * Initializes the test case parser and code template parser, then processes the input.
     *
     * @param input The raw input string containing the practice problem data
     * @throws IllegalArgumentException if input is null or empty
     */
    public Practice(String input) {
        if (StringUtils.isEmpty(input)) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        this.input = input;
        this.testCase = new PracticeTestCase();
        this.codeTemplate = new PracticePCTemplate();
        init();
    }

    /**
     * Initializes the practice problem by parsing the input data.
     * Processes both test cases and code template information.
     * Silently handles any parsing exceptions (errors will result in null fields).
     */
    void init() {
        try {
            // Parse and format test cases
            this.test = TestCaseUtil.testCaseToString(testCase.parseContest(this.input));

            // Parse code template information
            this.codeInfo = codeTemplate.parseCodeTemplate(this.input);
        } catch (ParseException ignore) {
            // Parsing errors are silently caught and fields remain null
        }
    }
}