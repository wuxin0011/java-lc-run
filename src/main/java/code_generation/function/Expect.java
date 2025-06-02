package code_generation.function;

/**
 * Represents a functional interface for defining expectation logic between two values with an associated message.
 * This interface is intended to be used in testing or validation scenarios where a condition needs to be verified.
 * The expectation logic is implemented through the single abstract method {@code expect}, which accepts two values
 * and a message. If the expectation is not met, the implementation may throw an exception or handle the failure
 * as appropriate.
 *
 * This interface is annotated with {@code @FunctionalInterface}, indicating that it is designed to be used as
 * a lambda expression or method reference. It is generic, allowing flexibility in the types of values and messages
 * it can handle.
 * @author wuxin0011
 * @since 1.0
 */
@FunctionalInterface
public interface Expect<V, M> {

    /**
     * Defines the expectation logic to compare two values and handle the result based on the provided message.
     * This method is used to verify conditions between the source value and the expected value, typically in testing scenarios.
     * If the condition defined by the implementation is not met, the method may log a failure message or throw an exception.
     *
     * @param src     the source value to be compared, representing the actual result or state
     * @param dist    the expected value against which the source value is compared, representing the desired result or state
     * @param message the message associated with the expectation, providing context or details about the comparison
     */
    void expect(V src, V dist, M message);

}
