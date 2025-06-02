package code_generation.crwal.leetcode;


import code_generation.crwal.request.Request;
import code_generation.utils.StringUtils;

import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for building and managing LeetCode URLs and API requests.
 * Provides methods to construct various LeetCode URLs and make API calls.
 * @author: wuxin0011
 * @since 1.0
 */
public class BuildUrl {

    // Base URL constants
    public final static String LC_PREFIX = "https://leetcode.cn";
    public final static String LC_PROBLEM_PREFIX = LC_PREFIX + "/problems";
    public final static String LC_LOGIN = LC_PREFIX + "/accounts/login/";
    public final static String graphql = LC_PREFIX + "/graphql/";
    public final static String API_PREFIX = LC_PREFIX + "/contest/api/info/";
    public final static String LC_WEEKLY_CONTEST_PREFIX = LC_PREFIX + "/contest/weekly-contest";
    public final static String LC_BI_WEEKLY_CONTEST_PREFIX = LC_PREFIX + "/contest/biweekly-contest";

    // URL patterns
    public final static String QuestionWeeklyUrlsPattern = API_PREFIX + "weekly-contest-%s/";
    public final static String QuestionBiWeeklyUrlsPattern = API_PREFIX + "biweekly-contest-%s/";
    public final static String WeeklyUrlPattern = LC_WEEKLY_CONTEST_PREFIX + "-%s/problems/%s/";
    public final static String BiWeeklyUrlPattern = LC_BI_WEEKLY_CONTEST_PREFIX + "-%s/problems/%s/";

    // Classic problem URL
    public final static String LC_CLASS_THEME_PREFIX = LC_PREFIX + "/classic" + "/problems";

    // Request configuration
    public static final Request request = new Request(BuildUrl.class);

    /**
     * Gets the default question description for a given title slug.
     *
     * @param titleSlug the problem identifier in URL (e.g., "two-sum")
     * @return JSON string containing question description
     * @throws NullPointerException if titleSlug is null
     */
    public static String getDefaultQuestionDescription(String titleSlug) {
        return request.requestPost(buildTitleSlugUrl(titleSlug), buildTitleSlug(titleSlug));
    }

    /**
     * Gets contest question page HTML.
     *
     * @param url the contest problem URL
     * @return HTML content of the contest problem page
     */
    public static String getContestQuestionPage(String url) {
        return request.requestGet(url, Request.applicationHtml);
    }

    /**
     * Gets contest question information in JSON format.
     *
     * @param url the contest API URL
     * @return JSON string containing contest questions
     */
    public static String getContestUrls(String url) {
        return request.requestGet(url, Request.applicationJSON);
    }

    /**
     * Makes a POST request to specified URL with JSON payload.
     *
     * @param url the target URL
     * @param jsonStr the JSON payload
     * @return response as JSON string
     */
    public static String getPost(String url, String jsonStr) {
        return request.requestPost(url, jsonStr);
    }

    /**
     * Makes a POST request to LeetCode GraphQL endpoint.
     *
     * @param jsonStr the GraphQL query JSON
     * @return response as JSON string
     */
    public static String getPost(String jsonStr) {
        return request.requestPost(graphql, jsonStr);
    }

    /**
     * Makes a POST request and returns full response headers.
     *
     * @param jsonStr the GraphQL query JSON
     * @return map of response headers
     */
    public static Map<String, List<String>> getResponse(String jsonStr) {
        return request.requestPostResponse(graphql, Request.applicationJSON, jsonStr, null, true);
    }

    /**
     * Gets weekly contest questions.
     *
     * @param id the contest ID
     * @return JSON string containing contest questions
     */
    public static String getWeeklyContestUrls(String id) {
        String url = String.format(QuestionWeeklyUrlsPattern, id);
        return request.requestGet(url, Request.applicationJSON);
    }

    /**
     * Gets bi-weekly contest questions.
     *
     * @param id the contest ID
     * @return JSON string containing contest questions
     */
    public static String getBiWeeklyContestUrls(String id) {
        String url = String.format(QuestionBiWeeklyUrlsPattern, id);
        return request.requestGet(url, Request.applicationJSON);
    }

    /**
     * Gets weekly contest problem page.
     *
     * @param id the contest ID
     * @param title the problem title slug
     * @return HTML content of the problem page
     */
    public static String getWeeklyContestProblem(String id, String title) {
        String url = String.format(WeeklyUrlPattern, id, title);
        return request.requestGet(url, Request.applicationHtml);
    }

    /**
     * Gets bi-weekly contest problem page.
     *
     * @param id the contest ID
     * @param title the problem title slug
     * @return HTML content of the problem page
     */
    public static String getBiWeeklyContestProblem(String id, String title) {
        String url = String.format(BiWeeklyUrlPattern, id, title);
        return request.requestGet(url, Request.applicationHtml);
    }

    /**
     * @deprecated This API is not reliable, use {@link #questionTranslations(String)} instead
     * @param titleSlug titleSlug
     * @return string result
     */
    @Deprecated
    public static String getCreateQuestionTestCase(String titleSlug) {
        return getPost(LCJsonTemplate.createQuestionTestCase(buildTitleSlug(titleSlug)));
    }

    /**
     * Gets today's daily challenge question.
     *
     * @return JSON string containing today's question
     */
    public static String questionOfToday() {
        return getPost(LCJsonTemplate.questionOfToday());
    }

    /**
     * Gets editor data including code templates.
     *
     * @param titleSlug the problem title slug
     * @return JSON string containing editor data
     */
    public static String questionEditorData(String titleSlug) {
        return getPost(LCJsonTemplate.questionEditorData(buildTitleSlug(titleSlug)));
    }

    /**
     * Gets CSRF token from cookies.
     *
     * @return CSRF token string
     */
    public static String queryToken() {
        Map<String, List<String>> response = getResponse(LCJsonTemplate.queryToken());
        if (response == null) return "";
        List<String> cookies = response.get("Set-Cookie");
        for (String cookie : cookies) {
            String[] parts = cookie.split(";");
            for (String part : parts) {
                if (part.trim().startsWith("csrftoken=")) {
                    return part.trim().substring("csrftoken=".length());
                }
            }
        }
        return "";
    }

    /**
     * Attempts to login to LeetCode.
     *
     * @param username the LeetCode username
     * @param password the LeetCode password
     * @param csrftoken the CSRF token
     * @return empty string (implementation incomplete)
     */
    public static String login(String username, String password, String csrftoken) {
        HashMap<String, String> data = new HashMap<>();
        data.put("password", password);
        data.put("login", username);
        data.put("csrfmiddlewaretoken", csrftoken);
        data.put("next", "/");
        HttpURLConnection connection = request.requestBefore(
                LC_LOGIN, Request.applicationJSON, null, "POST", data, true);
        System.out.println(connection);
        return "";
    }

    /**
     * Gets current user status.
     *
     * @return JSON string containing user status
     */
    public static String userStatus() {
        return getPost(LCJsonTemplate.userStatus());
    }

    /**
     * Builds full problem URL from title slug.
     *
     * @param titleSlug the problem identifier
     * @return complete problem URL
     * @throws NullPointerException if titleSlug is null
     * @throws RuntimeException if titleSlug is invalid
     */
    public static String buildTitleSlugUrl(String titleSlug) {
        if (StringUtils.isEmpty(titleSlug)) {
            throw new NullPointerException("Title slug cannot be null");
        }
        if (titleSlug.contains(LC_PREFIX) && !titleSlug.startsWith(LC_PROBLEM_PREFIX)) {
            throw new RuntimeException("Unsupported URL format: " + titleSlug);
        }
        if (!titleSlug.startsWith(LC_PROBLEM_PREFIX)) {
            titleSlug = LC_PROBLEM_PREFIX + "/" + titleSlug;
        }
        return titleSlug;
    }

    /**
     * Normalizes a title slug from various URL formats.
     *
     * @param titleSlug the input URL or slug
     * @return normalized title slug
     * @throws NullPointerException if titleSlug is null
     * @throws RuntimeException if titleSlug is invalid
     */
    public static String buildTitleSlug(String titleSlug) {
        if (StringUtils.isEmpty(titleSlug)) {
            throw new NullPointerException("Title slug cannot be null");
        }
        if (titleSlug.startsWith(LC_PROBLEM_PREFIX)) {
            titleSlug = titleSlug.replace(LC_PROBLEM_PREFIX + "/", "");
        }
        if (titleSlug.startsWith(LC_WEEKLY_CONTEST_PREFIX) ||
                titleSlug.startsWith(LC_BI_WEEKLY_CONTEST_PREFIX)) {
            if (!titleSlug.contains("/problems/")) {
                throw new RuntimeException("Invalid contest problem URL: " + titleSlug);
            }
            titleSlug = titleSlug.split("/problems/")[1];
        }
        return titleSlug.split("/")[0];
    }

    /**
     * Gets problem translations.
     *
     * @param titleSlug the problem identifier
     * @return JSON string containing translations
     */
    public static String questionTranslations(String titleSlug) {
        return getPost(LCJsonTemplate.questionTranslations(buildTitleSlug(titleSlug)));
    }

    /**
     * Gets new contest questions.
     *
     * @param contestUrl the contest URL
     * @return JSON string containing contest questions
     */
    public static String queryNewContestQuestion(String contestUrl) {
        String titleSlug = buildTitleSlug(contestUrl);
        String contestNo = contestUrl.replace(LC_PREFIX + "/contest/", "").split("/problems")[0];
        return getPost(LCJsonTemplate.newContestQuestion(contestNo, titleSlug));
    }

    /**
     * Attempts to request login page and get session cookies.
     * Currently prints connection information for debugging.
     */
    public static void requestLoginPage() {
        try {
            URL loginURL = new URL(LC_LOGIN);
            URI uri = loginURL.toURI();

            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            // This would need actual HTTP request to populate cookies
            // Currently just demonstrates cookie handling structure
            for (HttpCookie cookie : cookieManager.getCookieStore().get(uri)) {
                if ("LEETCODE_SESSION".equals(cookie.getName())) {
                    // Found session cookie
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}