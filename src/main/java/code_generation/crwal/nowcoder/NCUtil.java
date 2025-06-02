package code_generation.crwal.nowcoder;

import code_generation.utils.StringUtils;

import static code_generation.crwal.nowcoder.NCUrl.PRACTICE_PREFIX;

/**
 * Utility class for NowCoder-specific operations and helper methods.
 * Provides functionality for processing NowCoder URLs and extracting relevant information.
 * @author wuxin0011
 * @since 1.0
 */
public class NCUtil {

    /**
     * Main method (currently unused, available for testing purposes)
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Intentionally left empty
    }

    /**
     * Extracts the UUID from a NowCoder practice problem URL.
     * The UUID is the unique identifier between the practice prefix and query parameters.
     *
     * @param url The NowCoder practice problem URL to process
     * @return The extracted UUID string, or the original URL if empty
     * @throws RuntimeException if the URL doesn't start with the expected PRACTICE_PREFIX
     * @throws NullPointerException if the URL is null
     */
    public static String getUUIDByPracticeUrl(String url) {
        // Example input:
        // https://www.nowcoder.com/practice/75e878df47f24fdc9dc3e400ec6058ca?tpId=295&tqId=23286&ru=/exam/oj&qru=/ta/format-top101/question-ranking&sourceUrl=%2Fexam%2Foj

        if (StringUtils.isEmpty(url)) {
            return url;
        }
        if (!url.startsWith(NCUrl.PRACTICE_PREFIX)) {
            throw new RuntimeException("only allow " + NCUrl.PRACTICE_PREFIX + " starts with ");
        }

        // Remove the practice prefix
        url = url.replace(NCUrl.PRACTICE_PREFIX, "");

        // Extract UUID before query parameters
        int paramIndex = url.indexOf('?');
        if (paramIndex != -1) {
            return url.substring(0, paramIndex);
        }
        return url;
    }
}