package code_generation.crwal.leetcode;

/**
 * Utility class containing GraphQL query templates for LeetCode API requests.
 * Provides pre-formatted JSON strings for various LeetCode data queries.
 * @author: wuxin0011
 * @since 1.0
 */
public class LCJsonTemplate {

    // GraphQL query for today's question
    private static final String questionOfTodayStr = "{\n" +
            "    \"query\": \"\\n    query questionOfToday {\\n  todayRecord {\\n    date\\n    userStatus\\n    question {\\n      questionId\\n      frontendQuestionId: questionFrontendId\\n      difficulty\\n      title\\n      titleCn: translatedTitle\\n      titleSlug\\n      paidOnly: isPaidOnly\\n      freqBar\\n      isFavor\\n      acRate\\n      status\\n      solutionNum\\n      hasVideoSolution\\n      topicTags {\\n        name\\n        nameTranslated: translatedName\\n        id\\n      }\\n      extra {\\n        topCompanyTags {\\n          imgUrl\\n          slug\\n          numSubscribed\\n        }\\n      }\\n    }\\n    lastSubmission {\\n      id\\n    }\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {},\n" +
            "    \"operationName\": \"questionOfToday\"\n" +
            "}";

    /**
     * Gets the GraphQL query for today's question.
     * @return JSON string containing today's question query
     */
    public static String questionOfToday() {
        return questionOfTodayStr;
    }

    // GraphQL query for question title information
    private static final String questionTitleStr = "{\n" +
            "    \"query\": \"\\n    query questionTitle($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    questionId\\n    questionFrontendId\\n    title\\n    titleSlug\\n    isPaidOnly\\n    difficulty\\n    likes\\n    dislikes\\n    categoryTitle\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"titleSlug\": \"%s\"\n" +
            "    },\n" +
            "    \"operationName\": \"questionTitle\"\n" +
            "}";

    /**
     * Gets the GraphQL query for question title information.
     * @param titleSlug the problem identifier in URL
     * @return formatted JSON query string
     */
    public static String questionTitle(String titleSlug) {
        return String.format(questionTitleStr, titleSlug);
    }

    // GraphQL query for question translations
    private static final String questionTranslationStr = "{\n" +
            "    \"query\": \"\\n    query questionTranslations($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    translatedTitle\\n    translatedContent\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"titleSlug\": \"%s\"\n" +
            "    },\n" +
            "    \"operationName\": \"questionTranslations\"\n" +
            "}";

    /**
     * Gets the GraphQL query for question translations (title and content).
     * @param titleSlug the problem identifier in URL
     * @return formatted JSON query string
     */
    public static String questionTranslations(String titleSlug) {
        return String.format(questionTranslationStr, titleSlug);
    }

    // GraphQL query for question topic tags
    private static final String singleQuestionTopicTagsStr = "{\n" +
            "    \"query\": \"\\n    query singleQuestionTopicTags($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    topicTags {\\n      name\\n      slug\\n      translatedName\\n    }\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"titleSlug\": \"%s\"\n" +
            "    },\n" +
            "    \"operationName\": \"singleQuestionTopicTags\"\n" +
            "}";

    /**
     * Gets the GraphQL query for question topic tags.
     * @param suffixUrl the problem URL suffix
     * @return formatted JSON query string
     */
    public static String singleQuestionTopicTags(String suffixUrl) {
        return String.format(singleQuestionTopicTagsStr, suffixUrl);
    }

    // GraphQL query for question test cases (deprecated)
    private static final String questionTestCase = "{\n" +
            "    \"query\": \"\\n    query consolePanelConfig($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    questionId\\n    questionFrontendId\\n    questionTitle\\n    enableRunCode\\n    enableSubmit\\n    enableTestMode\\n    jsonExampleTestcases\\n    exampleTestcases\\n    metaData\\n    sampleTestCase\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"titleSlug\": \"%s\"\n" +
            "    },\n" +
            "    \"operationName\": \"consolePanelConfig\"\n" +
            "}";

    /**
     * @deprecated This API is not reliable, use other methods instead
     * @param suffixUrl the problem URL suffix
     * @return formatted JSON query string
     */
    @Deprecated
    public static String createQuestionTestCase(String suffixUrl) {
        return String.format(questionTestCase, suffixUrl);
    }

    // GraphQL query for global data
    private static final String totalInfo = "{\n" +
            "\"operationName\": \"globalData\",\n" +
            "\"query\":         \"query globalData {\n  feature {\n    questionTranslation\n    subscription\n    signUp\n    discuss\n    mockInterview\n    contest\n    store\n    book\n    chinaProblemDiscuss\n    socialProviders\n    studentFooter\n    cnJobs\n    __typename\n  }\n  userStatus {\n    isSignedIn\n    isAdmin\n    isStaff\n    isSuperuser\n    isTranslator\n    isPremium\n    isVerified\n    isPhoneVerified\n    isWechatVerified\n    checkedInToday\n    username\n    realName\n    userSlug\n    groups\n    jobsCompany {\n      nameSlug\n      logo\n      description\n      name\n      legalName\n      isVerified\n      permissions {\n        canInviteUsers\n        canInviteAllSite\n        leftInviteTimes\n        maxVisibleExploredUser\n        __typename\n      }\n      __typename\n    }\n    avatar\n    optedIn\n    requestRegion\n    region\n    activeSessionId\n    permissions\n    notificationStatus {\n      lastModified\n      numUnread\n      __typename\n    }\n    completedFeatureGuides\n    useTranslation\n    __typename\n  }\n  siteRegion\n  chinaHost\n  websocketUrl\n}\n\",\n" +
            "\t\t}";

    /**
     * Gets the GraphQL query for global data.
     * @return JSON string containing global data query
     */
    public static String getTotalInfo() {
        return totalInfo;
    }

    // GraphQL query for question discussion comments
    private static final String commentInfoStr = "{\n" +
            "    \"query\": \"\\n    query questionDiscussComments($topicId: Int!, $orderBy: CommentOrderBy, $skip: Int!, $numPerPage: Int = 10) {\\n  commonTopicComments(\\n    topicId: $topicId\\n    orderBy: $orderBy\\n    skip: $skip\\n    first: $numPerPage\\n  ) {\\n    edges {\\n      node {\\n        ...commentFields\\n      }\\n    }\\n    totalNum\\n  }\\n}\\n    \\n    fragment commentFields on CommentRelayNode {\\n  id\\n  ipRegion\\n  numChildren\\n  isEdited\\n  post {\\n    id\\n    content\\n    voteUpCount\\n    creationDate\\n    updationDate\\n    status\\n    voteStatus\\n    isOwnPost\\n    author {\\n      username\\n      isDiscussAdmin\\n      isDiscussStaff\\n      profile {\\n        userSlug\\n        userAvatar\\n        realName\\n      }\\n    }\\n    mentionedUsers {\\n      key\\n      username\\n      userSlug\\n      nickName\\n    }\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"topicId\": 2662371,\n" +
            "        \"skip\": 0,\n" +
            "        \"numPerPage\": 10,\n" +
            "        \"orderBy\": \"HOT\"\n" +
            "    },\n" +
            "    \"operationName\": \"questionDiscussComments\"\n" +
            "}";

    /**
     * Gets the GraphQL query for question discussion comments.
     * @return JSON string containing comments query
     */
    public static String questionDiscussComments() {
        return commentInfoStr;
    }

    // GraphQL query for question editor data
    private static final String questionEditorDataStr = "{\n" +
            "    \"query\": \"\\n    query questionEditorData($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    questionId\\n    questionFrontendId\\n    codeSnippets {\\n      lang\\n      langSlug\\n      code\\n    }\\n    envInfo\\n    enableRunCode\\n    hasFrontendPreview\\n    frontendPreviews\\n  }\\n}\\n    \",\n" +
            "    \"variables\": {\n" +
            "        \"titleSlug\": \"%s\"\n" +
            "    },\n" +
            "    \"operationName\": \"questionEditorData\"\n" +
            "}";

    // GraphQL query for contest questions (new version)
    private static final String newContestQuestionJSONStr =
            "{\n" +
                    "    \"query\": \"\\n    query contestQuestion($contestSlug: String, $questionSlug: String) {\\n  contestDetail(contestSlug: $contestSlug) {\\n    startTime\\n    duration\\n    titleSlug\\n    failCount\\n    enableContestDynamicLayout\\n    isDynamicLayout\\n    hasCompletedContest\\n    isVirtualContest\\n  }\\n  contestQuestion(contestSlug: $contestSlug, questionSlug: $questionSlug) {\\n    totalAc\\n    totalSubmission\\n    totalTriedUser\\n    totalAcUser\\n    languageList {\\n      id\\n      name\\n      verboseName\\n    }\\n    submittableLanguageList {\\n      id\\n      name\\n      verboseName\\n    }\\n    question {\\n      status\\n      questionId\\n      questionFrontendId\\n      enableRunCode\\n      enableSubmit\\n      enableTestMode\\n      metaData\\n      title\\n      titleSlug\\n      difficulty\\n      categoryTitle\\n      codeSnippets {\\n        code\\n        lang\\n        langSlug\\n      }\\n      exampleTestcaseList\\n      canSeeQuestion\\n      envInfo\\n      content\\n      translatedTitle\\n      translatedContent\\n    }\\n  }\\n}\\n    \",\n" +
                    "    \"variables\": {\n" +
                    "        \"contestSlug\": \"%s\",\n" +
                    "        \"questionSlug\": \"%s\"\n" +
                    "    },\n" +
                    "    \"operationName\": \"contestQuestion\"\n" +
                    "}";

    /**
     * Gets the GraphQL query for contest questions (new version).
     * @param contestSlug the contest identifier
     * @param questionSlug the problem identifier
     * @return formatted JSON query string
     */
    public static String newContestQuestion(String contestSlug, String questionSlug) {
        return String.format(newContestQuestionJSONStr, contestSlug, questionSlug);
    }

    /**
     * Gets the GraphQL query for question editor data.
     * @param titleSlug the problem identifier in URL
     * @return formatted JSON query string
     */
    public static String questionEditorData(String titleSlug) {
        return String.format(questionEditorDataStr, titleSlug);
    }

    // GraphQL query for user status
    private static final String userStatusStr = "{\n" +
            "    \"variables\": {},\n" +
            "    \"query\": \"\\n        query globalData {\\n          userStatus {\\n            isSignedIn\\n            isPremium\\n            username\\n            realName\\n            avatar\\n            userSlug\\n            isAdmin\\n            checkedInToday\\n            useTranslation\\n            premiumExpiredAt\\n            isTranslator\\n            isSuperuser\\n            isPhoneVerified\\n            isVerified\\n          }\\n          jobsMyCompany {\\n            nameSlug\\n          }\\n          commonNojPermissionTypes\\n        }\\n      \"\n" +
            "}";

    /**
     * Gets the GraphQL query for user status information.
     * @return JSON string containing user status query
     */
    public static String userStatus() {
        return userStatusStr;
    }

    // GraphQL query for CSRF token
    public static final String queryTokenStr = "{\n" +
            "    \"operationName\": \"ipInfo\",\n" +
            "    \"variables\": {},\n" +
            "    \"query\": \"query ipInfo {\\n  ipInfo {\\n    country\\n    countryCode\\n    __typename\\n  }\\n}\\n\"\n" +
            "}";

    /**
     * Gets the GraphQL query for CSRF token.
     * @return JSON string containing token query
     */
    public static String queryToken() {
        return queryTokenStr;
    }
}