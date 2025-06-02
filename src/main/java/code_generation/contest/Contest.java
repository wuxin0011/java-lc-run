package code_generation.contest;

import java.util.List;

/**
 * Interface representing a programming contest and defining operations related to contest management,
 * problem retrieval, and code template generation.
 * @author: wuxin0011
 * @since 1.0
 */
public interface Contest {

    /**
     * Gets the unique identifier for this contest.
     *
     * @return the contest ID
     */
    int getId();

    /**
     * Retrieves the URLs of all problem statements for a given contest.
     *
     * @param id the contest ID
     * @return a list of URLs pointing to the contest problems
     * @throws IllegalArgumentException if the provided ID is invalid
     */
    List<String> getUrls(int id);

    /**
     * Parses the input string to extract test cases for contest problems.
     *
     * @param input the raw input containing test cases
     * @return the formatted test case string
     * @throws NullPointerException if the input is null
     */
    String parseTestCase(String input);

    /**
     * Parses the input string to extract code template content.
     *
     * @param input the raw input containing code template
     * @return the parsed code template object
     * @throws NullPointerException if the input is null
     */
    Object parseCodeTemplate(String input);

    /**
     * Generates code templates for the contest problems.
     * Implementations should handle the creation of template files
     * with appropriate structure and boilerplate code.
     */
    void generatorTemplate();
}