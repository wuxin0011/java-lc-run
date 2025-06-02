package code_generation.utils;

import code_generation.function.ThrowableFunction;

import javax.sql.rowset.Predicate;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Utility class for handling operations that may throw exceptions in a functional programming context.
 * This class provides static methods to execute operations encapsulated by the {@code ThrowableFunction}
 * interface while managing exceptions in a controlled manner. It is designed to simplify exception handling
 * and provide flexibility in how exceptions are processed during execution.
 *
 * The primary feature of this class is the ability to execute operations with optional exception suppression.
 * Additionally, it includes utility methods for performing common tasks, such as sleeping for a random duration,
 * while ensuring that any exceptions thrown during execution are handled appropriately.
 * @author wuxin0011
 * @since 1.0
 */
public class ExceptionUtils {




    /**
     * Executes the provided operation encapsulated by the {@code ThrowableFunction} interface
     * while handling any exceptions that may occur during its execution.
     * This method simplifies exception management by wrapping the execution of the function
     * in a try-catch block. If an exception is thrown and exception suppression is not enabled,
     * the exception's stack trace will be printed to the standard error stream.
     *
     * This method is particularly useful in scenarios where functional programming patterns are applied,
     * and operations that may throw exceptions need to be executed in a controlled manner.
     *
     * @param function the operation to be executed, encapsulated as a {@code ThrowableFunction}.
     *                 This parameter must not be null and should define the logic to be executed.
     */
    public static void executeWithExceptionHandling(ThrowableFunction function) {
        executeWithExceptionHandling(function, false);
    }

    /**
     * Executes the provided operation encapsulated by the {@code ThrowableFunction} interface
     * while handling any exceptions that may occur during its execution. This method simplifies
     * exception management by wrapping the execution of the function in a try-catch block.
     * If an exception is thrown and exception suppression is not enabled, the exception's stack
     * trace will be printed to the standard error stream.
     *
     * This method is particularly useful in scenarios where functional programming patterns are
     * applied, and operations that may throw exceptions need to be executed in a controlled manner.
     *
     * @param function the operation to be executed, encapsulated as a {@code ThrowableFunction}.
     *                 This parameter must not be null and should define the logic to be executed.
     * @param isIgnore a boolean flag indicating whether exceptions should be ignored. If set to true,
     *                 any exceptions thrown during the execution of the function will be suppressed,
     *                 and no stack trace will be printed. If set to false, the stack trace of any
     *                 exception will be printed to the standard error stream.
     */
    public static void executeWithExceptionHandling(ThrowableFunction function, boolean isIgnore) {
        try {
            function.apply();
        } catch (Exception e) {
            if (!isIgnore) {
                e.printStackTrace();
            }

        }
    }


    /**
     * Pauses the current thread for a random duration of time, up to a specified maximum limit in seconds.
     * The actual sleep duration is determined by multiplying the input parameter by 1000 (to convert seconds
     * to milliseconds) and then scaling it by a random factor between 0 and 1. This method leverages
     * {@link ExceptionUtils#executeWithExceptionHandling(ThrowableFunction)} to handle any exceptions that
     * may occur during the sleep operation.
     *
     * @param s the maximum sleep duration in seconds. Must be a non-negative integer. The actual sleep time
     *          will be a random value between 0 and this maximum limit.
     */
    public static void sleep(int s) {
        executeWithExceptionHandling(() -> Thread.sleep((long) (Math.random() * 1000 * s)));
    }
}
