package code_generation.contest;

/**
 * Interface defining operations for custom problem handling and template generation.
 * Provides default implementations for common operations while allowing customization.
 * @author: wuxin0011
 * @since 1.0
 */
public interface CustomProblem {

    /**
     * Default implementation that starts problem processing without input requirement.
     * Delegates to {@link #start(Class, boolean)} with input flag set to false.
     *
     * @param c the target class to process
     */
    default void start(Class<?> c) {
        start(c, false);
    }

    /**
     * Main method to start custom template generation for a given class.
     * Implementing classes must provide concrete implementation.
     *
     * @param c the target class to process
     * @param input flag indicating whether input handling is required
     */
    void start(Class<?> c, boolean input);

    /**
     * Creates a problem template with the given problem information.
     * Provides default implementation that displays the problem info.
     * Implementing classes should override for actual file creation.
     *
     * @param problemInfo contains metadata about the problem to be created
     */
    default void createTemplate(ProblemInfo problemInfo) {
    }
}