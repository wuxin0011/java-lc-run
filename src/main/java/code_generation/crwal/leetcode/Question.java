package code_generation.crwal.leetcode;

import code_generation.crwal.TestCaseUtil;
import code_generation.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;



/**
 * Represents a coding question, typically from a programming contest platform.
 * Provides methods to build and retrieve question details and parse contest questions from JSON.
 * @author wuxin0011
 * @since 1.0
 */
public class Question {
    /**
     * The category slug of the question
     */
    public String category_slug;
    /**
     * The credit value of the question
     */
    public String credit;
    /**
     * The unique identifier of the question
     */
    public String id;
    /**
     * The question ID
     */
    public String question_id;
    /**
     * The title of the question
     */
    public String title;
    /**
     * The URL-friendly version of the title
     */
    public String title_slug;
    private String url;
    /**
     * The API URL for the question
     */
    public String api_url;
    /**
     * The contest number this question belongs to
     */
    public String contestNo;
    private boolean isContest = true;

    /**
     * Sets whether this question is part of a contest
     *
     * @param isContest true if the question is part of a contest, false otherwise
     * @return The current Question instance for method chaining
     */
    public Question buildIsContest(boolean isContest) {
        this.isContest = isContest;
        return this;
    }

    /**
     * Gets the URL for the question
     *
     * @return The question URL
     */
    public String getUrl() {
        if (isContest) {
            checkContestUrl(api_url, title_slug);
        }
        return url;
    }

    /**
     * Builds and sets the API URL for the question
     *
     * @param api_url The API URL to set
     * @return The current Question instance for method chaining
     */
    public Question buildApiUrl(String api_url) {
        if (isContest) {
            checkContestUrl(api_url, title_slug);
            this.url = api_url.replace("api/info/", "") + "problems/" + title_slug;
        }
        this.api_url = api_url;
        return this;
    }

    /**
     * Builds and sets the contest number
     *
     * @param contestNo The contest number to set
     * @return The current Question instance for method chaining
     */
    public Question buildContestNo(String contestNo) {
        this.contestNo = contestNo;
        return this;
    }

    /**
     * Builds and sets the category slug
     *
     * @param category_slug The category slug to set
     * @return The current Question instance for method chaining
     */
    public Question buildCategorySlug(String category_slug) {
        this.category_slug = category_slug;
        return this;
    }

    /**
     * Builds and sets the credit value
     *
     * @param credit The credit value to set
     * @return The current Question instance for method chaining
     */
    public Question buildCredit(String credit) {
        this.credit = credit;
        return this;
    }

    /**
     * Builds and sets the question title
     *
     * @param title The title to set
     * @return The current Question instance for method chaining
     */
    public Question buildTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Builds and sets the title slug
     *
     * @param title_slug The title slug to set
     * @return The current Question instance for method chaining
     */
    public Question buildTitleSlug(String title_slug) {
        this.title_slug = title_slug;
        return this;
    }

    /**
     * Builds and sets the question ID
     *
     * @param id The ID to set
     * @return The current Question instance for method chaining
     */
    public Question builderId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Builds and sets the question ID (alternative method)
     *
     * @param question_id The question ID to set
     * @return The current Question instance for method chaining
     */
    public Question buildQuestionId(String question_id) {
        this.question_id = question_id;
        return this;
    }

    /**
     * Gets the category slug
     *
     * @return The category slug
     */
    public String getCategory_slug() {
        return category_slug;
    }

    /**
     * Gets the credit value
     *
     * @return The credit value
     */
    public String getCredit() {
        return credit;
    }

    /**
     * Gets the question ID
     *
     * @return The question ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the question ID (alternative method)
     *
     * @return The question ID
     */
    public String getQuestion_id() {
        return question_id;
    }

    /**
     * Gets the question title
     *
     * @return The question title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the title slug
     *
     * @return The title slug
     */
    public String getTitle_slug() {
        return title_slug;
    }

    /**
     * Gets the API URL
     *
     * @return The API URL
     */
    public String getApi_url() {
        return api_url;
    }

    /**
     * Gets the contest number
     *
     * @return The contest number
     */
    public String getContestNo() {
        return contestNo;
    }

    /**
     * Returns a string representation of the Question
     *
     * @return String representation of the Question
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Question.class.getSimpleName() + "[", "]")
                .add("category_slug='" + category_slug + "'")
                .add("credit='" + credit + "'")
                .add("id='" + id + "'")
                .add("question_id='" + question_id + "'")
                .add("title='" + title + "'")
                .add("title_slug='" + title_slug + "'")
                .add("url='" + url + "'")
                .add("api_url='" + api_url + "'")
                .add("contentNo='" + contestNo + "'")
                .toString();
    }

    /**
     * Validates the contest URL format
     *
     * @param api_url    The API URL to validate
     * @param title_slug The title slug to check
     * @throws RuntimeException if the URL is invalid or title_slug is empty
     */
    public static void checkContestUrl(String api_url, String title_slug) {
        if (StringUtils.isEmpty(title_slug)) {
            throw new RuntimeException("place config title_slug");
        }
        if (api_url == null
                || !api_url.startsWith(BuildUrl.LC_PREFIX + "/contest/api/info")
                || (!api_url.contains("biweekly-contest") && !api_url.contains("weekly-contest"))) {
            throw new RuntimeException("api url prefix is " + BuildUrl.LC_PREFIX + "contest/api/info , and suffix is biweekly-contest-{number} or weekly-contest-{number} ");
        }
    }

    /**
     * Parses contest questions from JSON string
     *
     * @param jsonStr The JSON string containing contest questions
     * @param api_url The API URL for the contest
     * @return List of parsed Question objects
     */
    public static List<Question> getContestQuestion(String jsonStr, String api_url) {
        String p = parse(jsonStr, "questions", '[', ']');
        List<String> parses = parse(p, '{', '}');
        List<Question> qs = new ArrayList<>();
        String NoId = StringUtils.parseId(api_url);
        for (String s : parses) {
            Question question = new Question();
            String id = StringUtils.jsonStrGetValueByKey(s, "id");
            String question_id = StringUtils.jsonStrGetValueByKey(s, "question_id");
            String title = StringUtils.jsonStrGetValueByKey(s, "title");
            String credit = StringUtils.jsonStrGetValueByKey(s, "credit");
            String title_slug = StringUtils.jsonStrGetValueByKey(s, "title_slug");
            String category_slug = StringUtils.jsonStrGetValueByKey(s, "category_slug");
            question.buildTitleSlug(title_slug);
            question.buildTitle(title);
            question.buildCredit(credit);
            question.buildCategorySlug(category_slug);
            question.builderId(id);
            question.buildQuestionId(question_id);
            question.buildApiUrl(api_url);
            question.buildContestNo(NoId);
            qs.add(question);
        }
        return qs;
    }

    /**
     * Parses a specific section from input string
     *
     * @param input The input string to parse
     * @param name  The name of the section to find
     * @param start The starting character of the section
     * @param end   The ending character of the section
     * @return The parsed section as a string
     * @throws RuntimeException if the section is not found
     */
    public static String parse(String input, String name, char start, char end) {
        char[] charArray = input.toCharArray();
        StringBuilder sb = null;
        int pos = StringUtils.kmpSearch(input, name);
        if (pos == -1) {
            throw new RuntimeException("Not find " + name);
        }
        int deep = 0;
        for (int i = pos; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == ':' && deep == 0) {
                sb = new StringBuilder();
                continue;
            } else if (c == start) {
                deep++;
            } else if (c == end) {
                deep--;
                if (deep == 0) {
                    break;
                }
            }
            if (sb != null) {
                sb.append(c);
            }
        }
        return sb == null ? "" : sb.toString();
    }

    /**
     * Parses all sections between start and end characters from input string
     *
     * @param input The input string to parse
     * @param start The starting character of sections
     * @param end   The ending character of sections
     * @return List of parsed sections
     */
    public static List<String> parse(String input, char start, char end) {
        List<String> ans = new ArrayList<>();
        char[] charArray = input.toCharArray();
        StringBuilder sb = null;
        int deep = 0;
        for (char c : charArray) {
            if (c == start) {
                deep++;
                sb = new StringBuilder();
                sb.append(c);
            } else if (c == end) {
                deep--;
                if (deep == 0) {
                    if (sb != null) {
                        sb.append(c);
                        ans.add(sb.toString());
                        sb = null;
                    }
                }
            } else {
                if (sb != null) {
                    sb.append(c);
                }
            }
        }
        return ans;
    }
}