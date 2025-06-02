package code_generation.crwal.nowcoder;

import code_generation.contest.ClassTemplate;
import code_generation.contest.CustomProblem;
import code_generation.contest.ParseCodeInfo;
import code_generation.contest.ProblemInfo;
import code_generation.utils.IoUtil;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;


/**
 * An abstract base class for implementing custom problem parsers.
 * Provides common functionality for parsing coding problems and generating related files.
 * @author wuxin0011
 * @since 1.0
 */
public abstract class NCCustom implements CustomProblem {
    /** The target class being processed */
    public Class<?> aClass;
    /** Information about the parsed code structure */
    public ParseCodeInfo parseCodeInfo;
    /** Template for generating class files */
    public ClassTemplate classTemplate;
    /** Metadata about the problem being processed */
    public ProblemInfo info;
    /** Prefix used for generated files/classes */
    public String prefix;
    /** Name of the generated class */
    public String className;
    /** Name of the text file to generate */
    public String txtFile;
    /** Flag indicating whether to create a README.md file */
    boolean createReadme;

    /**
     * Starts processing for the given class with default input mode.
     * @param c The class to process (cannot be null)
     */
    @Override
    public void start(Class<?> c) {
        this.start(c, true);
    }

    /**
     * Starts processing for the given class with specified input mode.
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
            System.out.println("input a url : ");
            s = scanner.next();
        } while (!support(s));

        parse(s);
    }

    /**
     * Runs the processor with default settings.
     * Equivalent to calling start(aClass, true).
     */
    public void run() {
        this.start(aClass, true);
    }

    /**
     * Parses the problem from the given URL.
     * This must be implemented by concrete subclasses.
     * @param url The URL of the problem to parse
     */
    public abstract void parse(String url);

    /**
     * Checks if the given URL is supported by this parser.
     * This must be implemented by concrete subclasses.
     * @param s The URL to check
     * @return true if the URL is supported, false otherwise
     */
    public abstract boolean support(String s);
}
