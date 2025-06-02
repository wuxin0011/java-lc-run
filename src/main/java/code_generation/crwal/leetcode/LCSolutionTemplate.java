package code_generation.crwal.leetcode;

import code_generation.contest.ProblemInfo;
import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;

import java.io.File;


/**
 * Custom solution template generator for LeetCode problems.
 * Extends {@link LCCustom} to provide specialized solution file generation
 * with configurable naming and directory structure.
 * @author wuxin0011
 * @since 1.0
 */
public class LCSolutionTemplate extends LCCustom {

    /** Default prefix for solution files */
    private static final String Custom_Prefix = "Solution";

    /** Reference class for path resolution */
    private Class<?> aClass;

    /** Custom prefix for solution files */
    private String prefix;

    /** Class name for generated solution */
    private String className;

    /** Text file name for test cases */
    private String txtFile;

    /** Flag indicating whether to create readme.md file */
    private boolean createReadme;

    /** Private default constructor */
    private LCSolutionTemplate() {
    }

    /**
     * Constructs with reference class only.
     *
     * @param aClass reference class for path resolution
     */
    public LCSolutionTemplate(Class<?> aClass) {
        this(aClass, null, null, null);
    }

    /**
     * Constructs with reference class and readme flag.
     *
     * @param aClass reference class for path resolution
     * @param createReadme whether to create readme.md file
     */
    public LCSolutionTemplate(Class<?> aClass, boolean createReadme) {
        this(aClass, null, null, null, createReadme);
    }

    /**
     * Constructs with reference class and custom prefix.
     *
     * @param aClass reference class for path resolution
     * @param prefix custom prefix for solution files
     */
    public LCSolutionTemplate(Class<?> aClass, String prefix) {
        this(aClass, prefix, null, null);
    }

    /**
     * Constructs with reference class and naming configuration.
     *
     * @param aClass reference class for path resolution
     * @param prefix custom prefix for solution files
     * @param className custom class name for solution
     * @param txtFile custom text file name for test cases
     */
    public LCSolutionTemplate(Class<?> aClass, String prefix, String className, String txtFile) {
        this(aClass, prefix, className, txtFile, false);
    }

    /**
     * Full constructor with all configuration options.
     *
     * @param aClass reference class for path resolution
     * @param prefix custom prefix for solution files
     * @param className custom class name for solution
     * @param txtFile custom text file name for test cases
     * @param createReadme whether to create readme.md file
     */
    public LCSolutionTemplate(Class<?> aClass, String prefix, String className, String txtFile, boolean createReadme) {
        super(aClass);
        this.aClass = aClass;
        this.prefix = StringUtils.isEmpty(prefix) ? Custom_Prefix : prefix;
        this.className = StringUtils.isEmpty(className) ? Custom_Prefix : className;
        this.txtFile = StringUtils.isEmpty(txtFile) ?
                IoUtil.DEFAULT_READ_FILE :
                txtFile.lastIndexOf(".txt") != -1 ? txtFile : (txtFile + ".txt");
        this.createReadme = createReadme;
    }

    /**
     * Implements the solution generation logic.
     * Creates:
     * - Java solution file with configured class name
     * - Test case text file
     * - Optional readme.md file
     * Uses automatic directory numbering based on existing files.
     */
    @Override
    public void next() {
        // Create directory prefix with automatic numbering
        String prefix_dir = this.prefix + "_" + StringUtils.getDirCount(aClass, prefix);

        // Create problem info with configured names
        ProblemInfo problemInfo = new ProblemInfo(
                this.className,
                this.txtFile,
                prefix_dir,
                testCase,
                classTemplate,
                aClass
        );

        // Create readme if enabled
        if (createReadme) {
            IoUtil.writeContent(aClass, prefix_dir + File.separator + "readme.md", "");
        }

        // Generate template files
        createTemplate(problemInfo);
    }
}