package code_generation.crwal.leetcode;

import code_generation.contest.*;
import code_generation.crwal.TestCaseUtil;
import code_generation.utils.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for custom LeetCode problem processing.
 * Implements {@link CustomProblem} interface and provides common functionality
 * for parsing and generating LeetCode problem templates.
 * @author: wuxin0011
 * @since 1.0
 */
public abstract class LCCustom implements CustomProblem {

    /** Template processor for LeetCode problems */
    public static final LCTemplate lcTemplate = new LCTemplate();

    /** Test case processor for LeetCode problems */
    public static final LCTestCase lcTestCase = new LCTestCase();

    /** Class template builder */
    public ClassTemplate classTemplate = new ClassTemplate();

    /** Reference class for path resolution */
    public Class<?> aClass;

    /** Frontend question ID */
    public String frontendQuestionId;

    /** Test case content */
    public String testCase;

    /** Problem title slug */
    public String titleSlug;

    /** Translated problem title (unicode format) */
    public String translatedTitle;

    /** Translated problem content/description */
    public String translatedContent;

    /** Parsed code information */
    public ParseCodeInfo parseCodeInfo;

    /** Default constructor */
    public LCCustom() {
    }

    /**
     * Constructs with reference class.
     *
     * @param aClass reference class for path resolution
     */
    public LCCustom(Class<?> aClass) {
        this.aClass = aClass;
    }

    /**
     * Runs problem processing with default input mode.
     */
    public void run() {
        this.start(this.aClass, true);
    }

    /**
     * Starts problem processing with default input mode.
     *
     * @param c reference class for path resolution
     */
    @Override
    public void start(Class<?> c) {
        start(c, true);
    }

    /**
     * Starts problem processing.
     *
     * @param c      reference class for path resolution
     * @param input  whether to prompt for input
     */
    @Override
    public void start(Class<?> c, boolean input) {
        Objects.requireNonNull(c, "Class cannot be null");
        this.aClass = c;

        List<String> urls = new ArrayList<>();
        if (input) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Input content containing https://leetcode.cn/problems/xxxxxx/ or enter NO to exit: \n");

            StringBuilder inputContent = new StringBuilder();
            String line = "";
            while (!StringUtils.isEmpty((line = scanner.nextLine()))) {
                if (StringUtils.isEmpty(line)) {
                    break;
                }
                if ("NO".equalsIgnoreCase(line) || "exit".equalsIgnoreCase(line)) {
                    System.exit(0);
                }
                inputContent.append(line).append("\n");
            }

            urls.addAll(matchLeetCodeUrls(inputContent.toString()));
        }

        int sleepTime = urls.size() > 10 ? 10 : 5;
        System.out.println("Total problems: " + urls.size());
        System.out.println("=======================Start==================\n");
        for (int i = 0; i < urls.size(); i++) {
            String lcUrl = urls.get(i);
            System.out.printf("Processing URL %d: %s (waiting %d seconds)\n", i + 1, lcUrl, sleepTime);
            ExceptionUtils.sleep(sleepTime);
            try {
                createByTitleSlug(lcUrl);
            } catch (Exception ignore) {

            }
        }
        System.out.println("\n=======================End==================");
    }

    /**
     * Creates problem template from title slug.
     *
     * @param titleSlug problem URL or slug
     */
    public void createByTitleSlug(String titleSlug) {
        titleSlug = BuildUrl.buildTitleSlug(titleSlug);
        this.titleSlug = titleSlug;

        // Get code template
        String code = BuildUrl.questionEditorData(titleSlug);

        // Parse code template
        ParseCodeInfo parseCodeInfo = lcTemplate.parseCodeTemplate(code);
        Objects.requireNonNull(parseCodeInfo,"parseCodeInfo cannot be null");

        this.parseCodeInfo = parseCodeInfo;
        String method = parseCodeInfo.getMethod();
        String methodName = parseCodeInfo.getMethodName();

        if(StringUtils.isEmpty(this.frontendQuestionId)){
            this.frontendQuestionId = StringUtils.jsonStrGetValueByKey(code, "questionFrontendId");
        }
        String url = BuildUrl.LC_PROBLEM_PREFIX + "/" + titleSlug;

        // Get problem translations
        String questionTranslationInfo = BuildUrl.questionTranslations(titleSlug);

        // Check if modulo operation needed
        boolean isNeedMod = StringUtils.isNeedMOD(questionTranslationInfo);

        // Get problem description
        this.translatedContent = StringUtils.jsonStrGetValueByKey(questionTranslationInfo, "translatedContent");

        // Get problem title (unicode format)
        this.translatedTitle = StringUtils.jsonStrGetValueByKey(questionTranslationInfo, "translatedTitle");

        // Parse test cases
        this.testCase = TestCaseUtil.testCaseToString(lcTestCase.parseDefault(translatedContent));

        // Convert unicode title to Chinese
        String tempUnicodeTitle = StringUtils.unicodeToChinese(translatedTitle);
        titleSlug = !StringUtils.isEmpty(tempUnicodeTitle) ? tempUnicodeTitle : titleSlug;

        System.out.println("Processing problem: " + CustomColor.error(titleSlug));

        // Build class template
        classTemplate.buildIsNeedMod(isNeedMod)
                .buildTitle(titleSlug)
                .buildUrl(url)
                .buildMethod(method)
                .buildCodeInfo(parseCodeInfo)
                .buildAuthor(LCContest.getUserName())
                .buildMethodName(methodName);

        // Proceed to implementation-specific processing
        next();
    }

    /**
     * Abstract method for custom problem processing.
     * Implementations should define:
     * - Java file name via classTemplate.buildClassName
     * - Text file name via classTemplate.buildTextFileName
     * - Directory prefix
     * Package info will be automatically created based on Java file location.
     */
    public abstract void next();

    /**
     * Creates problem template from ProblemInfo.
     *
     * @param problemInfo contains all problem creation parameters
     */
    @Override
    public void createTemplate(ProblemInfo problemInfo) {
        autoCreatePackageInfo(problemInfo);
        updateClassName(problemInfo);
        Problem.create(problemInfo);
    }

    /**
     * Automatically generates package info based on file path.
     *
     * @param problemInfo problem information container
     */
    public void autoCreatePackageInfo(ProblemInfo problemInfo) {
        if (problemInfo == null) {
            System.out.println("Failed to auto-create package info: problemInfo is null");
            return;
        }
        String absolutePath = IoUtil.wrapperAbsolutePath(aClass, problemInfo.getJavaFile());
        String packageInfo = ReflectUtils.getPackageInfo(absolutePath);
        classTemplate.buildPackageInfo(packageInfo);
    }

    /**
     * Updates class name in problem info.
     * Default implementation does nothing.
     *
     * @param problemInfo problem information container
     */
    public void updateClassName(ProblemInfo problemInfo) {
        // Default implementation does nothing
    }

    /**
     * Matches LeetCode URLs from input string.
     *
     * @param s input string containing URLs
     * @return list of filtered LeetCode URLs
     */
    public static List<String> matchLeetCodeUrls(String s) {
        if (StringUtils.isEmpty(s)) {
            return Collections.emptyList();
        }
        List<String> urls = StringUtils.matchUrls(s);
        return urls.stream()
                .filter(url -> {
                    if (StringUtils.isEmpty(url)) return false;
                    return url.startsWith(BuildUrl.LC_PROBLEM_PREFIX) ||
                            url.startsWith(BuildUrl.LC_WEEKLY_CONTEST_PREFIX) ||
                            url.startsWith(BuildUrl.LC_CLASS_THEME_PREFIX) ||
                            url.startsWith(BuildUrl.LC_BI_WEEKLY_CONTEST_PREFIX);
                })
                .map(u -> {
                    if (u.startsWith(BuildUrl.LC_CLASS_THEME_PREFIX)) {
                        return u.replace(BuildUrl.LC_CLASS_THEME_PREFIX, BuildUrl.LC_PROBLEM_PREFIX);
                    }
                    String[] sss = u.split("problems");
                    return BuildUrl.LC_PROBLEM_PREFIX + sss[sss.length - 1];
                })
                .collect(Collectors.toList());
    }
}