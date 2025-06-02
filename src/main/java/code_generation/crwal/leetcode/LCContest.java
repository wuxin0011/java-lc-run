package code_generation.crwal.leetcode;

import code_generation.contest.*;
import code_generation.crwal.TestCaseUtil;
import code_generation.utils.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * Implementation of {@link Contest} for LeetCode weekly and bi-weekly contests.
 * Handles contest problem generation, template creation, and directory management.
 * @author: wuxin0011
 * @since 1.0
 */
public class LCContest implements Contest {

    // Constants for contest types and configuration
    private static final LocalDate NOW = LocalDate.now();

    /** Weekly contest directory identifier */
    public static final String WEEK_DRI = "weekly";

    /** Bi-weekly contest directory identifier */
    public static final String BI_WEEK_DRI = "biweekly";

    /** Weekly contest file prefix */
    public static final String WEEKLY_PREFIX = "w_";

    /** Bi-weekly contest file prefix */
    public static final String BI_WEEKLY_PREFIX = "bi_";

    /**
     * Whether to create solution.md files (default: false)
     */
    public static final boolean CREATE_SOLUTION_ME = false;

    /**
     * Whether to create parent directory readme.md (default: true)
     */
    public static final boolean CREATE_READ_ME_FATHER = true;

    /** Preconfigured weekly contest instance */
    public static final LCContest WEEK_CONTEST = new LCContest(380, "2024-01-14 10:30:00", 4, WEEKLY_PREFIX, WEEK_DRI, 1, null);

    /** Preconfigured bi-weekly contest instance */
    public static final LCContest BI_WEEK_CONTEST = new LCContest(1, "2019-06-01 22:30:00", 4, BI_WEEKLY_PREFIX, BI_WEEK_DRI, 2, null);

    private final static int[] DAYS = {0, 7, 1, 2, 3, 4, 5, 6};

    /**
     * originDir
     */
    private String originDir;
    /**
     * lastestDateNo
     */
    private int lastestDateNo; // Latest contest number
    /**
     * lastestDate
     */
    private LocalDate lastestDate; // Latest contest date

    /**
     * dir
     */
    private String dir; // Target directory

    /**
     * times
     */
    private int times; // Contest frequency (weeks)

    /**
     * problems
     */
    private int problems; // Number of problems (default: 4)

    /**
     * dirPrefix
     */
    private String dirPrefix; // Directory prefix

    /**
     * aClass
     */
    private Class<?> aClass; // Class for path resolution

    /**
     * username
     */
    private String username;

    /**
     * questionList
     */
    private List<Question> questionList;

    /**
     * testCase
     */
    private String testCase;

    /**
     * code
     */
    private String code;

    /**
     * java file save
     */
    private List<String> javafiles = new ArrayList<>();

    /**
     * default template parse
     */
    private static final LCTemplate lcTemplate = new LCTemplate("{'value': 'java', 'text': 'Java', 'defaultCode':");

    /**
     * default case parse
     */
    static final LCTestCase TEST_CASE = new LCTestCase();

    /**
     * Constructs a new LCContest instance.
     *
     * @param no            latest contest number
     * @param lastestDate   latest contest date (yyyy-MM-dd HH:mm:ss)
     * @param problems      number of problems
     * @param dirPrefix     directory prefix
     * @param dir           base directory
     * @param times         contest frequency in weeks
     * @param c             reference class for path resolution
     */
    public LCContest(int no, String lastestDate, int problems, String dirPrefix, String dir, int times, Class<?> c) {
        this.lastestDateNo = no;
        this.originDir = dir;
        this.dir = dir;
        this.lastestDate = Objects.requireNonNull(parse(lastestDate), "Invalid date format");
        this.setClass(c, dir);
        this.times = Math.max(1, times);
        this.problems = problems;
        this.dirPrefix = dirPrefix.endsWith("_") ? dirPrefix : (dirPrefix + "_");
    }

    /**
     * Sets the reference class and updates directory paths.
     *
     * @param c     reference class
     * @param _dir  base directory
     */
    public void setClass(Class<?> c, String _dir) {
        this.aClass = c == null ? LCContest.class : c;
        this.dir = IoUtil.wrapperAbsolutePath(aClass,
                StringUtils.isEmpty(_dir) ? !StringUtils.isEmpty(originDir) ? originDir : dir : _dir);
    }

    /**
     * Validates contest configuration.
     * Currently unimplemented.
     */
    public void valid() {
        //
    }

    /**
     * Generates the next contest problems.
     */
    public void next() {
        valid();
        createTestProblem(getId(), getProblemDir());
    }

    /**
     * Gets the username associated with this contest.
     * @return current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this contest.
     * @param username LeetCode username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Uses an older template format for problem generation.
     *
     * @param No        contest number
     * @param maxId     maximum contest ID
     * @param idx       problem index
     * @param question  question data
     */
    public void useOldTemplate(int No, int maxId, int idx, Question question) {
        if (maxId == No) {
            AtomicReference<String> tc = new AtomicReference<>("");
            ExceptionUtils.executeWithExceptionHandling(() -> {
                tc.set(question.getUrl());
            });
            ExceptionUtils.executeWithExceptionHandling(() -> {
                Problem.create(idx, dir, tc.get(), "");
            });
        } else {
            this.useDefaultTemplate(idx, question);
        }
    }

    /**
     * Uses the default template for problem generation.
     *
     * @param idx       problem index
     * @param question  question data
     */
    public void useDefaultTemplate(int idx, Question question) {
        ExceptionUtils.executeWithExceptionHandling(() -> {
            createContestTemplate(idx, dir, question);
        });
    }

    /**
     * Creates test problems for a contest.
     *
     * @param No    contest number
     * @param dir   target directory
     */
    private void createTestProblem(int No, String dir) {
        System.out.println("==========================================" + No + " start ==========================================");
        List<Question> questions = getQuestions(No);
        if (questions.isEmpty()) {
            System.out.println("No contest found for number: " + No);
            return;
        }

        if (StringUtils.isEmpty(username)) {
            this.username = getUserName();
        }

        if (!StringUtils.isEmpty(username)) {
            System.out.println("\nWelcome " + CustomColor.error(username) + " \n");
        }

        System.out.println("Fetching problem URLs...");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (question == null) continue;

            String tempTitle = (StringUtils.isEmpty(question.getTitle()) ? question.getTitle_slug() : question.getTitle());
            System.out.println("\nParsing question: " + CustomColor.pink(tempTitle) +
                    ", Credit: " + (StringUtils.isEmpty(question.getCredit()) ? "unknown" : question.getCredit()));
            System.out.println("URL: " + (StringUtils.isEmpty(question.getUrl()) ? "unknown" : question.getUrl()));

            try {
                createContestTemplate(i, dir, question);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Using fallback template...");
                AtomicReference<String> tc = new AtomicReference<>("");
                int finalI = i;
                ExceptionUtils.executeWithExceptionHandling(() -> {
                    tc.set(getTestCase(questions.get(finalI).getUrl()));
                });
                ExceptionUtils.executeWithExceptionHandling(() -> {
                    Problem.create(finalI, dir, tc.get(), "");
                });
            }

            ExceptionUtils.sleep(Math.min(1, (int) (Math.random() * 5)));
        }

        System.out.println("==========================================" + No + " end ==========================================");
        createReadme(No, dir, questions);
    }

    /**
     * Creates contest problem templates.
     *
     * @param curId     problem index
     * @param dir       target directory
     * @param question  question data
     */
    public void createContestTemplate(int curId, String dir, Question question) {
        if (question == null) throw new NullPointerException("Question cannot be null");

        ClassTemplate classTemplate = new ClassTemplate();
        String titleSlug = question.getTitle_slug();
        String testCase = null;

        // Parse code info and test cases
        ParseCodeInfo parseCodeInfo = this.parseCodeTemplate(question);
        if (parseCodeInfo != null) {
            List<String> parseResult = LCTestCase._2025NewHandlerInputAndOutput(parseCodeInfo.getOrigin());
            if (!parseResult.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for(String s : parseResult) sb.append(StringUtils.isEmpty(s) ? "" : s);
                testCase = sb.toString();
            } else {
                testCase = TestCaseUtil.testCaseToString(
                        TEST_CASE.parseContest(
                                StringUtils.jsonStrGetValueByKey(parseCodeInfo.getOrigin(), "translatedContent")
                        )
                );
            }
        } else {
            String contestHtml = BuildUrl.getContestQuestionPage(question.getUrl());
            testCase = TestCaseUtil.testCaseToString(TEST_CASE.parseContest(contestHtml));
            parseCodeInfo = this.parseCodeTemplate(contestHtml);
        }

        if (parseCodeInfo == null) {
            throw new RuntimeException("Authentication failed. Check your cookie at: " + BuildUrl.request.getConfigPath());
        }

        // Configure class template
        String method = parseCodeInfo.getMethod();
        String methodName = parseCodeInfo.getMethodName();
        String title = StringUtils.isEmpty(question.title) ? titleSlug : question.title;
        question.buildTitle(title);

        classTemplate.buildTitle(title)
                .buildCodeInfo(parseCodeInfo)
                .buildIsNeedMod(StringUtils.isNeedMOD(""))
                .buildUrl(question.getUrl())
                .buildMethod(method)
                .buildAuthor(this.username)
                .buildMethodName(methodName)
                .buildTextFileName(Problem.createDir(curId, true))
                .buildTitle(question.getTitle());

        // Create files
        String className = Problem.createDir(curId, true);
        String javaFile = dir + className + ".java";
        String txtFile = dir + className + ".txt";
        javafiles.add(javaFile);

        String packageInfo = ReflectUtils.getPackageInfo(javaFile);
        classTemplate.buildPackageInfo(packageInfo);

        Problem.create(new ProblemInfo(javaFile, txtFile, "", testCase, classTemplate, aClass));
    }

    /**
     * Gets test cases for a problem URL.
     *
     * @param s problem URL
     * @return test case string
     */
    private static String getTestCase(String s) {
        String contestHtml = BuildUrl.getContestQuestionPage(s);
        List<String> strings = TEST_CASE.parseContest(contestHtml);
        return TestCaseUtil.testCaseToString(strings);
    }

    /**
     * Wraps directory number for grouping.
     *
     * @param dirNo directory number
     * @return wrapped directory string
     */
    public static String wrapper(int dirNo) {
        dirNo = dirNo / 100 * 100;
        return dirNo == 0 ? "000" : String.valueOf(dirNo);
    }

    /**
     * Gets the wrapper directory path.
     *
     * @return wrapper directory path
     */
    public String getWrapperDir() {
        return String.format("%s%s%s%s", dir, File.separator, dirPrefix, wrapper(this.getId()));
    }

    /**
     * Gets the problem directory path.
     *
     * @return problem directory path
     */
    public String getProblemDir() {
        return String.format("%s%s%s%s%s", getWrapperDir(), File.separator, dirPrefix, this.getId(), File.separator);
    }

    /**
     * Parses a date string into LocalDate.
     *
     * @param text date string (yyyy-MM-dd HH:mm:ss)
     * @return parsed LocalDate or null if invalid
     */
    public LocalDate parse(String text) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);
            return localDateTime.toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Interactive method to create contest with user input.
     *
     * @param aClass reference class for path resolution
     */
    public void createNo(Class<?> aClass) {
        this.setClass(aClass, null);
        int NO = Integer.MAX_VALUE;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Please input a valid contest number, or 'NO' to break: ");
            try {
                String next = sc.next();
                if ("NO".equalsIgnoreCase(next)) {
                    break;
                }
                NO = Integer.parseInt(next);
                if (NO <= 0) {
                    continue;
                }
                break;
            } catch (Exception ignored) {
                sc.next();
            }
        }
        createNo(NO);
    }

    /**
     * Creates contest with specified number.
     *
     * @param NO contest number
     */
    public void createNo(int NO) {
        if (NO == Integer.MAX_VALUE) {
            return;
        }
        int maxId = getId();
        if (NO > maxId || NO < 0) {
            throw new RuntimeException("Max NO = " + maxId + ", you input No = " + NO);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dir);
        sb.append(File.separator);
        sb.append(dirPrefix);
        sb.append(wrapper(NO));
        sb.append(File.separator);
        sb.append(dirPrefix);
        sb.append(NO);
        sb.append(File.separator);
        String wrapperDir = sb.toString();
        createTestProblem(NO, wrapperDir);
    }

    @Override
    public int getId() {
        int weeksBetween = (int) (ChronoUnit.WEEKS.between(lastestDate, NOW) / times);
        return (int) (this.lastestDateNo + weeksBetween);
    }

    @Override
    public List<String> getUrls(int id) {
        List<Question> questions = getQuestions(id);
        this.questionList = questions;
        return questions.stream().map(Question::getUrl).collect(Collectors.toList());
    }

    /**
     * Gets questions for a contest.
     *
     * @param id contest ID
     * @return list of questions
     */
    public List<Question> getQuestions(int id) {
        String JSONStr = "";
        String apiUrl = "";
        if (isWeekContest(this.dirPrefix)) {
            JSONStr = BuildUrl.getWeeklyContestUrls(String.valueOf(id));
            apiUrl = String.format(BuildUrl.QuestionWeeklyUrlsPattern, id);
        } else if (isBiWeekContest(this.dirPrefix)) {
            JSONStr = BuildUrl.getBiWeeklyContestUrls(String.valueOf(id));
            apiUrl = String.format(BuildUrl.QuestionBiWeeklyUrlsPattern, id);
        } else {
            throw new RuntimeException("Contest type not supported: " + this.dirPrefix);
        }
        return Question.getContestQuestion(JSONStr, apiUrl);
    }

    @Override
    public String parseTestCase(String input) {
        this.testCase = TestCaseUtil.testCaseToString(TEST_CASE.parseContest(input));
        return this.testCase;
    }

    /**
     * Parses code template from question.
     *
     * @param question question data
     * @return parsed code info
     */
    public ParseCodeInfo parseCodeTemplate(Question question) {
        String info = BuildUrl.queryNewContestQuestion(question.getUrl());
        if (StringUtils.kmpSearch(info, "authenticated") != -1) {
            throw new RuntimeException("Authenticated access required. Check your cookie at: " + BuildUrl.request.getConfigPath());
        }
        String p = "\"lang\":\"Java\",\"langSlug\":\"java\"";
        return lcTemplate.parseCodeTemplate(info, p, true);
    }

    @Override
    public ParseCodeInfo parseCodeTemplate(String contestHtml) {
        String info = parseScriptCodeInfo(contestHtml);
        if (StringUtils.isEmpty(info)) {
            return null;
        }
        return lcTemplate.parseCodeTemplate(info);
    }

    @Override
    public void generatorTemplate() {
        // Implementation currently empty
    }

    /**
     * Checks if prefix indicates weekly contest.
     *
     * @param prefix directory prefix
     * @return true if weekly contest
     */
    public static boolean isWeekContest(String prefix) {
        return prefix != null && prefix.equals(WEEKLY_PREFIX);
    }

    /**
     * Checks if prefix indicates bi-weekly contest.
     *
     * @param prefix directory prefix
     * @return true if bi-weekly contest
     */
    public static boolean isBiWeekContest(String prefix) {
        return prefix != null && prefix.equals(BI_WEEKLY_PREFIX);
    }

    /**
     * Parses script code info from HTML.
     *
     * @param input HTML content
     * @return extracted script data
     */
    @SuppressWarnings("all")
    public static String parseScriptCodeInfo(String input) {
        int i = StringUtils.kmpSearch(input, "var pageData");
        if (i == -1) {
            return "";
        }
        input = input.substring(i);
        i = StringUtils.kmpSearch(input, "</script>");
        if (i == -1) {
            return "";
        }
        input = input.substring(0, i);
        return input;
    }

    /**
     * Gets title from input string.
     *
     * @param input input string
     * @return extracted title
     */
    @SuppressWarnings("all")
    public static String getTitle(String input) {
        final String key = "questionTitle:";
        int i = StringUtils.kmpSearch(input, key);
        if (i == -1) {
            return "";
        }
        int i2 = StringUtils.kmpSearch(input, i, ",");
        if (i2 == -1) {
            return "";
        }
        String title = input.substring(i + key.length(), i2);
        title = title.replace(",", "");
        title = title.replace("\'", "");
        title = title.replace("\"", "");
        return title;
    }

    /**
     * Gets method from input string.
     *
     * @param input input string
     * @return extracted method
     */
    @SuppressWarnings("all")
    public static String getMethod(String input) {
        final String t = "{'value': 'java', 'text': 'Java', 'defaultCode':";
        return StringUtils.getMethod(input,t);
    }

    /**
     * Gets current username from LeetCode.
     *
     * @return username or empty string if not found
     */
    public static String getUserName() {
        final String key = "username";
        String userStatusInfo = BuildUrl.userStatus();
        return StringUtils.jsonStrGetValueByKey(userStatusInfo, key);
    }

    /**
     * Automatically creates next contest based on current time.
     *
     * @param aClass reference class for path resolution
     */
    public static void autoCreateNext(Class<?> aClass) {
        Objects.requireNonNull(aClass,"Class cannot be null");
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour >= 10 && hour <= 12 && DAYS[today] == 7) {
            if (minute >= 30) {
                System.out.println("Weekly contest starting!");
                WEEK_CONTEST.setClass(aClass, null);
                WEEK_CONTEST.next();
            } else {
                continueRun(minute, aClass);
            }
        } else if (hour >= 22 && DAYS[today] == 6) {
            if (minute >= 30) {
                System.out.println("Bi-weekly contest starting!");
                BI_WEEK_CONTEST.setClass(aClass, null);
                BI_WEEK_CONTEST.next();
            } else {
                continueRun(minute, aClass);
            }
        } else {
            System.out.println("No contests scheduled to start now.");
        }
    }

    /**
     * Continues waiting for contest start time.
     *
     * @param minute current minute
     * @param aClass reference class
     */
    public static void continueRun(int minute, Class<?> aClass) {
        int rest = 30 - minute;
        System.out.println("Contest starts in " + rest + " minutes...");
        try {
            Thread.sleep(1000L * 60);
        } catch (InterruptedException ignored) {
        }
        autoCreateNext(aClass);
    }

    /**
     * Creates README.md file summarizing the contest.
     *
     * @param NO        contest number
     * @param dir       target directory
     * @param questions list of contest questions
     */
    public void createReadme(int NO, String dir, List<Question> questions) {
        StringBuilder content = new StringBuilder();
        boolean isBiWeekly = dir.contains(BI_WEEK_DRI);
        String parentDir = new File(new File(dir).getParent()).getParent();

        content.append("## \uD83C\uDFC6 第 ").append(NO).append(" 场").append(isBiWeekly ? "双" : "").append("周赛\n");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if (q == null) continue;

            content.append("- [ ] ")
                    .append("[").append(q.getTitle()).append("]")
                    .append("(").append(q.getUrl()).append(")\n ")
                    .append("[").append("🎈代码").append("]")
                    .append("(").append(javafiles.get(i).replace(parentDir,"").replace("\\","/").substring(1)).append(")\n");
        }

        if (CREATE_SOLUTION_ME) {
            IoUtil.writeContent(new File(dir + "solution.md"), content.toString());
        }

        if (CREATE_READ_ME_FATHER) {
            File file = new File(parentDir + File.separator + "readme.md");
            String existingContent = file.exists() ? IoUtil.readContent(file) : "";
            IoUtil.writeContent(file, content.toString() + (StringUtils.isEmpty(existingContent) ? "" : "\n\n\n" + existingContent));
        }
    }
}
