package code_generation.crwal.leetcode;

import code_generation.contest.ProblemInfo;
import code_generation.utils.IoUtil;
import code_generation.utils.StringUtils;

/**
 * Custom LeetCode problem template generator that uses hyphenated naming convention
 * Extends {@link LCCustom} to provide specialized naming and file organization.
 * @author wuxin0011
 * @since 1.0
 */
public class LCProblemTemplate extends LCCustom {

    /** Default class name for generated solution files */
    private String className = "Solution";

    /** Default text file name for test cases */
    private String txtName = IoUtil.DEFAULT_READ_FILE;

    /**
     * Constructs a new LCProblemTemplate with specified parameters.
     *
     * @param aClass     the reference class for path resolution
     * @param className  the custom class name (uses "Solution" if null or empty)
     * @param txtName    the custom text file name (uses default if null or empty)
     */
    public LCProblemTemplate(Class<?> aClass, String className, String txtName) {
        super(aClass);
        this.className = StringUtils.isEmpty(className) ? "Solution" : className;
        this.txtName = StringUtils.isEmpty(txtName) ? "Solution" : txtName;
    }

    /**
     * Constructs a new LCProblemTemplate with default names.
     *
     * @param aClass the reference class for path resolution
     */
    public LCProblemTemplate(Class<?> aClass) {
        super(aClass);
    }

    /**
     * Implements the problem generation logic with hyphenated naming convention.
     * Converts title slug (e.g., "AAA-B-C") to file naming format (e.g., "AAA_B_C").
     * Creates problem files using the configured class and text file names.
     */
    @Override
    public void next() {
        // Convert title slug to proper case name (AAA-B-C => AAA_B_C)
        String prefix_dir = StringUtils.toCaseName(this.titleSlug);

        // Create problem info with configured names and converted prefix
        ProblemInfo problemInfo = new ProblemInfo(
                this.className,
                this.txtName,
                prefix_dir,
                testCase,
                classTemplate,
                aClass
        );

        // Generate the template files
        createTemplate(problemInfo);
    }
}