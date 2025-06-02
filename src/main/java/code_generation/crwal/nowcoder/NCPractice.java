package code_generation.crwal.nowcoder;

import code_generation.contest.ClassTemplate;
import code_generation.contest.Problem;
import code_generation.contest.ProblemInfo;
import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * A concrete implementation of NCCustom for handling NowCoder practice problems.
 * Provides specific functionality for parsing and processing coding practice problems from NowCoder platform.
 * @author wuxin0011
 * @since 1.0
 */
public class NCPractice extends NCCustom {

    /** The practice problem being processed */
    Practice practice;

    /** Default prefix for generated solution files */
    public static final String PREFIX = "Solution";

    /**
     * Constructs an NCPractice instance with default settings.
     * @param c The target class to associate with this processor
     */
    public NCPractice(Class<?> c) {
        this(c, null, null, null, false);
    }

    /**
     * Constructs an NCPractice instance with custom settings.
     * @param c The target class to associate with this processor
     * @param prefix The prefix to use for generated files (uses default if null or empty)
     * @param javaClass The name for the generated Java class (uses default if null or empty)
     * @param txt The name for the generated text file (uses default if null or empty)
     * @param is Flag indicating whether to create a README file
     */
    public NCPractice(Class<?> c, String prefix, String javaClass, String txt, boolean is) {
        this.aClass = c;
        this.prefix = StringUtils.isEmpty(prefix) ? PREFIX : prefix;
        this.className = StringUtils.isEmpty(javaClass) ? PREFIX : javaClass;
        this.txtFile = StringUtils.isEmpty(txt) ? IoUtil.DEFAULT_READ_FILE : txt;
        this.createReadme = is;
    }

    /**
     * Starts processing for the given class with specified input mode.
     * Prompts user for a NowCoder practice problem URL until a valid one is provided.
     * @param c The class to process (cannot be null)
     * @param input Whether to prompt for user input
     * @throws NullPointerException if the class parameter is null
     */
    @Override
    public void start(Class<?> c, boolean input) {
        Objects.requireNonNull(c, "class not allow null");
        this.aClass = c;
        Scanner scanner = new Scanner(System.in);
        String s = "";
        do {
            System.out.println("input https://www.nowcoder.com/exam/oj problems url:");
            s = scanner.next();
        } while (!support(s));

        parse(s);
    }

    /**
     * Parses a NowCoder practice problem from the given URL.
     * Extracts problem information, generates code templates, and creates necessary files.
     * @param url The URL of the NowCoder practice problem to parse
     */
    @Override
    public void parse(String url) {
        String input = NCUrl.getPractice(url);
        this.practice = new Practice(input);
        this.parseCodeInfo = practice.codeInfo;
        this.classTemplate = new ClassTemplate();
        String test = practice.test;

        classTemplate.buildClassName(className)
                .buildTitle("")
                .buildCodeInfo(parseCodeInfo)
                .buildMethod(parseCodeInfo.getMethod())
                .buildMethodName(parseCodeInfo.getMethodName())
                .buildTextFileName(txtFile)
                .buildUrl(url);
        String prefix_dir = this.prefix + "_" + StringUtils.getDirCount(aClass, prefix);
        ProblemInfo problemInfo = new ProblemInfo(className, txtFile, prefix_dir, test, classTemplate, aClass);
        this.info = problemInfo;
        createTemplate(problemInfo);
        if (createReadme) {
            IoUtil.writeContent(aClass, prefix_dir + File.separator + "readme.md", "");
        }
    }

    /**
     * Creates the problem template using the provided problem information.
     * @param problemInfo The problem metadata to use for template creation
     */
    @Override
    public void createTemplate(ProblemInfo problemInfo) {
        Problem.create(problemInfo);
    }

    /**
     * Checks if the given URL is a valid NowCoder practice problem URL.
     * @param s The URL to validate
     * @return true if the URL starts with the NowCoder practice prefix, false otherwise
     */
    public boolean support(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.startsWith(NCUrl.PRACTICE_PREFIX);
    }
}