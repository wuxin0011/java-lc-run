package code_generation.crwal.leetcode;

import code_generation.contest.ClassTemplate;
import code_generation.contest.Constant;
import code_generation.contest.ProblemInfo;
import code_generation.utils.ProblemEveryDayUtils;
import code_generation.utils.StringUtils;

import java.util.Objects;
import java.util.Scanner;

/**
 * Implementation of {@link LCCustom} for daily LeetCode problem processing.
 * Handles generation of daily problem templates and file organization.
 * @author: wuxin0011
 * @since 1.0
 */
public class LCEveryDay extends LCCustom {

    /** Singleton instance of LCEveryDay */
    public static LCEveryDay everyDay = new LCEveryDay();

    /**
     * Starts daily problem processing.
     *
     * @param c      reference class for path resolution
     * @param input  whether to prompt for input (unused in this implementation)
     * @throws NullPointerException if class parameter is null
     */
    @Override
    public void start(Class<?> c, boolean input) {
        Objects.requireNonNull(c, "Class cannot be null");
        this.aClass = c;
        this.classTemplate = new ClassTemplate();

        // Get today's problem
        String questionOfToday = BuildUrl.questionOfToday();
        this.frontendQuestionId = StringUtils.jsonStrGetValueByKey(questionOfToday, "frontendQuestionId");
        String titleSlug = StringUtils.jsonStrGetValueByKey(questionOfToday, "titleSlug");

        System.out.printf("Accessing today's problem URL: %s/%s\n", BuildUrl.LC_PROBLEM_PREFIX, titleSlug);
        createByTitleSlug(titleSlug);
    }

    /**
     * Custom problem processing with interactive input.
     *
     * @param c reference class for path resolution
     * @throws NullPointerException if class parameter is null
     */
    public void custom(Class<?> c) {
        Objects.requireNonNull(c, "Class cannot be null");
        this.aClass = c;
        this.classTemplate = new ClassTemplate();
        Scanner scanner = new Scanner(System.in);

        String titleSlug = "";
        do {
            System.out.print("Input a URL (https://leetcode.cn/problems/xxxxxx/) or 'NO' to exit: \n");
            titleSlug = scanner.next();
            if ("exit".equalsIgnoreCase(titleSlug) || "NO".equalsIgnoreCase(titleSlug)) {
                System.exit(-1);
            }
            // Handle classic theme URLs
            if (!StringUtils.isEmpty(titleSlug) && titleSlug.startsWith(BuildUrl.LC_CLASS_THEME_PREFIX)) {
                titleSlug = titleSlug.replace(BuildUrl.LC_CLASS_THEME_PREFIX, BuildUrl.LC_PROBLEM_PREFIX);
            }
        } while (StringUtils.isEmpty(titleSlug) || !titleSlug.startsWith(BuildUrl.LC_PROBLEM_PREFIX));

        createByTitleSlug(titleSlug);
    }

    /**
     * Implements custom problem generation logic.
     * Handles:
     * - Directory structure creation
     * - File naming conventions
     * - Template generation
     */
    @Override
    public void next() {
        // Calculate file count and create directory structure
        int count = Math.max(ProblemEveryDayUtils.getJavaFileCount(aClass), 0);
        String dir = ProblemEveryDayUtils.createPrefix(count);
        String base = ProblemEveryDayUtils.convertDir(count);

        // Create file names
        String name = dir + Constant.FILE_PREFIX + base;
        name = name + "_" + this.frontendQuestionId;

        // Build file paths
        String javaPath = ProblemEveryDayUtils.buildJavaFilePath("", name);
        String txtPath = ProblemEveryDayUtils.buildTxtFilePath(dir, name);

        // Configure text file name in template
        classTemplate.buildTextFileName(txtPath.replace(dir, ""));

        // Create and process problem info
        ProblemInfo problemInfo = new ProblemInfo(javaPath, txtPath, testCase, classTemplate, aClass);
        createTemplate(problemInfo);
    }
}