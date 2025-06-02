package code_generation.crwal;


/**
 * A utility class containing constant values used throughout the application.
 * This class primarily stores configuration-related constants including:
 * - Cookie/Session token names
 * - Configuration file names
 * - Request parameter names
 * @author wuxin0011
 * @since 1.0
 */
public class Constant {

    /**
     * Name of the CSRF token used in requests
     */
    public static final String csrftoken = "csrftoken";

    /**
     * Name of the LeetCode session cookie
     */
    public static final String LEETCODE_SESSION = "LEETCODE_SESSION";

    /**
     * Default name for the request configuration file
     */
    public static final String CONFIG_NAME = "request.properties";

    /**
     * Key for request parameters in configuration
     */
    public static final String params = "params";

    /**
     * Key for request headers in configuration
     */
    public static final String headers = "headers";

    /**
     * Key for cookies in configuration
     */
    public static final String cookies = "cookies";

    /**
     * Array of configuration file names that should be loaded
     */
    public static final String[] CONFIGS = new String[]{headers};
}