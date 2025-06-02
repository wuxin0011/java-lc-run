package code_generation.contest;

import java.text.ParseException;

/**
 * Interface defining a contract for parsing code templates and extracting structural information.
 * Implementations should process raw code input and return a structured representation of the code elements.
 * @author: wuxin0011
 * @since 1.0
 */
public interface ParseCodeTemplate {

    /**
     * Parses the input code string and extracts structural information about the code template.
     *
     * @param input the raw code string to be parsed (must not be null)
     * @return a {@link ParseCodeInfo} object containing the parsed code structure
     * @throws ParseException if the input cannot be parsed due to invalid format or syntax
     * @throws NullPointerException if the input parameter is null
     *
     * @see ParseCodeInfo
     */
    ParseCodeInfo parseCodeTemplate(String input) throws ParseException;
}