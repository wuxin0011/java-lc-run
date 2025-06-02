package code_generation.crwal.nowcoder;

import code_generation.crwal.request.Request;

/**
 * Utility class for handling NowCoder URLs and related API requests.
 * Provides constants and methods for working with NowCoder problem URLs and their API endpoints.
 * @author wuxin0011
 * @since 1.0
 */
public class NCUrl {

    /**
     * Base URL prefix for NowCoder website
     */
    public static final String PREFIX = "https://www.nowcoder.com/";

    /**
     * URL prefix for NowCoder practice problems
     */
    public static final String PRACTICE_PREFIX = PREFIX + "practice/";

    /**
     * API endpoint for querying question details by UUID
     */
    public static final String QUERY_BY_UUID_URL = "https://questionbank.nowcoder.com/api/qmp/question/detail/by-uuid?uuid=";

    /**
     * Shared Request instance for making HTTP requests
     */
    private static final Request REQUEST = new Request(NCUrl.class);

    /**
     * Retrieves practice problem data from NowCoder API using a practice URL.
     * Extracts the UUID from the practice URL and queries the NowCoder API for problem details.
     *
     * @param url The NowCoder practice problem URL (must be a valid PRACTICE_PREFIX URL)
     * @return String containing the JSON response from the NowCoder API
     * @throws IllegalArgumentException if the URL is not a valid practice URL
     * @throws RuntimeException if there's an error making the API request
     */
    public static String getPractice(String url) {
        String uuid = NCUtil.getUUIDByPracticeUrl(url);
        return REQUEST.requestGet(QUERY_BY_UUID_URL + uuid, Request.applicationJSON);
    }
}