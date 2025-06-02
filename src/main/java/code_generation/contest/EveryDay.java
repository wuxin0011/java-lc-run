package code_generation.contest;

import code_generation.utils.ProblemEveryDayUtils;

import java.util.Objects;
import java.util.Scanner;


/**
 * Implementation of {@link CustomProblem} for daily coding problem processing.
 * Handles the creation of daily problem files with interactive input capabilities.
 * @author: wuxin0011
 * @since 1.0
 */
public class EveryDay implements CustomProblem {

    /**
     * Singleton instance of EveryDay.
     */
    public static final EveryDay EVERY_DAY = new EveryDay();

    /**
     * Starts problem processing with default input requirement (true).
     * Delegates to {@link #start(Class, boolean)} with input flag set to true.
     *
     * @param c the target class to process (must not be null)
     */
    @Override
    public void start(Class<?> c) {
        start(c, true);
    }

    /**
     * Starts the daily problem processing workflow.
     * <p>
     * When input is required:
     * <ol>
     *   <li>Prompts user for problem number input</li>
     *   <li>Validates the input format</li>
     * </ol>
     * Creates appropriate directory structure and files for the problem.
     *
     * @param c the target class to process (must not be null)
     * @param input flag indicating whether interactive input is required
     * @throws NullPointerException if the class parameter is null
     */
    @Override
    public void start(Class<?> c, boolean input) {
        Objects.requireNonNull(c, "Class parameter cannot be null");

        String id = "";
        if (input) {
            Scanner scanner = new Scanner(System.in);
            do {
                System.out.print("Please input a No as problem NO: ");
                id = scanner.next();
            } while (!ProblemEveryDayUtils.check(id));
        }

        int count = Math.max(ProblemEveryDayUtils.getJavaFileCount(c), 0);
        String dir = ProblemEveryDayUtils.createPrefix(count);
        String base = ProblemEveryDayUtils.convertDir(count);
        String name = dir + Constant.FILE_PREFIX + base;

        if (input) {
            name = name + "_" + id;
        }

        String javaPath = ProblemEveryDayUtils.buildJavaFilePath("", name);
        String txtPath = ProblemEveryDayUtils.buildTxtFilePath(dir, name);
        Problem.create(javaPath, txtPath, c);
    }
}
